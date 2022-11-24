package com.aias.dispatcher.server;

import com.aias.dispatcher.server.dingtalk.DingParam;
import com.aias.dispatcher.server.dingtalk.DingTalk;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dispatch")
@Slf4j
public class Controller {

    @Resource
    private DingTalk dingTalk;

    @GetMapping("/ding")
    public void ding(@RequestParam("phone") String phone, @RequestParam("content") String content) {
        try{
            DingParam dingParam = DingParam.builder().type("text").messageUrl("www.baidu.com")
                .picUrl("https://profile.csdnimg.cn/0/F/8/0_rokkki").build();
            MessageParam param =
                MessageParam.builder().atList(Lists.newArrayList(phone)).content(content).title("title").ding(dingParam)
                    .build();
            dingTalk.notify("0ccd9996d06003b8b49568d5e017125c3291b5593af1be8fe27b4694da5f5f42",
                "SEC6b112f9544836f4bba8f609a63d08e707ce9ec6f08c4b11da3b4ac2d060c3f5e", param);
        }catch (Exception e){
            log.error("发送消息报错",e);
        }
    }
}
