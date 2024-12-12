package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.mapper.support.CompanyArticleEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class CompanyArticleEntityMapper extends CompanyArticleEntityMapperSupport {
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    @Mapping(target = COMPANY_ARTICLE, ignore = true)
    public abstract CompanyArticleEntity toCompanyArticleEntity(CompanyArticle companyArticle);

    @Mapping(source = ARTICLE, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(source = PRESS, target = PRESS_NUMBER, qualifiedByName = "pressNumberToDomain")
    @Mapping(target = MAPPED_COMPANY_CODES, ignore = true)
    public abstract CompanyArticle toCompanyArticle(CompanyArticleEntity companyArticleEntity);
}
