package com.aias.dispatcher.server.dingtalk;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aias.dispatcher.server.MessageParam;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuhy
 * @version 1.0
 * @since 2021/10/26
 */
@Slf4j
@Component
public class DingTalk  {

    private static final String SEND_URL = "https://oapi.dingtalk.com/robot/send";

    private String buildRequestUrl(String token, String secret) {
        String url = String.format("%s?access_token=%s", SEND_URL, token);
        if (StringUtils.isNoneBlank(secret)) {
            long timeStamp = System.currentTimeMillis();
            url += "&timestamp=" + timeStamp;
            String stringToSign = timeStamp + "\n" + secret;
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
                url += "&sign=" + sign;
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    private Map<String, Object> buildRequestParam(MessageParam notifyParam) {
        Map<String, Object> reqMap = Maps.newHashMap();
        Map<String, String> contentMap = Maps.newHashMap();
        //?????????
        Map<String, Object> atMap = Maps.newHashMap();
        //1.?????????????????????
        atMap.put("isAtAll", false);
        //2.????????????????????????????????????
        atMap.put("atMobiles", notifyParam.getAtList());
        reqMap.put("at", atMap);
        //3.??????
        if (Objects.nonNull(notifyParam.getDing()) && notifyParam.getDing().getType().equals("actionCard")
            && StringUtils.isNoneBlank(notifyParam.getDing().getMessageUrl())) {
            reqMap.put("msgtype", "actionCard");
            String title = StringUtils.isBlank(notifyParam.getTitle()) ? "???????????????" : notifyParam.getTitle();
            contentMap.put("title", title);
            StringBuilder stringBuilder = new StringBuilder();
            if (StringUtils.isNotBlank(notifyParam.getDing().getPicUrl())) {
                stringBuilder.append("![screenshot](").append(notifyParam.getDing().getPicUrl()).append(")\n");
            }
            stringBuilder.append("### ").append(title).append("\n");
            stringBuilder.append(notifyParam.getContent());
            contentMap.put("text", stringBuilder.toString());
            contentMap.put("singleTitle", "????????????");
            contentMap.put("singleURL", notifyParam.getDing().getMessageUrl());
            reqMap.put("actionCard", contentMap);
        } else if (Objects.nonNull(notifyParam.getDing()) && notifyParam.getDing().getType().equals("markdown")) {
            reqMap.put("msgtype", "actionCard");

        } else {
            reqMap.put("msgtype", "text");
            contentMap.put("content", notifyParam.getContent());
            reqMap.put("text", contentMap);
        }
        return reqMap;
    }

    public DingResult notify(String token, String secret, MessageParam messageParam) {
        log.info("??????ding??????:{}", JSONUtil.toJsonStr(messageParam));
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("ding robot token can not be blank!");
        }
        Map<String, Object> reqMap = buildRequestParam(messageParam);
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=utf-8");
        HttpEntity<String> httpEntity = new HttpEntity<>(JSONUtil.toJsonStr(reqMap), headers);
        ResponseEntity<DingResult> response =
            restTemplate.postForEntity(buildRequestUrl(token, secret), httpEntity, DingResult.class);
        DingResult dingResult = response.getBody();
        log.info("??????dingding?????????????????????{}", JSONUtil.toJsonStr(dingResult));
        if (null == dingResult) {
            log.error("??????dingding????????????,???????????????{}", JSONUtil.toJsonStr(response));
            throw new RuntimeException(JSONUtil.toJsonStr(response));
        }
        return dingResult;
    }
}
