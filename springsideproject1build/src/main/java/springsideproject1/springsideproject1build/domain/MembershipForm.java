package springsideproject1.springsideproject1build.domain;

import java.util.concurrent.atomic.AtomicReference;

public class MembershipForm {
    private AtomicReference<String> id;
    private AtomicReference<String> password;
    private AtomicReference<String> name;

    public String getId() {
        return id.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getName() {
        return name.get();
    }

    private MembershipForm(String id, String password, String name) {
        this.id = new AtomicReference<>(id);
        this.password = new AtomicReference<>(password);
        this.name = new AtomicReference<>(name);
    }

    public static class MembershipFormBuilder {
        private String id;
        private String password;
        private String name;

        public MembershipFormBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MembershipFormBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MembershipFormBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MembershipForm build() {
            return new MembershipForm(id, password, name);
        }
    }
}