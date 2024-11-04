package site.hixview.util;

import jakarta.servlet.http.HttpSession;

import static site.hixview.domain.vo.Word.LOGIN_INFO;

public abstract class SessionUtils {

    public static boolean hasLoginInfo(HttpSession session) {
        return session != null && session.getAttribute(LOGIN_INFO) != null;
    }
}
