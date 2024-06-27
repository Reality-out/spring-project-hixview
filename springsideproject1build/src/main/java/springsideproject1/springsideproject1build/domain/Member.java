package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private AtomicLong identifier;
    private AtomicReference<String> id;
    private AtomicReference<String> password;
    private AtomicReference<String> name;

    private Member(Long identifier, String id, String password, String name) {
        this.identifier = new AtomicLong(identifier);
        this.id = new AtomicReference<>(id);
        this.password = new AtomicReference<>(password);
        this.name = new AtomicReference<>(name);
    }

    public Long getIdentifier() {
        return identifier.get();
    }

    public String getId() {
        return id.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getName() {
        return name.get();
    }

    public static class MemberBuilder {
        private Long identifier = -1L;
        private String id;
        private String password;
        private String name;

        public MemberBuilder member(Member member) {
            identifier = member.getIdentifier();
            id = member.getId();
            password = member.getPassword();
            name = member.getName();
            return this;
        }

        public MemberBuilder identifier(Long identifier) {
            this.identifier = identifier;
            return this;
        }

        public MemberBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Member build() {
            return new Member(identifier, id, password, name);
        }
    }
}
