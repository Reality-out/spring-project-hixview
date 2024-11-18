package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryRepositoryFunction;

public interface SecondCategoryRepository extends BasicIndustryCategoryRepositoryFunction<SecondCategoryEntity>, JpaRepository<SecondCategoryEntity, Long> {
}