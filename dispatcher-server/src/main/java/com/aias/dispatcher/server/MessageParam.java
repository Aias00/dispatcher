package com.aias.dispatcher.server;

import com.aias.dispatcher.server.dingtalk.DingParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author liuhy
 * @version 1.0
 * @since 2021/10/25
 */
@Getter
@Setter
@Builder
public class MessageParam {

    /**
     * 发送信息类型
     */
    private String sendType;

    /**
     * 标题
     */
    private String title;

    /**
     * 信息内容
     */
    private String content;

    /**
     * 提醒人集合
     * 可以是手机号、邮箱
     */
    private List<String> atList;

    /**
     * 钉钉类型参数
     */
    private DingParam ding;
}
