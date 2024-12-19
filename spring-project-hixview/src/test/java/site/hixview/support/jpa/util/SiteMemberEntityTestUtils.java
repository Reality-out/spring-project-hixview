package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.support.spring.util.SiteMemberTestUtils;

public interface SiteMemberEntityTestUtils extends SiteMemberTestUtils {
    /**
     * Create
     */
    default SiteMemberEntity createSiteMemberEntity() {
        return new SiteMemberEntity(siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
    }

    default SiteMemberEntity createAnotherSiteMemberEntity() {
        return new SiteMemberEntity(anotherSiteMember.getId(), anotherSiteMember.getPw(), anotherSiteMember.getName(), anotherSiteMember.getEmail());
    }

    default SiteMemberEntity createNumberedSiteMemberEntity() {
        return new SiteMemberEntity(siteMember.getNumber(), siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
    }
}