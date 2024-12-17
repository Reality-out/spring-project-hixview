package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.IndustryArticleService;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.mapper.IndustryArticleEntityMapper;
import site.hixview.jpa.mapper.IndustryArticleEntityMapperImpl;
import site.hixview.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndustryArticleEntityService implements IndustryArticleService {

    private final PressEntityRepository pressEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;
    
    private final IndustryArticleEntityMapper mapper = new IndustryArticleEntityMapperImpl();

    @Override
    public List<IndustryArticle> getAll() {
        return industryArticleEntityRepository.findAll().stream().map((IndustryArticleEntity industryArticleEntity) -> 
                mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByDate(LocalDate date) {
        return industryArticleEntityRepository.findByDate(date).stream().map((IndustryArticleEntity industryArticleEntity) ->
                mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return industryArticleEntityRepository.findByDateBetween(startDate, endDate).stream()
                .map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getBySubjectCountry(Country subjectCountry) {
        return industryArticleEntityRepository.findBySubjectCountry(subjectCountry.name()).stream()
                .map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByImportance(Importance importance) {
        return industryArticleEntityRepository.findByImportance(importance.name()).stream()
                .map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public Optional<IndustryArticle> getByNumber(Long number) {
        return getOptionalIndustryArticle(industryArticleEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<IndustryArticle> getByName(String name) {
        return getOptionalIndustryArticle(industryArticleEntityRepository.findByName(name).orElse(null));
    }

    @Override
    public Optional<IndustryArticle> getByLink(String link) {
        return getOptionalIndustryArticle(industryArticleEntityRepository.findByLink(link).orElse(null));
    }

    @Override
    public IndustryArticle insert(IndustryArticle industryArticle) {
        Long number = industryArticle.getNumber();
        String name = industryArticle.getName();
        if (industryArticleEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, IndustryArticleEntity.class);
        }
        if (industryArticleEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, IndustryArticleEntity.class);
        }
        return mapper.toIndustryArticle(industryArticleEntityRepository.save(mapper.toIndustryArticleEntity(
                industryArticle, articleEntityRepository, pressEntityRepository,
                firstCategoryEntityRepository)), iascEntityRepository);
    }

    @Override
    public IndustryArticle update(IndustryArticle industryArticle) {
        Long number = industryArticle.getNumber();
        String name = industryArticle.getName();
        if (!industryArticleEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, IndustryArticleEntity.class);
        }
        if (industryArticleEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, IndustryArticleEntity.class);
        }
        IndustryArticleEntity industryArticleEntity = industryArticleEntityRepository.save(mapper.toIndustryArticleEntity(
                industryArticle, articleEntityRepository, pressEntityRepository, firstCategoryEntityRepository));
        propagateIndustryArticleEntity(industryArticleEntity);
        return mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository);
    }
    
    @Override
    public void removeByNumber(Long number) {
        if (!industryArticleEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, IndustryArticleEntity.class);
        }
        IndustryArticleEntity industryArticleEntity = industryArticleEntityRepository.findByNumber(number).orElseThrow();
        if (!iascEntityRepository.findByIndustryArticle(industryArticleEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, IndustryArticleEntity.class));
        }
        industryArticleEntityRepository.deleteByNumber(number);
    }

    private Optional<IndustryArticle> getOptionalIndustryArticle(IndustryArticleEntity optionalIndustryArticleEntity) {
        if (optionalIndustryArticleEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toIndustryArticle(optionalIndustryArticleEntity, iascEntityRepository));
    }

    private void propagateIndustryArticleEntity(IndustryArticleEntity industryArticleEntity) {
        iascEntityRepository.saveAll(iascEntityRepository.findByIndustryArticle(industryArticleEntity).stream()
                        .peek(mapper -> mapper.updateIndustryArticle(industryArticleEntity)).toList());
    }
}
