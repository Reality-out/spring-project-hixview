package site.hixview.jpa.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.mapper.support.CompanyArticleEntityMapperSupport;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyArticleEntityMapper extends CompanyArticleEntityMapperSupport {
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    @Mapping(target = COMPANY_ARTICLE, ignore = true)
    CompanyArticleEntity toCompanyArticleEntity(CompanyArticle companyArticle,
                                                @Context ArticleEntityRepository articleEntityRepository,
                                                @Context PressEntityRepository pressEntityRepository);

    @Mapping(source = ARTICLE, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(source = PRESS, target = PRESS_NUMBER, qualifiedByName = "pressNumberToDomain")
    @Mapping(target = MAPPED_COMPANY_CODES, ignore = true)
    @Mapping(target = COMPANY_ARTICLE, ignore = true)
    CompanyArticle toCompanyArticle(CompanyArticleEntity companyArticleEntity,
                                    @Context CompanyArticleCompanyEntityRepository companyArticleCompanyRepository);
}
