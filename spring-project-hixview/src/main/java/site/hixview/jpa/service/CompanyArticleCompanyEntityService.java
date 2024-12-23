package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.CompanyArticleCompanyService;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.CompanyEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE_NUMBER;
import static site.hixview.aggregate.vo.WordCamel.COMPANY_CODE;
import static site.hixview.jpa.utils.MapperUtils.map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyArticleCompanyEntityService implements CompanyArticleCompanyService {

    private final CompanyEntityRepository companyEntityRepository;
    private final CompanyArticleEntityRepository companyArticleEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;

    private final CompanyArticleCompanyEntityMapper companyArticleCompanyEntityMapper = new CompanyArticleCompanyEntityMapperImpl();

    @Override
    public List<CompanyArticleCompany> getAll() {
        return cacEntityRepository.findAll().stream().map(companyArticleCompanyEntityMapper::toCompanyArticleCompany).toList();
    }

    @Override
    public Optional<CompanyArticleCompany> getByNumber(Long number) {
        return getOptionalCompanyArticleCompany(cacEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public List<CompanyArticleCompany> getByCompanyArticle(CompanyArticle companyArticle) {
        return cacEntityRepository.findByCompanyArticle(
                        companyArticleEntityRepository.findByNumber(companyArticle.getNumber()).orElseThrow())
                .stream().map(companyArticleCompanyEntityMapper::toCompanyArticleCompany).toList();
    }

    @Override
    public List<CompanyArticleCompany> getByCompany(Company company) {
        return cacEntityRepository.findByCompany(
                        companyEntityRepository.findByCode(company.getCode()).orElseThrow())
                .stream().map(companyArticleCompanyEntityMapper::toCompanyArticleCompany).toList();
    }

    @Override
    @Transactional
    public CompanyArticleCompany insert(CompanyArticleCompany companyArticleCompany) {
        Long number = companyArticleCompany.getNumber();
        Long articleNumber = companyArticleCompany.getArticleNumber();
        String companyCode = companyArticleCompany.getCompanyCode();
        if (cacEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, CompanyArticleCompanyEntity.class);
        }
        validateDuplicateEntity(articleNumber, companyCode);
        return companyArticleCompanyEntityMapper.toCompanyArticleCompany(cacEntityRepository.save(
                companyArticleCompanyEntityMapper.toCompanyArticleCompanyEntity(
                        companyArticleCompany, companyArticleEntityRepository, companyEntityRepository)));
    }
    
    @Override
    @Transactional
    public CompanyArticleCompany update(CompanyArticleCompany companyArticleCompany) {
        Long number = companyArticleCompany.getNumber();
        if (!cacEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, CompanyArticleCompanyEntity.class);
        }
        Long articleNumber = companyArticleCompany.getArticleNumber();
        String companyCode = companyArticleCompany.getCompanyCode();
        validateDuplicateEntity(articleNumber, companyCode);
        return companyArticleCompanyEntityMapper.toCompanyArticleCompany(cacEntityRepository.save(
                map(companyArticleCompany, cacEntityRepository.findByNumber(number).orElseThrow(),
                        companyArticleEntityRepository, companyEntityRepository)));
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!cacEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, CompanyArticleCompanyEntity.class);
        }
        cacEntityRepository.deleteByNumber(number);
    }

    private Optional<CompanyArticleCompany> getOptionalCompanyArticleCompany(CompanyArticleCompanyEntity companyArticleCompanyEntity) {
        if (companyArticleCompanyEntity == null) {
            return Optional.empty();
        }
        return Optional.of(companyArticleCompanyEntityMapper.toCompanyArticleCompany(companyArticleCompanyEntity));
    }
    
    private void validateDuplicateEntity(Long articleNumber, String companyCode) {
        if (cacEntityRepository.findByCompanyArticleAndCompany(
                companyArticleEntityRepository.findByNumber(articleNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(articleNumber, CompanyArticleEntity.class)),
                companyEntityRepository.findByCode(companyCode).orElseThrow(
                        () -> new EntityNotFoundException(getFormattedExceptionMessage(
                                CANNOT_FOUND_ENTITY, COMPANY_CODE, companyCode, CompanyEntity.class)))
        ).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, articleNumber, CompanyArticleEntity.class,
                    COMPANY_CODE, companyCode, CompanyEntity.class));
        }
    }
}
