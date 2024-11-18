package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.aggregate.domain.Company;
import site.hixview.jpa.entity.CompanyEntity;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
    /**
     * SELECT Company
     */
    Optional<Company> findByCode(String code);

    Optional<Company> findByName(String name);

    /**
     * REMOVE Company
     */
    void deleteByCode(String code);
}