package site.hixview.jpa.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Company;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.mapper.support.CompanyEntityMapperSupport;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyEntityMapper extends CompanyEntityMapperSupport {
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    @Mapping(target = COMPANY, ignore = true)
    CompanyEntity toCompanyEntity(Company company,
                                  @Context FirstCategoryEntityRepository firstCategoryRepository,
                                  @Context SecondCategoryEntityRepository secondCategoryRepository);

    @Mapping(source = FIRST_CATEGORY, target = FIRST_CATEGORY_NUMBER, qualifiedByName = "firstCategoryNumberToDomain")
    @Mapping(source = SECOND_CATEGORY, target = SECOND_CATEGORY_NUMBER, qualifiedByName = "secondCategoryNumberToDomain")
    Company toCompany(CompanyEntity companyEntity);
}
