package site.hixview.domain.entity.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.entity.member.dto.MemberDto;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.vo.Regex;

import java.util.HashMap;

import static site.hixview.domain.vo.Regex.*;
import static site.hixview.domain.vo.Word.*;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private final Long identifier;

    @NotBlank
    @Pattern(regexp = Regex.ID_REGEX)
    private final String id;

    @NotBlank
    @Pattern(regexp = PW_REGEX)
    private final String password;

    @NotBlank
    @Pattern(regexp = NAME_REGEX)
    private final String name;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX)
    private final String email;

    public MemberDto toDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(id);
        memberDto.setPassword(password);
        memberDto.setName(name);
        memberDto.setEmail(email);
        return memberDto;
    }

    public HashMap<String, Object> toMapWithNoIdentifier() {
        return new HashMap<>() {{
            put(ID, id);
            put(PASSWORD, password);
            put(NAME, name);
            put(EMAIL, email);
        }};
    }

    public static class MemberBuilder {
        public MemberBuilder() {}

        public MemberBuilder member(Member member) {
            identifier = member.getIdentifier();
            id = member.getId();
            password = member.getPassword();
            name = member.getName();
            email = member.getEmail();
            return this;
        }

        public MemberBuilder memberDto(MemberDto memberDto) {
            id = memberDto.getId();
            password = memberDto.getPassword();
            name = memberDto.getName();
            email = memberDto.getEmail();
            return this;
        }

        public MemberBuilder membershipDto(MembershipDto membershipDto) {
            id = membershipDto.getId();
            password = membershipDto.getPassword();
            name = membershipDto.getName();
            email = membershipDto.getEmail();
            return this;
        }
    }
}