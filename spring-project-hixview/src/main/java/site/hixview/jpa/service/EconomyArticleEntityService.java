package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.EconomyArticleService;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.mapper.EconomyArticleEntityMapper;
import site.hixview.jpa.mapper.EconomyArticleEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EconomyArticleEntityService implements EconomyArticleService {

    private final PressEntityRepository pressEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final EconomyArticleEntityRepository economyArticleEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;
    
    private final EconomyArticleEntityMapper mapper = new EconomyArticleEntityMapperImpl();

    @Override
    public List<EconomyArticle> getAll() {
        return economyArticleEntityRepository.findAll().stream().map((EconomyArticleEntity economyArticleEntity) -> 
                mapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)).toList();
    }

    @Override
    public List<EconomyArticle> getByDate(LocalDate date) {
        return economyArticleEntityRepository.findByDate(date).stream().map((EconomyArticleEntity economyArticleEntity) ->
                mapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)).toList();
    }

    @Override
    public List<EconomyArticle> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return economyArticleEntityRepository.findByDateBetween(startDate, endDate).stream()
                .map((EconomyArticleEntity economyArticleEntity) ->
                        mapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)).toList();
    }

    @Override
    public List<EconomyArticle> getBySubjectCountry(Country subjectCountry) {
        return economyArticleEntityRepository.findBySubjectCountry(subjectCountry.name()).stream()
                .map((EconomyArticleEntity economyArticleEntity) ->
                        mapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)).toList();
    }

    @Override
    public List<EconomyArticle> getByImportance(Importance importance) {
        return economyArticleEntityRepository.findByImportance(importance.name()).stream()
                .map((EconomyArticleEntity economyArticleEntity) ->
                        mapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)).toList();
    }

    @Override
    public Optional<EconomyArticle> getByNumber(Long number) {
        return getOptionalEconomyArticle(economyArticleEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<EconomyArticle> getByName(String name) {
        return getOptionalEconomyArticle(economyArticleEntityRepository.findByName(name).orElse(null));
    }

    @Override
    public Optional<EconomyArticle> getByLink(String link) {
        return getOptionalEconomyArticle(economyArticleEntityRepository.findByLink(link).orElse(null));
    }

    @Override
    public EconomyArticle insert(EconomyArticle economyArticle) {
        Long number = economyArticle.getNumber();
        String name = economyArticle.getName();
        if (economyArticleEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, EconomyArticleEntity.class);
        }
        if (economyArticleEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, EconomyArticleEntity.class);
        }
        return mapper.toEconomyArticle(economyArticleEntityRepository.save(mapper.toEconomyArticleEntity(
                economyArticle, articleEntityRepository, pressEntityRepository)), eacEntityRepository);
    }

    @Override
    public EconomyArticle update(EconomyArticle economyArticle) {
        Long number = economyArticle.getNumber();
        String name = economyArticle.getName();
        if (!economyArticleEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyArticleEntity.class);
        }
        if (economyArticleEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, EconomyArticleEntity.class);
        }
        EconomyArticleEntity economyArticleEntity = economyArticleEntityRepository.save(mapper.toEconomyArticleEntity(
                economyArticle, articleEntityRepository, pressEntityRepository));
        propagateEconomyArticleEntity(economyArticleEntity);
        return mapper.toEconomyArticle(economyArticleEntity, eacEntityRepository);
    }
    
    @Override
    public void removeByNumber(Long number) {
        if (!economyArticleEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyArticleEntity.class);
        }
        EconomyArticleEntity economyArticleEntity = economyArticleEntityRepository.findByNumber(number).orElseThrow();
        if (!eacEntityRepository.findByEconomyArticle(economyArticleEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, EconomyArticleEntity.class));
        }
        economyArticleEntityRepository.deleteByNumber(number);
    }

    private Optional<EconomyArticle> getOptionalEconomyArticle(EconomyArticleEntity optionalEconomyArticleEntity) {
        if (optionalEconomyArticleEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toEconomyArticle(optionalEconomyArticleEntity, eacEntityRepository));
    }

    private void propagateEconomyArticleEntity(EconomyArticleEntity economyArticleEntity) {
        eacEntityRepository.saveAll(eacEntityRepository.findByEconomyArticle(economyArticleEntity).stream()
                        .peek(mapper -> mapper.updateEconomyArticle(economyArticleEntity)).toList());
    }
}
