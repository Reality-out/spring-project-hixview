package springsideproject1.springsideproject1build.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private final Long identifier;
    private final String id;
    private final String password;
    private final String name;
    private final LocalDate birth;
    private final PhoneNumber phoneNumber;

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put("identifier", identifier);
            putAll(toMapWithNoIdentifier());
        }};
    }

    public HashMap<String, Object> toMapWithNoIdentifier() {
        return new HashMap<>() {{
            put("id", id);
            put("password", password);
            put("name", name);
            put("birth", birth);
            put("phoneNumber", phoneNumber);
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
    }
}