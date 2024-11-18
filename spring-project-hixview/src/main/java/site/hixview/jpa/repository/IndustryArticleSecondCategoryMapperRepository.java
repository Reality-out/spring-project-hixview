package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryMapperEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

public interface IndustryArticleSecondCategoryMapperRepository extends BasicMapperRepositoryFunction<IndustryArticleEntity, SecondCategoryEntity>, JpaRepository<IndustryArticleSecondCategoryMapperEntity, Long> {
}
