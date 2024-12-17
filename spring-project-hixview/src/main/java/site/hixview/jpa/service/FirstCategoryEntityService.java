package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.FirstCategoryService;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.mapper.FirstCategoryEntityMapperImpl;
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
public class FirstCategoryEntityService implements FirstCategoryService {

    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final FirstCategoryEntityMapperImpl mapper = new FirstCategoryEntityMapperImpl();

    @Override
    public List<FirstCategory> getAll() {
        return firstCategoryEntityRepository.findAll().stream().map(mapper::toFirstCategory).toList();
    }

    @Override
    public Optional<FirstCategory> getByNumber(Long number) {
        return getOptionalFirstCategory(firstCategoryEntityRepository.findByNumber(number).orElse(null));
    }
    
    @Override
    public Optional<FirstCategory> getByKoreanName(String koreanName) {
        return getOptionalFirstCategory(firstCategoryEntityRepository.findByKoreanName(koreanName).orElse(null));
    }

    @Override
    public Optional<FirstCategory> getByEnglishName(String englishName) {
        return getOptionalFirstCategory(firstCategoryEntityRepository.findByEnglishName(englishName).orElse(null));
    }
    
    @Override
    public FirstCategory insert(FirstCategory firstCategory) {
        Long number = firstCategory.getNumber();
        String englishName = firstCategory.getEnglishName();
        if (firstCategoryEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, FirstCategoryEntity.class);
        }
        if (firstCategoryEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, FirstCategoryEntity.class));
        }
        return mapper.toFirstCategory(firstCategoryEntityRepository.save(mapper.toFirstCategoryEntity(
                firstCategory, industryCategoryEntityRepository)));
    }

    @Override
    public FirstCategory update(FirstCategory firstCategory) {
        Long number = firstCategory.getNumber();
        String englishName = firstCategory.getEnglishName();
        if (!firstCategoryEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, FirstCategoryEntity.class);
        }
        if (firstCategoryEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, FirstCategoryEntity.class));
        }
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(mapper.toFirstCategoryEntity(
                firstCategory, industryCategoryEntityRepository));
        propagateFirstCategoryEntity(firstCategoryEntity);
        return mapper.toFirstCategory(firstCategoryEntity);
    }
    
    @Override
    public void removeByNumber(Long number) {
        if (!firstCategoryEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, FirstCategoryEntity.class);
        }
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.findByNumber(number).orElseThrow();
        if (!secondCategoryEntityRepository.findByFirstCategory(firstCategoryEntity).isEmpty() ||
                !companyEntityRepository.findByFirstCategory(firstCategoryEntity).isEmpty() ||
                !industryArticleEntityRepository.findByFirstCategory(firstCategoryEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, FirstCategoryEntity.class));
        }
        firstCategoryEntityRepository.deleteByNumber(number);
    }

    private Optional<FirstCategory> getOptionalFirstCategory(FirstCategoryEntity optionalFirstCategoryEntity) {
        if (optionalFirstCategoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toFirstCategory(optionalFirstCategoryEntity));
    }

    private void propagateFirstCategoryEntity(FirstCategoryEntity firstCategoryEntity) {
        secondCategoryEntityRepository.saveAll(
                secondCategoryEntityRepository.findByFirstCategory(firstCategoryEntity).stream().peek(
                        category -> category.updateFirstCategory(firstCategoryEntity)).toList());
        companyEntityRepository.saveAll(
                companyEntityRepository.findByFirstCategory(firstCategoryEntity).stream().map(company -> {
                    company = CompanyEntity.builder().company(company).firstCategory(firstCategoryEntity).build();
                    return company;
                }).toList());
        industryArticleEntityRepository.saveAll(
                industryArticleEntityRepository.findByFirstCategory(firstCategoryEntity).stream().map(article -> {
                    article = IndustryArticleEntity.builder().industryArticle(article).firstCategory(firstCategoryEntity).build();
                    return article;
                }).toList());
    }
}
