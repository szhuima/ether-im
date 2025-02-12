package cn.ether.im.message.single.controller;

import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.model.vo.Resp;
import cn.ether.im.message.single.service.ImMessageService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 12:29
 * * @Description
 **/
@Tag(
        name = "MessageController",
        description = "对话消息接口",
        externalDocs = @ExternalDocumentation(
                description = "对话消息接口文档",
                url = "https://www.google.com"))
@RequestMapping("/message")
@RestController
public class MessageController {

    @Resource
    private ImMessageService singleMessageService;


    /**
     * 发送消息 通过MQ的事物消息实现，有可能会回滚也有可能提交
     *
     * @param req
     * @return
     */
    @Operation(summary = "发送消息", description = "")
    @PostMapping("/send")
    public Resp send(@RequestBody MessageSendReq req) throws Exception {
        ImSingleMessage singleMessage = singleMessageService.convertSendReqToCoreModel(req);
        singleMessageService.sendSingleMessage(singleMessage);
        return Resp.success();
    }

}
