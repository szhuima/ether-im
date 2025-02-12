package cn.ether.im.message.single.interceptor;

import cn.ether.im.common.util.XssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/24 14:51
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
public class SecurityContentInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查参数
        Map<String, String[]> paramMap = request.getParameterMap();
        for (String[] values : paramMap.values()) {
            for (String value : values) {
                if (XssUtils.checkXss(value)) {
                    response.setStatus(403);
                    return false;
                }
            }
        }
        return true;
    }

    private String getBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("获取请求体异常:{}", e.getMessage());
        }
        return sb.toString();
    }
}
