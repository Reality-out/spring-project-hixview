package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:02:20+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class SiteMemberMapperImpl implements SiteMemberMapper {

    @Override
    public SiteMember toSiteMember(SiteMemberDto siteMemberDto) {
        if ( siteMemberDto == null ) {
            return null;
        }

        SiteMember.SiteMemberBuilder siteMember = SiteMember.builder();

        siteMember.number( siteMemberDto.getNumber() );
        siteMember.id( siteMemberDto.getId() );
        siteMember.pw( siteMemberDto.getPw() );
        siteMember.name( siteMemberDto.getName() );
        siteMember.email( siteMemberDto.getEmail() );

        return siteMember.build();
    }

    @Override
    public SiteMemberDto toSiteMemberDto(SiteMember siteMember) {
        if ( siteMember == null ) {
            return null;
        }

        SiteMemberDto siteMemberDto = new SiteMemberDto();

        siteMemberDto.setNumber( siteMember.getNumber() );
        siteMemberDto.setId( siteMember.getId() );
        siteMemberDto.setPw( siteMember.getPw() );
        siteMemberDto.setName( siteMember.getName() );
        siteMemberDto.setEmail( siteMember.getEmail() );

        return siteMemberDto;
    }

    @Override
    public SiteMember toSiteMember(SiteMemberDtoNoNumber siteMemberDto) {
        if ( siteMemberDto == null ) {
            return null;
        }

        SiteMember.SiteMemberBuilder siteMember = SiteMember.builder();

        siteMember.id( siteMemberDto.getId() );
        siteMember.pw( siteMemberDto.getPw() );
        siteMember.name( siteMemberDto.getName() );
        siteMember.email( siteMemberDto.getEmail() );

        return siteMember.build();
    }

    @Override
    public SiteMemberDtoNoNumber toSiteMemberDtoNoNumber(SiteMember siteMember) {
        if ( siteMember == null ) {
            return null;
        }

        SiteMemberDtoNoNumber siteMemberDtoNoNumber = new SiteMemberDtoNoNumber();

        siteMemberDtoNoNumber.setId( siteMember.getId() );
        siteMemberDtoNoNumber.setPw( siteMember.getPw() );
        siteMemberDtoNoNumber.setName( siteMember.getName() );
        siteMemberDtoNoNumber.setEmail( siteMember.getEmail() );

        return siteMemberDtoNoNumber;
    }
}
