package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryEntityRepositoryFunction;

import java.util.List;

@Repository
public interface SecondCategoryEntityRepository extends BasicIndustryCategoryEntityRepositoryFunction<SecondCategoryEntity>, JpaRepository<SecondCategoryEntity, Long> {
    /**
     * SELECT SecondCategory
     */
    List<SecondCategoryEntity> findByFirstCategory(FirstCategoryEntity firstCategory);
}