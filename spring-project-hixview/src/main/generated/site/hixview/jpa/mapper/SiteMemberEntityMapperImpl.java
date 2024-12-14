package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T11:41:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class SiteMemberEntityMapperImpl implements SiteMemberEntityMapper {

    @Override
    public SiteMemberEntity toSiteMemberEntity(SiteMember siteMember) {
        if ( siteMember == null ) {
            return null;
        }

        SiteMemberEntity siteMemberEntity = new SiteMemberEntity();

        return siteMemberEntity;
    }

    @Override
    public SiteMember toSiteMember(SiteMemberEntity siteMemberEntity) {
        if ( siteMemberEntity == null ) {
            return null;
        }

        SiteMember.SiteMemberBuilder siteMember = SiteMember.builder();

        siteMember.number( siteMemberEntity.getNumber() );
        siteMember.id( siteMemberEntity.getId() );
        siteMember.pw( siteMemberEntity.getPw() );
        siteMember.name( siteMemberEntity.getName() );
        siteMember.email( siteMemberEntity.getEmail() );

        return siteMember.build();
    }
}
