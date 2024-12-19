package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.SecondCategoryService;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.mapper.SecondCategoryEntityMapper;
import site.hixview.jpa.mapper.SecondCategoryEntityMapperImpl;
import site.hixview.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ENGLISH_NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecondCategoryEntityService implements SecondCategoryService {

    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final SecondCategoryEntityMapper mapper = new SecondCategoryEntityMapperImpl();

    @Override
    public List<SecondCategory> getAll() {
        return secondCategoryEntityRepository.findAll().stream().map(mapper::toSecondCategory).toList();
    }

    @Override
    public Optional<SecondCategory> getByNumber(Long number) {
        return getOptionalSecondCategory(secondCategoryEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<SecondCategory> getByKoreanName(String koreanName) {
        return getOptionalSecondCategory(secondCategoryEntityRepository.findByKoreanName(koreanName).orElse(null));
    }

    @Override
    public Optional<SecondCategory> getByEnglishName(String englishName) {
        return getOptionalSecondCategory(secondCategoryEntityRepository.findByEnglishName(englishName).orElse(null));
    }

    @Override
    public SecondCategory insert(SecondCategory secondCategory) {
        Long number = secondCategory.getNumber();
        String englishName = secondCategory.getEnglishName();
        if (secondCategoryEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, SecondCategoryEntity.class);
        }
        if (secondCategoryEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, SecondCategoryEntity.class));
        }
        return mapper.toSecondCategory(secondCategoryEntityRepository.save(mapper.toSecondCategoryEntity(
                secondCategory, industryCategoryEntityRepository, firstCategoryEntityRepository)));
    }

    @Override
    public SecondCategory update(SecondCategory secondCategory) {
        Long number = secondCategory.getNumber();
        String englishName = secondCategory.getEnglishName();
        if (!secondCategoryEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, SecondCategoryEntity.class);
        }
        if (secondCategoryEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, SecondCategoryEntity.class));
        }
        SecondCategoryEntity secondCategoryEntity = secondCategoryEntityRepository.save(mapper.toSecondCategoryEntity(
                secondCategory, industryCategoryEntityRepository, firstCategoryEntityRepository));
        propagateSecondCategoryEntity(secondCategoryEntity);
        return mapper.toSecondCategory(secondCategoryEntity);
    }

    @Override
    public void removeByNumber(Long number) {
        if (!secondCategoryEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, SecondCategoryEntity.class);
        }
        SecondCategoryEntity secondCategoryEntity = secondCategoryEntityRepository.findByNumber(number).orElseThrow();
        if (!companyEntityRepository.findBySecondCategory(secondCategoryEntity).isEmpty() ||
                !iascEntityRepository.findBySecondCategory(secondCategoryEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, SecondCategoryEntity.class));
        }
        secondCategoryEntityRepository.deleteByNumber(number);
    }

    private Optional<SecondCategory> getOptionalSecondCategory(SecondCategoryEntity optionalSecondCategoryEntity) {
        if (optionalSecondCategoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toSecondCategory(optionalSecondCategoryEntity));
    }

    private void propagateSecondCategoryEntity(SecondCategoryEntity secondCategoryEntity) {
        companyEntityRepository.saveAll(
                companyEntityRepository.findBySecondCategory(secondCategoryEntity).stream().map(company -> {
                    company = CompanyEntity.builder().company(company).secondCategory(secondCategoryEntity).build();
                    return company;
                }).toList());
        iascEntityRepository.saveAll(
                iascEntityRepository.findBySecondCategory(secondCategoryEntity).stream()
                        .peek(mapper -> mapper.updateSecondCategory(secondCategoryEntity)).toList());
    }
}
