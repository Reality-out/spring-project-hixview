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
import site.hixview.jpa.mapper.FirstCategoryEntityMapper;
import site.hixview.jpa.mapper.FirstCategoryEntityMapperImpl;
import site.hixview.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ENGLISH_NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;
import static site.hixview.jpa.utils.MapperUtils.map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FirstCategoryEntityService implements FirstCategoryService {

    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final FirstCategoryEntityRepository fcEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final FirstCategoryEntityMapper mapper = new FirstCategoryEntityMapperImpl();

    @Override
    public List<FirstCategory> getAll() {
        return fcEntityRepository.findAll().stream().map(mapper::toFirstCategory).toList();
    }

    @Override
    public Optional<FirstCategory> getByNumber(Long number) {
        return getOptionalFirstCategory(fcEntityRepository.findByNumber(number).orElse(null));
    }
    
    @Override
    public Optional<FirstCategory> getByKoreanName(String koreanName) {
        return getOptionalFirstCategory(fcEntityRepository.findByKoreanName(koreanName).orElse(null));
    }

    @Override
    public Optional<FirstCategory> getByEnglishName(String englishName) {
        return getOptionalFirstCategory(fcEntityRepository.findByEnglishName(englishName).orElse(null));
    }
    
    @Override
    public FirstCategory insert(FirstCategory firstCategory) {
        Long number = firstCategory.getNumber();
        String englishName = firstCategory.getEnglishName();
        if (fcEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, FirstCategoryEntity.class);
        }
        if (fcEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, FirstCategoryEntity.class));
        }
        return mapper.toFirstCategory(fcEntityRepository.save(mapper.toFirstCategoryEntity(
                firstCategory, industryCategoryEntityRepository)));
    }

    @Override
    public FirstCategory update(FirstCategory firstCategory) {
        Long number = firstCategory.getNumber();
        String englishName = firstCategory.getEnglishName();
        if (!fcEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, FirstCategoryEntity.class);
        }
        if (fcEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, FirstCategoryEntity.class));
        }
        FirstCategoryEntity firstCategoryEntity = fcEntityRepository.save(map(firstCategory,
                fcEntityRepository.findByNumber(number).orElseThrow(), industryCategoryEntityRepository));
        propagateFirstCategoryEntity(firstCategoryEntity);
        return mapper.toFirstCategory(firstCategoryEntity);
    }
    
    @Override
    public void removeByNumber(Long number) {
        if (!fcEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, FirstCategoryEntity.class);
        }
        FirstCategoryEntity firstCategoryEntity = fcEntityRepository.findByNumber(number).orElseThrow();
        if (!secondCategoryEntityRepository.findByFirstCategory(firstCategoryEntity).isEmpty() ||
                !companyEntityRepository.findByFirstCategory(firstCategoryEntity).isEmpty() ||
                !industryArticleEntityRepository.findByFirstCategory(firstCategoryEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, FirstCategoryEntity.class));
        }
        fcEntityRepository.deleteByNumber(number);
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
