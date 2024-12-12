package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class SiteMemberEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract SiteMemberEntity toSiteMemberEntity(SiteMember siteMember);

    public abstract SiteMember toSiteMember(SiteMemberEntity siteMemberEntity);
}
