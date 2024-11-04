package site.hixview.domain.error.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.MemberTestUtils;

import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_LOGGED_IN_ERROR;
import static site.hixview.domain.vo.user.RequestPath.LOGOUT_PATH;

@OnlyRealControllerContext
public class UserMemberErrorHandleTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("로그아웃되어 있을 때 시도하는 로그아웃")
    @Test
    void logoutWhenLoggedOut() throws Exception {
        // given & when
        MockHttpSession mockHttpSession = spy(new MockHttpSession());

        // then
        mockMvc.perform(postWithNoParam(LOGOUT_PATH).session(mockHttpSession))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(ERROR).value(NOT_LOGGED_IN_ERROR));
    }
}
