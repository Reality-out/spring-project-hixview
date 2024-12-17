package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.EconomyArticleContentService;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE_NUMBER;
import static site.hixview.aggregate.vo.WordCamel.CONTENT_NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EconomyArticleContentEntityService implements EconomyArticleContentService {

    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final EconomyArticleEntityRepository economyArticleEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;

    private final EconomyArticleContentEntityMapper economyArticleContentEntityMapper = new EconomyArticleContentEntityMapperImpl();
    private final EconomyContentEntityMapper economyContentEntityMapper = new EconomyContentEntityMapperImpl();
    private final EconomyArticleEntityMapper economyArticleEntityMapper = new EconomyArticleEntityMapperImpl();

    @Override
    public List<EconomyArticleContent> getAll() {
        return eacEntityRepository.findAll().stream().map(economyArticleContentEntityMapper::toEconomyArticleContent).toList();
    }

    @Override
    public Optional<EconomyArticleContent> getByNumber(Long number) {
        return getOptionalEconomyArticleContent(eacEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public List<EconomyArticleContent> getByEconomyArticle(EconomyArticle economyArticle) {
        return eacEntityRepository.findByEconomyArticle(
                        economyArticleEntityMapper.toEconomyArticleEntity(
                                economyArticle, articleEntityRepository, pressEntityRepository))
                .stream().map(economyArticleContentEntityMapper::toEconomyArticleContent).toList();
    }
    
    @Override
    public List<EconomyArticleContent> getByEconomyContent(EconomyContent economyContent) {
        return eacEntityRepository.findByEconomyContent(
                        economyContentEntityMapper.toEconomyContentEntity(economyContent))
                .stream().map(economyArticleContentEntityMapper::toEconomyArticleContent).toList();
    }
    
    @Override
    public EconomyArticleContent insert(EconomyArticleContent economyArticleContent) {
        Long number = economyArticleContent.getNumber();
        Long articleNumber = economyArticleContent.getArticleNumber();
        Long contentNumber = economyArticleContent.getContentNumber();
        if (eacEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, EconomyArticleContentEntity.class);
        }
        validateDuplicateEntity(articleNumber, contentNumber);
        return economyArticleContentEntityMapper.toEconomyArticleContent(eacEntityRepository.save(
                economyArticleContentEntityMapper.toEconomyArticleContentEntity(
                        economyArticleContent, economyArticleEntityRepository, economyContentEntityRepository)));
    }
    
    @Override
    public EconomyArticleContent update(EconomyArticleContent economyArticleContent) {
        Long number = economyArticleContent.getNumber();
        if (!eacEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyArticleContentEntity.class);
        }
        Long articleNumber = economyArticleContent.getArticleNumber();
        Long contentNumber = economyArticleContent.getContentNumber();
        validateDuplicateEntity(articleNumber, contentNumber);
        return economyArticleContentEntityMapper.toEconomyArticleContent(eacEntityRepository.save(
                economyArticleContentEntityMapper.toEconomyArticleContentEntity(
                        economyArticleContent, economyArticleEntityRepository, economyContentEntityRepository)));
    }

    @Override
    public void removeByNumber(Long number) {
        if (!eacEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyArticleContentEntity.class);
        }
        eacEntityRepository.deleteByNumber(number);
    }

    private Optional<EconomyArticleContent> getOptionalEconomyArticleContent(EconomyArticleContentEntity economyArticleContentEntity) {
        if (economyArticleContentEntity == null) {
            return Optional.empty();
        }
        return Optional.of(economyArticleContentEntityMapper.toEconomyArticleContent(economyArticleContentEntity));
    }
    
    private void validateDuplicateEntity(Long articleNumber, Long contentNumber) {
        if (eacEntityRepository.findByEconomyArticleAndEconomyContent(
                economyArticleEntityRepository.findByNumber(articleNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(articleNumber, EconomyArticleEntity.class)),
                economyContentEntityRepository.findByNumber(contentNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(contentNumber, EconomyContentEntity.class))
        ).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, articleNumber, EconomyArticleEntity.class,
                    CONTENT_NUMBER, contentNumber, EconomyContentEntity.class));
        }
    }
}
