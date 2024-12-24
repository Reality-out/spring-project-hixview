package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.domain.Press;
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
import static site.hixview.jpa.utils.MapperUtils.map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndustryArticleEntityService implements IndustryArticleService {

    private final PressEntityRepository pressEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;
    
    private final IndustryArticleEntityMapper mapper = new IndustryArticleEntityMapperImpl();

    @Override
    public List<IndustryArticle> getAll() {
        return iaEntityRepository.findAll().stream().map((IndustryArticleEntity industryArticleEntity) ->
                mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByDate(LocalDate date) {
        return iaEntityRepository.findByDate(date).stream().map((IndustryArticleEntity industryArticleEntity) ->
                mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return iaEntityRepository.findByDateBetween(startDate, endDate).stream()
                .map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getBySubjectCountry(Country subjectCountry) {
        return iaEntityRepository.findBySubjectCountry(subjectCountry.name()).stream()
                .map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByImportance(Importance importance) {
        return iaEntityRepository.findByImportance(importance.name()).stream()
                .map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByPress(Press press) {
        return iaEntityRepository.findByPress(
                        pressEntityRepository.findByNumber(press.getNumber()).orElseThrow())
                .stream().map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public List<IndustryArticle> getByFirstCategory(FirstCategory firstCategory) {
        return iaEntityRepository.findByFirstCategory(
                        firstCategoryEntityRepository.findByNumber(firstCategory.getNumber()).orElseThrow())
                .stream().map((IndustryArticleEntity industryArticleEntity) ->
                        mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)).toList();
    }

    @Override
    public Optional<IndustryArticle> getByNumber(Long number) {
        return getOptionalIndustryArticle(iaEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<IndustryArticle> getByName(String name) {
        return getOptionalIndustryArticle(iaEntityRepository.findByName(name).orElse(null));
    }

    @Override
    public Optional<IndustryArticle> getByLink(String link) {
        return getOptionalIndustryArticle(iaEntityRepository.findByLink(link).orElse(null));
    }

    @Override
    @Transactional
    public IndustryArticle insert(IndustryArticle industryArticle) {
        Long number = industryArticle.getNumber();
        String name = industryArticle.getName();
        if (iaEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, IndustryArticleEntity.class);
        }
        if (iaEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, IndustryArticleEntity.class);
        }
        return mapper.toIndustryArticle(iaEntityRepository.save(mapper.toIndustryArticleEntity(
                industryArticle, articleEntityRepository, pressEntityRepository,
                firstCategoryEntityRepository)), iascEntityRepository);
    }

    @Override
    @Transactional
    public IndustryArticle update(IndustryArticle industryArticle) {
        Long number = industryArticle.getNumber();
        String name = industryArticle.getName();
        if (!iaEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, IndustryArticleEntity.class);
        }
        if (iaEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, IndustryArticleEntity.class);
        }
        IndustryArticleEntity industryArticleEntity = iaEntityRepository.save(map(industryArticle,
                iaEntityRepository.findByNumber(number).orElseThrow(), articleEntityRepository, pressEntityRepository, firstCategoryEntityRepository)
        );
        propagateIndustryArticleEntity(industryArticleEntity);
        return mapper.toIndustryArticle(industryArticleEntity, iascEntityRepository);
    }
    
    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!iaEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, IndustryArticleEntity.class);
        }
        IndustryArticleEntity industryArticleEntity = iaEntityRepository.findByNumber(number).orElseThrow();
        if (!iascEntityRepository.findByIndustryArticle(industryArticleEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, IndustryArticleEntity.class));
        }
        iaEntityRepository.deleteByNumber(number);
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
