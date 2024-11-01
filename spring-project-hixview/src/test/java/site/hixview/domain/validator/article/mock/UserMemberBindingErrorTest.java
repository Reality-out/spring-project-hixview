package site.hixview.domain.validator.article.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.member.dto.LoginDto;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.MemberTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.LOGIN_PATH;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@OnlyRealControllerContext
public class UserMemberBindingErrorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("ID의 NotBlank(공백)에 대한 로그인 유효성 검증")
    @Test
    void validateIDNotBlankSpaceLogin() throws Exception {
        // given & when
        LoginDto loginDto = new LoginDto();
        loginDto.setId("");
        loginDto.setPassword("");

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(LAYOUT_PATH).value(BASIC_LAYOUT))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8(fieldErrorMap.get(ID))).isEqualTo("ID가 비어 있습니다.");
    }

    @DisplayName("ID의 NotBlank(null)에 대한 로그인 유효성 검증")
    @Test
    void validateIDNotBlankNullLogin() throws Exception {
        // given & when
        LoginDto loginDto = new LoginDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(LAYOUT_PATH).value(BASIC_LAYOUT))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8(fieldErrorMap.get(ID))).isEqualTo("ID가 비어 있습니다.");
    }

    @DisplayName("비밀번호의 NotBlank(공백)에 대한 로그인 유효성 검증")
    @Test
    void validatePasswordNotBlankSpaceLogin() throws Exception {
        // given & when
        LoginDto loginDto = new LoginDto();
        loginDto.setId(testMember.getId());
        loginDto.setPassword("");

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(LAYOUT_PATH).value(BASIC_LAYOUT))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8(fieldErrorMap.get(PASSWORD))).isEqualTo("비밀번호가 비어 있습니다.");
    }

    @DisplayName("비밀번호의 NotBlank(null)에 대한 로그인 유효성 검증")
    @Test
    void validatePasswordNotBlankNullLogin() throws Exception {
        // given & when
        LoginDto loginDto = new LoginDto();
        loginDto.setId(testMember.getId());

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(LAYOUT_PATH).value(BASIC_LAYOUT))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8(fieldErrorMap.get(PASSWORD))).isEqualTo("비밀번호가 비어 있습니다.");
    }
}
