package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;
import site.hixview.aggregate.service.CompanyService;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.CompanyEntityRepository;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordCamel.CODE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyEntityService implements CompanyService {

    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;

    private final CompanyEntityMapper companyEntityMapper = new CompanyEntityMapperImpl();

    @Override
    public List<Company> getAll() {
        return companyEntityRepository.findAll().stream().map(companyEntityMapper::toCompany).toList();
    }

    @Override
    public List<Company> getByCountryListed(Country countryListed) {
        return companyEntityRepository.findByCountryListed(countryListed.name()).stream().map(companyEntityMapper::toCompany).toList();
    }

    @Override
    public List<Company> getByScale(Scale scale) {
        return companyEntityRepository.findByScale(scale.name()).stream().map(companyEntityMapper::toCompany).toList();
    }

    @Override
    public List<Company> getByFirstCategory(FirstCategory firstCategory) {
        return companyEntityRepository.findByFirstCategory(
                        firstCategoryEntityRepository.findByNumber(firstCategory.getNumber()).orElseThrow())
                .stream().map(companyEntityMapper::toCompany).toList();
    }

    @Override
    public List<Company> getBySecondCategory(SecondCategory secondCategory) {
        return companyEntityRepository.findBySecondCategory(
                        secondCategoryEntityRepository.findByNumber(secondCategory.getNumber()).orElseThrow())
                .stream().map(companyEntityMapper::toCompany).toList();
    }

    @Override
    public Optional<Company> getByCode(String code) {
        return getOptionalCompany(companyEntityRepository.findByCode(code).orElse(null));
    }

    @Override
    public Optional<Company> getByKoreanName(String koreanName) {
        return getOptionalCompany(companyEntityRepository.findByKoreanName(koreanName).orElse(null));
    }

    @Override
    public Optional<Company> getByEnglishName(String englishName) {
        return getOptionalCompany(companyEntityRepository.findByEnglishName(englishName).orElse(null));
    }

    @Override
    public Optional<Company> getByNameListed(String nameListed) {
        return getOptionalCompany(companyEntityRepository.findByNameListed(nameListed).orElse(null));
    }

    @Override
    public Company insert(Company company) {
        String code = company.getCode();
        if (companyEntityRepository.existsByCode(code)) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, CODE, code, CompanyEntity.class));
        }
        return companyEntityMapper.toCompany(companyEntityRepository.save(companyEntityMapper.toCompanyEntity(
                company, firstCategoryEntityRepository, secondCategoryEntityRepository)));
    }

    @Override
    public Company update(Company company) {
        String code = company.getCode();
        if (!companyEntityRepository.existsByCode(code)) {
            throw new EntityNotFoundException(getFormattedExceptionMessage(
                    CANNOT_FOUND_ENTITY, CODE, code, CompanyEntity.class));
        }
        CompanyEntity companyEntity = companyEntityRepository.save(companyEntityMapper.toCompanyEntity(
                company, firstCategoryEntityRepository, secondCategoryEntityRepository));
        propagateCompanyEntity(companyEntity);
        return companyEntityMapper.toCompany(companyEntity);
    }

    @Override
    public void removeByCode(String code) {
        if (!companyEntityRepository.existsByCode(code)) {
            throw new EntityNotFoundException(getFormattedExceptionMessage(
                    CANNOT_FOUND_ENTITY, CODE, code, CompanyEntity.class));
        }
        CompanyEntity companyEntity = companyEntityRepository.findByCode(code).orElseThrow();
        if (!cacEntityRepository.findByCompany(companyEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, CODE, code, CompanyEntity.class));
        }
        companyEntityRepository.deleteByCode(code);
    }

    private Optional<Company> getOptionalCompany(CompanyEntity optionalCompanyEntity) {
        if (optionalCompanyEntity == null) {
            return Optional.empty();
        }
        return Optional.of(companyEntityMapper.toCompany(optionalCompanyEntity));
    }

    private void propagateCompanyEntity(CompanyEntity companyEntity) {
        cacEntityRepository.saveAll(cacEntityRepository.findByCompany(companyEntity).stream()
                .peek(mapper -> mapper.updateCompany(companyEntity)).toList());
    }
}
