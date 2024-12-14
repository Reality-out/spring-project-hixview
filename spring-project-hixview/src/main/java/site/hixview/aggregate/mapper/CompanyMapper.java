package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.aggregate.mapper.support.CompanyMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyMapper extends CompanyMapperSupport {
    @Mapping(source = COUNTRY_LISTED, target = COUNTRY_LISTED, qualifiedByName = "countryListedToDomain")
    @Mapping(source = SCALE, target = SCALE, qualifiedByName = "scaleToDomain")
    @Mapping(target = COMPANY, ignore = true)
    Company toCompany(CompanyDto companyDto);

    @Mapping(source = COUNTRY_LISTED, target = COUNTRY_LISTED, qualifiedByName = "countryListedToDto")
    @Mapping(source = SCALE, target = SCALE, qualifiedByName = "scaleToDto")
    CompanyDto toCompanyDto(Company company);
}
