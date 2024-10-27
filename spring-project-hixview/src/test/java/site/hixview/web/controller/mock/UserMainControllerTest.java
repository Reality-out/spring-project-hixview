package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.service.MemberService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.MemberTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.user.RequestPath.*;
import static site.hixview.domain.vo.user.ViewName.LOGIN_VIEW;

@OnlyRealControllerContext
class UserMainControllerTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("로그인 페이지 접속")
    @Test
    void accessLogin() throws Exception {
        mockMvc.perform(get(LOGIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(LOGIN_VIEW + VIEW_SHOW),
                        model().attribute("membership", MEMBERSHIP_PATH),
                        model().attribute("findId", FIND_ID_PATH));
    }
}