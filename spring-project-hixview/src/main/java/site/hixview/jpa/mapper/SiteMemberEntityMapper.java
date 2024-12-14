package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SiteMemberEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    SiteMemberEntity toSiteMemberEntity(SiteMember siteMember);

    SiteMember toSiteMember(SiteMemberEntity siteMemberEntity);
}
