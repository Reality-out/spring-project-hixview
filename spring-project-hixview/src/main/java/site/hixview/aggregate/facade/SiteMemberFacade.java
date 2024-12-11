package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.domain.SiteMember.SiteMemberBuilder;
import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;

public class SiteMemberFacade {
    public SiteMemberBuilder createBuilder(SiteMember siteMember) {
        return SiteMember.builder()
                .number(siteMember.getNumber())
                .id(siteMember.getId())
                .pw(siteMember.getPw())
                .name(siteMember.getName())
                .email(siteMember.getEmail());
    }

    public SiteMemberBuilder createBuilder(SiteMemberDto siteMemberDto) {
        return SiteMember.builder()
                .number(siteMemberDto.getNumber())
                .id(siteMemberDto.getId())
                .pw(siteMemberDto.getPw())
                .name(siteMemberDto.getName())
                .email(siteMemberDto.getEmail());
    }

    public SiteMemberBuilder createBuilder(SiteMemberDtoNoNumber siteMemberDto) {
        return SiteMember.builder()
                .id(siteMemberDto.getId())
                .pw(siteMemberDto.getPw())
                .name(siteMemberDto.getName())
                .email(siteMemberDto.getEmail());
    }
}
