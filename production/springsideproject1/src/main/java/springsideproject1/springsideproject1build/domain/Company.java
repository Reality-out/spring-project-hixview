package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Company {
    private AtomicReference<String> code;
    private AtomicReference<String> country;
    private AtomicReference<String> scale;
    private AtomicReference<String> name;
    private AtomicReference<String> category1st;
    private AtomicReference<String> category2nd;

    private Company(String code, String country, String scale, String name, String category1st, String category2nd) {
        this.code = new AtomicReference<>(code);
        this.country = new AtomicReference<>(country);
        this.scale = new AtomicReference<>(scale);
        this.name = new AtomicReference<>(name);
        this.category1st = new AtomicReference<>(category1st);
        this.category2nd = new AtomicReference<>(category2nd);
    }

    public String getCode() {
        return code.get();
    }

    public String getCountry() {
        return country.get();
    }

    public String getScale() {
        return scale.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCategory1st() {
        return category1st.get();
    }

    public String getCategory2nd() {
        return category2nd.get();
    }

    public static class CompanyBuilder {
        private String code;
        private String country;
        private String scale;
        private String name;
        private String category1st;
        private String category2nd;

        public CompanyBuilder company(Company company) {
            code = company.getCode();
            country = company.getCountry();
            scale = company.getScale();
            name = company.getName();
            category1st = company.getCategory1st();
            category2nd = company.getCategory2nd();
            return this;
        }

        public CompanyBuilder code(String code) {
            this.code = code;
            return this;
        }

        public CompanyBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CompanyBuilder scale(String scale) {
            this.scale = scale;
            return this;
        }

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyBuilder category1st(String category1st) {
            this.category1st = category1st;
            return this;
        }

        public CompanyBuilder category2nd(String category2nd) {
            this.category2nd = category2nd;
            return this;
        }


        public Company build() {
            return new Company(code, country, scale, name, category1st, category2nd);
        }
    }
}
