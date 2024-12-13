package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.method.BasicIndustryCategoryEntityRepositoryFunction;

@Repository
public interface IndustryCategoryEntityRepository extends BasicIndustryCategoryEntityRepositoryFunction<IndustryCategoryEntity>, JpaRepository<IndustryCategoryEntity, Long> {
}