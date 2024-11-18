package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryRepositoryFunction;

public interface IndustryCategoryRepository extends BasicIndustryCategoryRepositoryFunction<IndustryCategoryEntity>, JpaRepository<IndustryCategoryEntity, Long> {
}