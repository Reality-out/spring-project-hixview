package site.hixview.jpa.repository;

import site.hixview.aggregate.domain.Company;

import java.util.Optional;

public interface CompanyRepository {
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