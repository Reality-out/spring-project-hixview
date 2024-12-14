package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

import static site.hixview.aggregate.vo.WordCamel.SITE_MEMBER;
import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SiteMemberEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    SiteMemberEntity toSiteMemberEntity(SiteMember siteMember);

    @Mapping(target = SITE_MEMBER, ignore = true)
    SiteMember toSiteMember(SiteMemberEntity siteMemberEntity);
}
