package springsideproject1.springsideproject1build.domain;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Company {
    private AtomicReference<String> code;
    private AtomicReference<String> country;
    private AtomicReference<String> scale;
    private AtomicReference<String> name;
    private AtomicReference<String> category1st;
    private AtomicReference<String> category2nd;

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code = new AtomicReference<String>(code);
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country = new AtomicReference<>(country);
    }

    public String getScale() {
        return scale.get();
    }

    public void setScale(String scale) {
        this.scale = new AtomicReference<>(scale);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new AtomicReference<>(name);
    }

    public String getCategory1st() {
        return category1st.get();
    }

    public void setCategory1st(String category1st) {
        this.category1st = new AtomicReference<>(category1st);
    }

    public String getCategory2nd() {
        return category2nd.get();
    }

    public void setCategory2nd(String category2nd) {
        this.category2nd = new AtomicReference<>(category2nd);
    }

}
