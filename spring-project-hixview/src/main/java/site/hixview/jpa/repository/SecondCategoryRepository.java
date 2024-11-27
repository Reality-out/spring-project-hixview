package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryRepositoryFunction;

import java.util.List;

@Repository
public interface SecondCategoryRepository extends BasicIndustryCategoryRepositoryFunction<SecondCategoryEntity>, JpaRepository<SecondCategoryEntity, Long> {
    /**
     * SELECT SecondCategory
     */
    List<SecondCategoryEntity> findByIndustryCategory(IndustryCategoryEntity industryCategory);

    List<SecondCategoryEntity> findByFirstCategory(FirstCategoryEntity firstCategory);

    /**
     * CHECK SecondCategory
     */
    boolean existsByNumber(Long number);
}