package cn.ether.im.message.single.model.session;

import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.message.single.model.constants.ImMessageConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SessionContext {

    public static void setSession(ImUserTerminal userTerminal) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute(ImMessageConstants.SESSION_KEY, userTerminal);
    }

    public static ImUserTerminal loggedUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object attribute = request.getSession().getAttribute(ImMessageConstants.SESSION_KEY);
        if (attribute == null) {
            throw new ImException(ImExceptionCode.UN_LOGGED);
        }
        return (ImUserTerminal) attribute;
    }
}