package springsideproject1.springsideproject1production.domain;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Member {
    private AtomicLong identifier;
    private AtomicReference<String> id;
    private AtomicReference<String> password;
    private AtomicReference<String> name;

    public Long getIdentifier() {
        return identifier.get();
    }

    public void setIdentifier(Long identifier) {
        this.identifier = new AtomicLong(identifier);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id = new AtomicReference<>(id);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password = new AtomicReference<>(password);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new AtomicReference<>(name);
    }
}