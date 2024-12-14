package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;
import static site.hixview.aggregate.vo.WordCamel.SITE_MEMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SiteMemberMapper {
    @Mapping(target = SITE_MEMBER, ignore = true)
    SiteMember toSiteMember(SiteMemberDto siteMemberDto);

    SiteMemberDto toSiteMemberDto(SiteMember siteMember);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(target = SITE_MEMBER, ignore = true)
    SiteMember toSiteMember(SiteMemberDtoNoNumber siteMemberDto);

    SiteMemberDtoNoNumber toSiteMemberDtoNoNumber(SiteMember siteMember);
}
