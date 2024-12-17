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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndustryArticleSecondCategoryEntityService implements IndustryArticleSecondCategoryService {

    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;

    private final IndustryArticleSecondCategoryEntityMapper industryArticleSecondCategoryEntityMapper = new IndustryArticleSecondCategoryEntityMapperImpl();
    private final SecondCategoryEntityMapper secondCategoryEntityMapper = new SecondCategoryEntityMapperImpl();
    private final IndustryArticleEntityMapper industryArticleEntityMapper = new IndustryArticleEntityMapperImpl();

    @Override
    public List<IndustryArticleSecondCategory> getAll() {
        return iascEntityRepository.findAll().stream().map(industryArticleSecondCategoryEntityMapper::toIndustryArticleSecondCategory).toList();
    }

    @Override
    public Optional<IndustryArticleSecondCategory> getByNumber(Long number) {
        return getOptionalIndustryArticleSecondCategory(iascEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public List<IndustryArticleSecondCategory> getByIndustryArticle(IndustryArticle industryArticle) {
        return iascEntityRepository.findByIndustryArticle(industryArticleEntityMapper.toIndustryArticleEntity(
                        industryArticle, articleEntityRepository, pressEntityRepository, firstCategoryEntityRepository))
                .stream().map(industryArticleSecondCategoryEntityMapper::toIndustryArticleSecondCategory).toList();
    }
    
    @Override
    public List<IndustryArticleSecondCategory> getBySecondCategory(SecondCategory secondCategory) {
        return iascEntityRepository.findBySecondCategory(secondCategoryEntityMapper.toSecondCategoryEntity(
                        secondCategory, industryCategoryEntityRepository, firstCategoryEntityRepository))
                .stream().map(industryArticleSecondCategoryEntityMapper::toIndustryArticleSecondCategory).toList();
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
        return industryArticleSecondCategoryEntityMapper.toIndustryArticleSecondCategory(iascEntityRepository.save(
                industryArticleSecondCategoryEntityMapper.toIndustryArticleSecondCategoryEntity(
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
        return industryArticleSecondCategoryEntityMapper.toIndustryArticleSecondCategory(iascEntityRepository.save(
                industryArticleSecondCategoryEntityMapper.toIndustryArticleSecondCategoryEntity(
                        industryArticleSecondCategory, industryArticleEntityRepository, secondCategoryEntityRepository)));
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
        return Optional.of(industryArticleSecondCategoryEntityMapper.toIndustryArticleSecondCategory(industryArticleSecondCategoryEntity));
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
