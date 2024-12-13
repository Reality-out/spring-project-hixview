package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;

public interface SiteMemberDtoTestUtils {
    default SiteMemberDto createSiteMemberDto() {
        SiteMemberDto siteMemberDto = new SiteMemberDto();
        siteMemberDto.setNumber(1L);
        siteMemberDto.setId("arman1435");
        siteMemberDto.setPw("crumble!646!");
        siteMemberDto.setName("박강호");
        siteMemberDto.setEmail("crazzz1@naver.com");
        return siteMemberDto;
    }

    default SiteMemberDto createAnotherSiteMemberDto() {
        SiteMemberDto siteMemberDto = new SiteMemberDto();
        siteMemberDto.setNumber(2L);
        siteMemberDto.setId("skipp0230");
        siteMemberDto.setPw("croskan@#12");
        siteMemberDto.setName("김건수");
        siteMemberDto.setEmail("akgkgh2@naver.com");
        return siteMemberDto;
    }

    default SiteMemberDtoNoNumber createSiteMemberDtoNoNumber() {
        SiteMemberDtoNoNumber siteMemberDto = new SiteMemberDtoNoNumber();
        siteMemberDto.setId("arman1435");
        siteMemberDto.setPw("crumble!646!");
        siteMemberDto.setName("박강호");
        siteMemberDto.setEmail("crazzz1@naver.com");
        return siteMemberDto;
    }

    default SiteMemberDtoNoNumber createAnotherSiteMemberDtoNoNumber() {
        SiteMemberDtoNoNumber siteMemberDto = new SiteMemberDtoNoNumber();
        siteMemberDto.setId("skipp0230");
        siteMemberDto.setPw("croskan@#12");
        siteMemberDto.setName("김건수");
        siteMemberDto.setEmail("akgkgh2@naver.com");
        return siteMemberDto;
    }
}
