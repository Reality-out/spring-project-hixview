package site.hixview.domain.validator.member;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.service.MemberService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.MemberTestUtils;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.user.RequestPath.MEMBERSHIP_PATH;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@OnlyRealControllerContext
class MemberBindingErrorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("NotBlank에 대한 회원 가입 유효성 검증")
    @Test
    void validateNotBlankMembership() throws Exception {
        // given & when
        ObjectMapper objectMapper = new ObjectMapper();
        MembershipDto membershipDtoSpace = createTestMembershipDto();
        membershipDtoSpace.setId(" ");
        membershipDtoSpace.setPassword(" ");
        membershipDtoSpace.setName(" ");
        membershipDtoSpace.setEmail(" ");
        MembershipDto membershipDtoNull = new MembershipDto();

        // then
        for (MembershipDto memberDto : List.of(membershipDtoSpace, membershipDtoNull)) {
            Map<String, Object> resultMap = objectMapper.readValue(requireNonNull(mockMvc.perform(postWithMembershipDto(MEMBERSHIP_PATH, memberDto))
                    .andExpectAll(status().isBadRequest()
                    )).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
            });
            @SuppressWarnings("unchecked")
            Map<String, String> fieldErrorMap = (Map<String, String>) resultMap.get(FIELD_ERROR_MAP);
            assertThat(fieldErrorMap.get(ID)).isEqualTo(encodeWithUTF8("ID가 비어 있습니다."));
            assertThat(fieldErrorMap.get(PASSWORD)).isEqualTo(encodeWithUTF8("비밀번호가 비어 있습니다."));
            assertThat(fieldErrorMap.get(NAME)).isEqualTo(encodeWithUTF8("이름이 비어 있습니다."));
            assertThat(fieldErrorMap.get(EMAIL)).isEqualTo(encodeWithUTF8("이메일 값이 비어 있습니다."));
        }
    }

    @DisplayName("Pattern에 대한 회원 가입 유효성 검증")
    @Test
    void validatePatternMembership() throws Exception {
        // given & when
        ObjectMapper objectMapper = new ObjectMapper();
        MembershipDto membershipDto = createTestMembershipDto();
        membershipDto.setId(getRandomLongString(8));
        membershipDto.setPassword(getRandomLongString(8));
        membershipDto.setName(getRandomLongString(8));
        membershipDto.setEmail(getRandomLongString(8));

        // then
        Map<String, Object> resultMap = objectMapper.readValue(requireNonNull(mockMvc.perform(postWithMembershipDto(MEMBERSHIP_PATH, membershipDto))
                .andExpectAll(status().isBadRequest()
                )).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) resultMap.get(FIELD_ERROR_MAP);
        assertThat(fieldErrorMap.get(ID)).isEqualTo(encodeWithUTF8("ID 형식이 올바르지 않습니다."));
        assertThat(fieldErrorMap.get(PASSWORD)).isEqualTo(encodeWithUTF8("비밀번호 형식이 올바르지 않습니다."));
        assertThat(fieldErrorMap.get(NAME)).isEqualTo(encodeWithUTF8("이름 형식이 올바르지 않습니다."));
        assertThat(fieldErrorMap.get(EMAIL)).isEqualTo(encodeWithUTF8("이메일 형식이 올바르지 않습니다."));
    }
}
