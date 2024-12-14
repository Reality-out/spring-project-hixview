package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;
import site.hixview.support.spring.util.SiteMemberTestUtils;

public interface SiteMemberDtoTestUtils extends SiteMemberTestUtils {
    /**
     * Create
     */
    default SiteMemberDto createSiteMemberDto() {
        SiteMemberDto siteMemberDto = new SiteMemberDto();
        siteMemberDto.setNumber(siteMember.getNumber());
        siteMemberDto.setId(siteMember.getId());
        siteMemberDto.setPw(siteMember.getPw());
        siteMemberDto.setName(siteMember.getName());
        siteMemberDto.setEmail(siteMember.getEmail());
        return siteMemberDto;
    }

    default SiteMemberDto createAnotherSiteMemberDto() {
        SiteMemberDto siteMemberDto = new SiteMemberDto();
        siteMemberDto.setNumber(anotherSiteMember.getNumber());
        siteMemberDto.setId(anotherSiteMember.getId());
        siteMemberDto.setPw(anotherSiteMember.getPw());
        siteMemberDto.setName(anotherSiteMember.getName());
        siteMemberDto.setEmail(anotherSiteMember.getEmail());
        return siteMemberDto;
    }

    default SiteMemberDtoNoNumber createSiteMemberDtoNoNumber() {
        SiteMemberDtoNoNumber siteMemberDto = new SiteMemberDtoNoNumber();
        siteMemberDto.setId(siteMember.getId());
        siteMemberDto.setPw(siteMember.getPw());
        siteMemberDto.setName(siteMember.getName());
        siteMemberDto.setEmail(siteMember.getEmail());
        return siteMemberDto;
    }

    default SiteMemberDtoNoNumber createAnotherSiteMemberDtoNoNumber() {
        SiteMemberDtoNoNumber siteMemberDto = new SiteMemberDtoNoNumber();
        siteMemberDto.setId(anotherSiteMember.getId());
        siteMemberDto.setPw(anotherSiteMember.getPw());
        siteMemberDto.setName(anotherSiteMember.getName());
        siteMemberDto.setEmail(anotherSiteMember.getEmail());
        return siteMemberDto;
    }
}
