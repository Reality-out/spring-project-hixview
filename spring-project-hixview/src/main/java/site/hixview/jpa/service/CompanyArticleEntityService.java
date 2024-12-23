package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.CompanyArticleService;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.mapper.CompanyArticleEntityMapper;
import site.hixview.jpa.mapper.CompanyArticleEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
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
public class CompanyArticleEntityService implements CompanyArticleService {

    private final PressEntityRepository pressEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final CompanyArticleEntityRepository companyArticleEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;
    
    private final CompanyArticleEntityMapper mapper = new CompanyArticleEntityMapperImpl();

    @Override
    public List<CompanyArticle> getAll() {
        return companyArticleEntityRepository.findAll().stream().map((CompanyArticleEntity companyArticleEntity) -> 
                mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)).toList();
    }

    @Override
    public List<CompanyArticle> getByDate(LocalDate date) {
        return companyArticleEntityRepository.findByDate(date).stream().map((CompanyArticleEntity companyArticleEntity) ->
                mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)).toList();
    }

    @Override
    public List<CompanyArticle> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return companyArticleEntityRepository.findByDateBetween(startDate, endDate).stream()
                .map((CompanyArticleEntity companyArticleEntity) ->
                        mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)).toList();
    }

    @Override
    public List<CompanyArticle> getBySubjectCountry(Country subjectCountry) {
        return companyArticleEntityRepository.findBySubjectCountry(subjectCountry.name()).stream()
                .map((CompanyArticleEntity companyArticleEntity) ->
                        mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)).toList();
    }

    @Override
    public List<CompanyArticle> getByImportance(Importance importance) {
        return companyArticleEntityRepository.findByImportance(importance.name()).stream()
                .map((CompanyArticleEntity companyArticleEntity) ->
                        mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)).toList();
    }

    @Override
    public List<CompanyArticle> getByPress(Press press) {
        return companyArticleEntityRepository.findByPress(
                        pressEntityRepository.findByNumber(press.getNumber()).orElseThrow())
                .stream().map((CompanyArticleEntity companyArticleEntity) ->
                        mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)).toList();
    }

    @Override
    public Optional<CompanyArticle> getByNumber(Long number) {
        return getOptionalCompanyArticle(companyArticleEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<CompanyArticle> getByName(String name) {
        return getOptionalCompanyArticle(companyArticleEntityRepository.findByName(name).orElse(null));
    }

    @Override
    public Optional<CompanyArticle> getByLink(String link) {
        return getOptionalCompanyArticle(companyArticleEntityRepository.findByLink(link).orElse(null));
    }

    @Override
    public CompanyArticle insert(CompanyArticle companyArticle) {
        Long number = companyArticle.getNumber();
        String name = companyArticle.getName();
        if (companyArticleEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, CompanyArticleEntity.class);
        }
        if (companyArticleEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, CompanyArticleEntity.class);
        }
        return mapper.toCompanyArticle(companyArticleEntityRepository.save(mapper.toCompanyArticleEntity(
                companyArticle, articleEntityRepository, pressEntityRepository)), cacEntityRepository);
    }

    @Override
    public CompanyArticle update(CompanyArticle companyArticle) {
        Long number = companyArticle.getNumber();
        String name = companyArticle.getName();
        if (!companyArticleEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, CompanyArticleEntity.class);
        }
        if (companyArticleEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, CompanyArticleEntity.class);
        }
        CompanyArticleEntity companyArticleEntity = companyArticleEntityRepository.save(mapper.toCompanyArticleEntity(
                companyArticle, articleEntityRepository, pressEntityRepository));
        propagateCompanyArticleEntity(companyArticleEntity);
        return mapper.toCompanyArticle(companyArticleEntity, cacEntityRepository);
    }
    
    @Override
    public void removeByNumber(Long number) {
        if (!companyArticleEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, CompanyArticleEntity.class);
        }
        CompanyArticleEntity companyArticleEntity = companyArticleEntityRepository.findByNumber(number).orElseThrow();
        if (!cacEntityRepository.findByCompanyArticle(companyArticleEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, CompanyArticleEntity.class));
        }
        companyArticleEntityRepository.deleteByNumber(number);
    }

    private Optional<CompanyArticle> getOptionalCompanyArticle(CompanyArticleEntity optionalCompanyArticleEntity) {
        if (optionalCompanyArticleEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toCompanyArticle(optionalCompanyArticleEntity, cacEntityRepository));
    }

    private void propagateCompanyArticleEntity(CompanyArticleEntity companyArticleEntity) {
        cacEntityRepository.saveAll(cacEntityRepository.findByCompanyArticle(companyArticleEntity).stream()
                        .peek(mapper -> mapper.updateCompanyArticle(companyArticleEntity)).toList());
    }
}
