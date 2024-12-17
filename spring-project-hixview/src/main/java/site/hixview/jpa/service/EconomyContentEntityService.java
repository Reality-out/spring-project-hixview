package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.EconomyContentService;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.EconomyContentEntityMapperImpl;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EconomyContentEntityService implements EconomyContentService {

    private final EconomyArticleContentEntityRepository eacEntityRepository;
    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final EconomyContentEntityMapperImpl mapper = new EconomyContentEntityMapperImpl();

    @Override
    public List<EconomyContent> getAll() {
        return economyContentEntityRepository.findAll().stream().map(mapper::toEconomyContent).toList();
    }

    @Override
    public Optional<EconomyContent> getByNumber(Long number) {
        return getOptionalEconomyContent(economyContentEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<EconomyContent> getByName(String name) {
        return getOptionalEconomyContent(economyContentEntityRepository.findByName(name).orElse(null));
    }

    @Override
    @Transactional
    public EconomyContent insert(EconomyContent economyContent) {
        Long number = economyContent.getNumber();
        String name = economyContent.getName();
        if (economyContentEntityRepository.existsByNumber(number)) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, NUMBER, number, EconomyContent.class));
        }
        if (economyContentEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, NAME, name, EconomyContent.class));
        }
        return mapper.toEconomyContent(economyContentEntityRepository.save(mapper.toEconomyContentEntity(economyContent)));
    }

    @Override
    @Transactional
    public EconomyContent update(EconomyContent economyContent) {
        Long number = economyContent.getNumber();
        String name = economyContent.getName();
        if (!economyContentEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyContent.class);
        }
        if (economyContentEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, NAME, name, EconomyContent.class));
        }
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(mapper.toEconomyContentEntity(economyContent));
        List<EconomyArticleContentEntity> eacEntities = eacEntityRepository.findByEconomyContent(economyContentEntity);
        for (EconomyArticleContentEntity eacEntity : eacEntities) {
            eacEntity.updateEconomyContent(economyContentEntity);
        }
        eacEntityRepository.saveAll(eacEntities);
        return mapper.toEconomyContent(economyContentEntity);
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!economyContentEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyContent.class);
        }
        if (!eacEntityRepository.findByEconomyContent(economyContentEntityRepository.findByNumber(number).orElseThrow()).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, EconomyContent.class));
        }
        economyContentEntityRepository.deleteByNumber(number);
    }

    private Optional<EconomyContent> getOptionalEconomyContent(EconomyContentEntity economyContentEntity) {
        if (economyContentEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toEconomyContent(economyContentEntity));
    }
}