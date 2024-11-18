package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryRepositoryFunction;

public interface FirstCategoryRepository extends BasicIndustryCategoryRepositoryFunction<FirstCategoryEntity>, JpaRepository<FirstCategoryEntity, Long> {
}