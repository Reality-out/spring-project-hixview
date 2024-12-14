package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.dto.CompanyDto;

import static site.hixview.aggregate.vo.WordCamel.COMPANY;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyMapper {
    @Mapping(target = COMPANY, ignore = true)
    Company toCompany(CompanyDto companyDto);

    CompanyDto toCompanyDto(Company company);
}
