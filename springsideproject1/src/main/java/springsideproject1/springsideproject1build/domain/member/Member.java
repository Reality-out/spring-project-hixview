package springsideproject1.springsideproject1build.domain.member;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashMap;

import static springsideproject1.springsideproject1build.utility.ConstantUtils.ID;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private final Long identifier;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$")
    private final String id;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,64}$")
    private final String password;

    @NotBlank
    private final String name;

    @NotBlank
    @PastOrPresent
    private final LocalDate birth;

    @NotBlank
    @Pattern(regexp = "^01([0|1|6|7|8|9])-([0-9]{4})-([0-9]{4})+$")
    private final PhoneNumber phoneNumber;

    public MemberDto toMemberDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setIdentifier(identifier);
        memberDto.setId(id);
        memberDto.setPassword(password);
        memberDto.setName(name);
        memberDto.setYear(String.valueOf(birth.getYear()));
        memberDto.setMonth(String.valueOf(birth.getMonthValue()));
        memberDto.setDate(String.valueOf(birth.getDayOfMonth()));
        memberDto.setPhoneNumber(phoneNumber.toString());
        return memberDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put("identifier", identifier);
            putAll(toMapWithNoIdentifier());
        }};
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
            identifier = memberDto.getIdentifier();
            id = memberDto.getId();
            password = memberDto.getPassword();
            name = memberDto.getName();
            birth = LocalDate.of(Integer.parseInt(memberDto.getYear()),
                    Integer.parseInt(memberDto.getMonth()), Integer.parseInt(memberDto.getDate()));
            phoneNumber = PhoneNumber.builder().phoneNumber(memberDto.getPhoneNumber()).build();
            return this;
        }
    }
}