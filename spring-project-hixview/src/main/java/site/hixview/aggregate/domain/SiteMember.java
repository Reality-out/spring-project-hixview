package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class SiteMember {
    private final Long number;
    private final String id;
    private final String pw;
    private final String name;
    private final String email;

    public static class SiteMemberBuilder {
        private Long number;
        private String id;
        private String pw;
        private String name;
        private String email;

        public SiteMemberBuilder siteMember(final SiteMember siteMember) {
            this.number = siteMember.getNumber();
            this.id = siteMember.getId();
            this.pw = siteMember.getPw();
            this.name = siteMember.getName();
            this.email = siteMember.getEmail();
            return this;
        }

        public SiteMember build() {
            return new SiteMember(this.number, this.id, this.pw, this.name, this.email);
        }
    }
}