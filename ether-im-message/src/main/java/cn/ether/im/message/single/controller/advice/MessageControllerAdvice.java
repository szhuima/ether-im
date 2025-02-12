package cn.ether.im.message.single.controller.advice;


import cn.ether.im.common.exception.ImException;
import cn.ether.im.message.single.model.vo.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * * @Author: Martin
 * * @Date    2024/8/13 23:39
 * * @Description
 **/
@Slf4j
@Order(1)
@RestControllerAdvice
public class MessageControllerAdvice {


    @ExceptionHandler(ImException.class)
    public Resp handleException(ImException e) {
        log.error("ImException caught: {}", e.getMessage());
        return Resp.fail(e);
    }
}
