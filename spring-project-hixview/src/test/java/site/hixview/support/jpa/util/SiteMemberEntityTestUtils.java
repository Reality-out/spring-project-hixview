package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.SiteMemberEntity;

public interface SiteMemberEntityTestUtils {
    /**
     * Create
     */
    default SiteMemberEntity createSiteMember() {
        return new SiteMemberEntity("arman1435", "crumble!646!", "박강호", "crazzz1@naver.com");
    }

    default SiteMemberEntity createAnotherSiteMember() {
        return new SiteMemberEntity("skipp0230", "croskan@#12", "김건수", "akgkgh2@naver.com");
    }
}