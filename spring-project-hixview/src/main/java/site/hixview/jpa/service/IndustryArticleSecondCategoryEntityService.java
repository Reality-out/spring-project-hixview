package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.IndustryArticleSecondCategoryService;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE_NUMBER;
import static site.hixview.aggregate.vo.WordCamel.SECOND_CATEGORY_NUMBER;
import static site.hixview.jpa.utils.MapperUtils.map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndustryArticleSecondCategoryEntityService implements IndustryArticleSecondCategoryService {

    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;

    private final IndustryArticleSecondCategoryEntityMapper iascEntityMapper = new IndustryArticleSecondCategoryEntityMapperImpl();

    @Override
    public List<IndustryArticleSecondCategory> getAll() {
        return iascEntityRepository.findAll().stream().map(iascEntityMapper::toIndustryArticleSecondCategory).toList();
    }

    @Override
    public Optional<IndustryArticleSecondCategory> getByNumber(Long number) {
        return getOptionalIndustryArticleSecondCategory(iascEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public List<IndustryArticleSecondCategory> getByIndustryArticle(IndustryArticle industryArticle) {
        return iascEntityRepository.findByIndustryArticle(
                        industryArticleEntityRepository.findByNumber(industryArticle.getNumber()).orElseThrow())
                .stream().map(iascEntityMapper::toIndustryArticleSecondCategory).toList();
    }
    
    @Override
    public List<IndustryArticleSecondCategory> getBySecondCategory(SecondCategory secondCategory) {
        return iascEntityRepository.findBySecondCategory(
                        secondCategoryEntityRepository.findByNumber(secondCategory.getNumber()).orElseThrow())
                .stream().map(iascEntityMapper::toIndustryArticleSecondCategory).toList();
    }
    
    @Override
    public IndustryArticleSecondCategory insert(IndustryArticleSecondCategory industryArticleSecondCategory) {
        Long number = industryArticleSecondCategory.getNumber();
        Long articleNumber = industryArticleSecondCategory.getArticleNumber();
        Long secondCategoryNumber = industryArticleSecondCategory.getSecondCategoryNumber();
        if (iascEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, IndustryArticleSecondCategoryEntity.class);
        }
        validateDuplicateEntity(articleNumber, secondCategoryNumber);
        return iascEntityMapper.toIndustryArticleSecondCategory(iascEntityRepository.save(
                iascEntityMapper.toIndustryArticleSecondCategoryEntity(
                        industryArticleSecondCategory, industryArticleEntityRepository, secondCategoryEntityRepository)));
    }
    
    @Override
    public IndustryArticleSecondCategory update(IndustryArticleSecondCategory industryArticleSecondCategory) {
        Long number = industryArticleSecondCategory.getNumber();
        if (!iascEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, IndustryArticleSecondCategoryEntity.class);
        }
        Long articleNumber = industryArticleSecondCategory.getArticleNumber();
        Long secondCategoryNumber = industryArticleSecondCategory.getSecondCategoryNumber();
        validateDuplicateEntity(articleNumber, secondCategoryNumber);
        return iascEntityMapper.toIndustryArticleSecondCategory(iascEntityRepository.save(
                map(industryArticleSecondCategory, iascEntityRepository.findByNumber(number).orElseThrow(),
                        industryArticleEntityRepository, secondCategoryEntityRepository)));
    }

    @Override
    public void removeByNumber(Long number) {
        if (!iascEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, IndustryArticleSecondCategoryEntity.class);
        }
        iascEntityRepository.deleteByNumber(number);
    }

    private Optional<IndustryArticleSecondCategory> getOptionalIndustryArticleSecondCategory(IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity) {
        if (industryArticleSecondCategoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(iascEntityMapper.toIndustryArticleSecondCategory(industryArticleSecondCategoryEntity));
    }
    
    private void validateDuplicateEntity(Long articleNumber, Long secondCategoryNumber) {
        if (iascEntityRepository.findByIndustryArticleAndSecondCategory(
                industryArticleEntityRepository.findByNumber(articleNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(articleNumber, IndustryArticleEntity.class)),
                secondCategoryEntityRepository.findByNumber(secondCategoryNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(secondCategoryNumber, SecondCategoryEntity.class))
        ).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, articleNumber, IndustryArticleEntity.class,
                    SECOND_CATEGORY_NUMBER, secondCategoryNumber, SecondCategoryEntity.class));
        }
    }
}
