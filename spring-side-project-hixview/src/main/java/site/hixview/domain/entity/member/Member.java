package site.hixview.domain.entity.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.vo.Regex;

import java.time.LocalDate;
import java.util.HashMap;

import static site.hixview.domain.vo.name.EntityName.Member.ID;
import static site.hixview.domain.vo.Regex.NAME_REGEX;
import static site.hixview.domain.vo.Regex.PW_REGEX;
import static site.hixview.domain.vo.Word.NAME;

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

    @NotNull
    @PastOrPresent
    private final LocalDate birth;

    @NotNull
    private final PhoneNumber phoneNumber;

    public MemberDto toDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(id);
        memberDto.setPassword(password);
        memberDto.setName(name);
        memberDto.setYear(birth.getYear());
        memberDto.setMonth(birth.getMonthValue());
        memberDto.setDays(birth.getDayOfMonth());
        memberDto.setPhoneNumber(phoneNumber.toStringWithDash());
        return memberDto;
    }

    public HashMap<String, Object> toMapWithNoIdentifier() {
        return new HashMap<>() {{
            put(ID, id);
            put("password", password);
            put(NAME, name);
            put("birth", birth);
            put("phoneNumber", phoneNumber.toStringWithDash());
        }};
    }

    public static class MemberBuilder {
        public MemberBuilder() {}

        public MemberBuilder phoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public MemberBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = PhoneNumber.builder().phoneNumber(phoneNumber).build();
            return this;
        }

        public MemberBuilder member(Member member) {
            identifier = member.getIdentifier();
            id = member.getId();
            password = member.getPassword();
            name = member.getName();
            birth = member.getBirth();
            phoneNumber = member.getPhoneNumber();
            return this;
        }

        public MemberBuilder memberDto(MemberDto memberDto) {
            id = memberDto.getId();
            password = memberDto.getPassword();
            name = memberDto.getName();
            birth = LocalDate.of(memberDto.getYear(), memberDto.getMonth(), memberDto.getDays());
            phoneNumber = PhoneNumber.builder().phoneNumber(memberDto.getPhoneNumber()).build();
            return this;
        }
    }
}