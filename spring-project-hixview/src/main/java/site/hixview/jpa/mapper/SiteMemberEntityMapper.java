package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.jpa.mapper.support.SiteMemberEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.SITE_MEMBER;

@Mapper
public interface SiteMemberEntityMapper extends SiteMemberEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    SiteMemberEntity toSiteMemberEntity(SiteMember siteMember);

    @Mapping(target = SITE_MEMBER, ignore = true)
    SiteMember toSiteMember(SiteMemberEntity siteMemberEntity);
}
