package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryEntityRepositoryFunction;

@Repository
public interface FirstCategoryEntityRepository extends BasicIndustryCategoryEntityRepositoryFunction<FirstCategoryEntity>, JpaRepository<FirstCategoryEntity, Long> {
}