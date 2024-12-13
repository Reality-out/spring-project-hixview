package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.SiteMember;

public interface SiteMemberTestUtils {
    SiteMember siteMember = SiteMember.builder()
            .number(1L)
            .id("arman1435")
            .pw("crumble!646!")
            .name("박강호")
            .email("crazzz1@naver.com").build();

    SiteMember anotherSiteMember = SiteMember.builder()
            .number(2L)
            .id("skipp0230")
            .pw("croskan@#12")
            .name("김건수")
            .email("akgkgh2@naver.com").build();
}
