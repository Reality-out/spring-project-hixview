package springsideproject1.springsideproject1build.domain;

import lombok.*;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private final Long identifier;
    private final String id;
    private final String password;
    private final String name;

    public static class MemberBuilder {
        public MemberBuilder() {
        }

        public MemberBuilder member(Member member) {
            identifier = member.getIdentifier();
            id = member.getId();
            password = member.getPassword();
            name = member.getName();
            return this;
        }
    }
}