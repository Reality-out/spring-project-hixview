package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.aggregate.mapper.support.CompanyMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.COUNTRY_LISTED;
import static site.hixview.aggregate.vo.WordCamel.SCALE;

@Mapper
public interface CompanyMapper extends CompanyMapperSupport {
    @Mapping(source = COUNTRY_LISTED, target = COUNTRY_LISTED, qualifiedByName = "countryListedToDomain")
    @Mapping(source = SCALE, target = SCALE, qualifiedByName = "scaleToDomain")
    Company toCompany(CompanyDto companyDto);

    @Mapping(source = COUNTRY_LISTED, target = COUNTRY_LISTED, qualifiedByName = "countryListedToDto")
    @Mapping(source = SCALE, target = SCALE, qualifiedByName = "scaleToDto")
    CompanyDto toCompanyDto(Company company);
}
