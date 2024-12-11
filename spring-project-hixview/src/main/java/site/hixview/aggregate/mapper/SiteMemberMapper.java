package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper
public interface SiteMemberMapper {
    SiteMember toSiteMember(SiteMemberDto siteMemberDto);

    SiteMemberDto toSiteMemberDto(SiteMember siteMember);

    @Mapping(target = NUMBER, ignore = true)
    SiteMember toSiteMember(SiteMemberDtoNoNumber siteMemberDto);

    SiteMemberDtoNoNumber toSiteMemberDtoNoNumber(SiteMember siteMember);
}
