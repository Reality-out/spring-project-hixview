package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

public interface SiteMemberEntityMapperSupport {
    @AfterMapping
    default SiteMemberEntity afterMappingToEntity(
            @MappingTarget SiteMemberEntity ignoredEntity, SiteMember siteMember) {
        return new SiteMemberEntity(siteMember.getNumber(),
                siteMember.getId(),
                siteMember.getPw(),
                siteMember.getName(),
                siteMember.getEmail());
    }
}
