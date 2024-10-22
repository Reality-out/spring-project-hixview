package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.manager.ViewName.MANAGER_HOME_VIEW;

@OnlyRealControllerContext
class ManagerMainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("관리자 메인 페이지 접속")
    @Test
    void accessManagerMainPage() throws Exception {
        mockMvc.perform(get("/manager"))
                .andExpectAll(status().isOk(), view().name(MANAGER_HOME_VIEW));
    }
}