package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryRepositoryFunction;

@Repository
public interface FirstCategoryRepository extends BasicIndustryCategoryRepositoryFunction<FirstCategoryEntity>, JpaRepository<FirstCategoryEntity, Long> {
}