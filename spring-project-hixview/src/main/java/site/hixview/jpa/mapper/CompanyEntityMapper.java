package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Company;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.mapper.support.CompanyEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyEntityMapper extends CompanyEntityMapperSupport {
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    @Mapping(target = COMPANY, ignore = true)
    CompanyEntity toCompanyEntity(Company company);

    @Mapping(source = FIRST_CATEGORY, target = FIRST_CATEGORY_NUMBER, qualifiedByName = "firstCategoryNumberToDomain")
    @Mapping(source = SECOND_CATEGORY, target = SECOND_CATEGORY_NUMBER, qualifiedByName = "secondCategoryNumberToDomain")
    Company toCompany(CompanyEntity companyEntity);
}
