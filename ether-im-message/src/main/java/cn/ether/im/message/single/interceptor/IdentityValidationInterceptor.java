package cn.ether.im.message.single.interceptor;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.message.single.model.session.SessionContext;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 身份校验拦截器
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/23 00:24
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
public class IdentityValidationInterceptor implements HandlerInterceptor {

    private String secret = ImConstants.TOKEN_SECRET;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token) || !JwtUtils.checkSign(token.replace("Bearer ", ""), secret)) {
            response.setStatus(401);
            return false;
        }
        try {
            String info = JwtUtils.getInfo(token.replace("Bearer ", ""));
            ImUserTerminal userTerminal = JSON.parseObject(info, ImUserTerminal.class);
            SessionContext.setSession(userTerminal);
        } catch (Exception e) {
            log.error("身份校验失败|{}", e.getMessage());
            response.setStatus(401);
            return false;
        }
        return true;

    }
}
