package com.aias.dispatcher.server.dingtalk;

import lombok.Builder;
import lombok.Data;

/**
 * 钉钉机器人配置参数
 *
 * @author 王一飞
 * @version 1.0
 * @since 2021/10/31
 */
@Data
@Builder
public class DingParam {

    /**
     * 消息发送类型 目前支持text、actionCard、markdown
     */
    private String type;

    /**
     * 信息超链接
     * 一般给机器人用，可以点进去看详细内容
     */
    private String messageUrl;

    /**
     * 信息缩略图片地址
     * 一般给机器人用
     */
    private String picUrl;
}
