package cn.ether.im.message.controller.advice;


import cn.ether.im.message.model.vo.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * * @Author: Martin
 * * @Date    2024/8/13 23:39
 * * @Description
 **/
@Slf4j
@Order(10)
@RestControllerAdvice
public class GlobalControllerAdvice {


    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Resp handleException(Exception e, HttpServletRequest request) {
        logErrorCause(e, request);
        return Resp.fail(e.getMessage());
    }

    private void logErrorCause(Exception e, HttpServletRequest request) {
        // 打印堆栈，以供调试
        log.warn("-----全局异常处理---------------");
        e.printStackTrace();
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常:{}", requestURI, e.getMessage());
    }


}
