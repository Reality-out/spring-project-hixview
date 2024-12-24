package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.CompanyArticleEntityMapper;
import site.hixview.jpa.mapper.CompanyArticleEntityMapperImpl;
import site.hixview.jpa.mapper.PressEntityMapper;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.CompanyArticleCompanyEntityTestUtils;
import site.hixview.support.jpa.util.CompanyArticleEntityTestUtils;
import site.hixview.support.spring.util.CompanyArticleTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordCamel.NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@OnlyRealServiceContext
@Slf4j
class CompanyArticleEntityServiceTest implements CompanyArticleEntityTestUtils, CompanyArticleCompanyEntityTestUtils, CompanyArticleTestUtils {

    private final CompanyArticleEntityService caEntityService;
    private final CompanyArticleEntityRepository caEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;

    private final CompanyArticleEntityMapper caEntityMapper = new CompanyArticleEntityMapperImpl();
    private final PressEntityMapper pressEntityMapper = new PressEntityMapperImpl();

    @Autowired
    CompanyArticleEntityServiceTest(CompanyArticleEntityService caEntityService, CompanyArticleEntityRepository caEntityRepository, CompanyArticleCompanyEntityRepository cacEntityRepository, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository) {
        this.caEntityService = caEntityService;
        this.caEntityRepository = caEntityRepository;
        this.cacEntityRepository = cacEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
    }

    @DisplayName("모든 기업 기사 획득")
    @Test
    void getAllTest() {
        // given
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findAll()).thenReturn(List.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getAll()).isEqualTo(List.of(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)));
    }

    @DisplayName("날짜로 기업 기사 획득")
    @Test
    void getByDateTest() {
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        LocalDate date = companyArticleEntity.getDate();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByDate(date)).thenReturn(List.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByDate(date)).isEqualTo(List.of(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)));
    }

    @DisplayName("날짜 범위로 기업 기사 획득")
    @Test
    void getByDateRangeTest() {
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        LocalDate date = companyArticleEntity.getDate();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByDateBetween(date, date)).thenReturn(List.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByDateRange(date, date)).isEqualTo(List.of(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)));
    }

    @DisplayName("대상 국가로 기업 기사 획득")
    @Test
    void getBySubjectCountryTest() {
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        String subjectCountry = companyArticleEntity.getSubjectCountry();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findBySubjectCountry(subjectCountry)).thenReturn(List.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getBySubjectCountry(Country.valueOf(subjectCountry))).isEqualTo(List.of(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)));
    }

    @DisplayName("중요도로 기업 기사 획득")
    @Test
    void getByImportanceTest() {
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        String importance = companyArticleEntity.getImportance();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByImportance(importance)).thenReturn(List.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByImportance(Importance.valueOf(importance))).isEqualTo(List.of(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)));
    }

    @DisplayName("언론사로 기업 기사 획득")
    @Test
    void getByPressTest() {
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        PressEntity pressEntity = createNumberedPressEntity();
        companyArticleEntity.updatePress(pressEntity);
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByPress(pressEntity)).thenReturn(List.of(companyArticleEntity));
        when(pressEntityRepository.findByNumber(pressEntity.getNumber())).thenReturn(Optional.of(pressEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByPress(pressEntityMapper.toPress(pressEntity))).isEqualTo(List.of(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository)));
    }

    @DisplayName("번호로 기업 기사 획득")
    @Test
    void getByNumberTest() {
        // given
        CompanyArticleEntity companyArticleEntity = createNumberedCompanyArticleEntity();
        Long number = companyArticleEntity.getNumber();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByNumber(number)).thenReturn(Optional.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByNumber(number).orElseThrow()).isEqualTo(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository));
    }

    @DisplayName("이름으로 기업 기사 획득")
    @Test
    void getByNameTest() {
        // given
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        String name = companyArticleEntity.getName();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByName(name)).thenReturn(Optional.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByName(name).orElseThrow()).isEqualTo(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository));
    }

    @DisplayName("링크로 기업 기사 획득")
    @Test
    void getByLinkTest() {
        // given
        CompanyArticleEntity companyArticleEntity = createCompanyArticleEntity();
        String link = companyArticleEntity.getLink();
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findByLink(link)).thenReturn(Optional.of(companyArticleEntity));

        // when
        caEntityRepository.save(companyArticleEntity);

        // then
        assertThat(caEntityService.getByLink(link).orElseThrow()).isEqualTo(caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository));
    }

    @DisplayName("기업 기사 삽입")
    @Test
    void insertTest() {
        // given
        CompanyArticleEntity companyArticleEntity = createNumberedCompanyArticleEntity();
        ArticleEntity articleEntity = companyArticleEntity.getArticle();
        CompanyArticle companyArticle = caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(caEntityRepository.existsByNumber(companyArticle.getNumber())).thenReturn(false);
        when(caEntityRepository.findByName(companyArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        when(caEntityRepository.findAll()).thenReturn(List.of(companyArticleEntity));
        articleEntityRepository.save(articleEntity);

        // when
        caEntityService.insert(companyArticle);

        // then
        assertThat(caEntityService.getAll()).isEqualTo(List.of(companyArticle));
    }

    @DisplayName("이미 존재하는 번호로 기업 기사 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        CompanyArticleEntity companyArticleEntity = createNumberedCompanyArticleEntity();
        ArticleEntity articleEntity = companyArticleEntity.getArticle();
        CompanyArticle companyArticle = caEntityMapper.toCompanyArticle(companyArticleEntity, cacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(caEntityRepository.existsByNumber(companyArticle.getNumber())).thenReturn(false).thenReturn(true);
        when(caEntityRepository.findByName(companyArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(caEntityRepository.save(companyArticleEntity)).thenReturn(companyArticleEntity);
        articleEntityRepository.save(articleEntity);

        // when
        caEntityService.insert(companyArticle);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> caEntityService.insert(CompanyArticle.builder()
                        .companyArticle(anotherCompanyArticle).number(companyArticle.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, companyArticle.getNumber(), CompanyArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 기업 기사 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        CompanyArticleEntity caEntity = createNumberedCompanyArticleEntity();
        CompanyArticleEntity caEntityExistedEnglishName = CompanyArticleEntity.builder()
                .companyArticle(createAnotherCompanyArticleEntity()).article(caEntity.getArticle()).name(caEntity.getName()).build();
        ArticleEntity articleEntity = caEntity.getArticle();
        Long number = articleEntity.getNumber();
        CompanyArticle companyArticle = caEntityMapper.toCompanyArticle(caEntity, cacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(caEntityRepository.existsByNumber(number)).thenReturn(false);
        when(caEntityRepository.findByName(companyArticle.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(caEntity));
        when(articleEntityRepository.findByNumber(number)).thenReturn(Optional.of(articleEntity));
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        articleEntityRepository.save(articleEntity);

        // when
        caEntityService.insert(companyArticle);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> caEntityService.insert(caEntityMapper.toCompanyArticle(caEntityExistedEnglishName, cacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, caEntity.getName(), CompanyArticleEntity.class));
    }

    @DisplayName("기업 기사 갱신")
    @Test
    void updateTest() {
        // given
        CompanyArticleEntity caEntity = createNumberedCompanyArticleEntity();
        ArticleEntity articleEntity = caEntity.getArticle();
        Long articleNumber = articleEntity.getNumber();
        Long pressNumber = caEntity.getPress().getNumber();
        CompanyArticle caUpdated = CompanyArticle.builder().companyArticle(anotherCompanyArticle).number(articleNumber).pressNumber(pressNumber).mappedCompanyCodes(Collections.emptyList()).build();
        when(articleEntityRepository.findByNumber(articleNumber)).thenReturn(Optional.of(articleEntity));
        when(pressEntityRepository.findByNumber(pressNumber)).thenReturn(Optional.of(caEntity.getPress()));
        CompanyArticleEntity caEntityUpdated = caEntityMapper.toCompanyArticleEntity(caUpdated, articleEntityRepository, pressEntityRepository);
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(caEntityRepository.save(caEntityUpdated)).thenReturn(caEntityUpdated);
        when(caEntityRepository.existsByNumber(articleNumber)).thenReturn(true);
        when(caEntityRepository.findByName(caEntityUpdated.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(cacEntityRepository.findByCompanyArticle(caEntityUpdated)).thenReturn(Collections.emptyList());
        when(caEntityRepository.findAll()).thenReturn(List.of(caEntityUpdated));
        caEntityRepository.save(caEntity);

        // when
        caEntityService.update(caUpdated);

        // then
        assertThat(caEntityService.getAll()).isEqualTo(List.of(caUpdated));
    }

    @DisplayName("발견되지 않는 번호로 기업 기사 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        CompanyArticleEntity caEntity = createNumberedCompanyArticleEntity();
        Long number = caEntity.getArticle().getNumber();

        // when
        when(caEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                caEntityService.update(caEntityMapper.toCompanyArticle(caEntity, cacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, CompanyArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 기업 기사 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        CompanyArticleEntity caEntity = createCompanyArticleEntity();
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(caEntityRepository.existsByNumber(any())).thenReturn(true);
        when(caEntityRepository.findByName(caEntity.getName())).thenReturn(Optional.of(caEntity));

        // when
        caEntityRepository.save(caEntity);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class, () ->
                caEntityService.update(caEntityMapper.toCompanyArticle(caEntity, cacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, caEntity.getName(), CompanyArticleEntity.class));
    }

    @DisplayName("번호로 기업 기사 제거")
    @Test
    void removeByNumberTest() {
        // given
        CompanyArticleEntity caEntity = createNumberedCompanyArticleEntity();
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(caEntityRepository.existsByNumber(caEntity.getNumber())).thenReturn(true);
        when(caEntityRepository.findByNumber(caEntity.getNumber())).thenReturn(Optional.of(caEntity));
        when(cacEntityRepository.findByCompanyArticle(caEntity)).thenReturn(Collections.emptyList());
        doNothing().when(caEntityRepository).deleteByNumber(caEntity.getNumber());
        when(caEntityRepository.findAll()).thenReturn(Collections.emptyList());
        caEntityRepository.save(caEntity);

        // when
        caEntityService.removeByNumber(caEntity.getNumber());

        // then
        assertThat(caEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 기업 기사 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        CompanyArticleEntity caEntity = createNumberedCompanyArticleEntity();
        Long number = caEntity.getNumber();

        // when
        when(caEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> caEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, CompanyArticleEntity.class));
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼에 포함된 기업 기사 제거")
    @Test
    void removeByNumberInMapperTest() {
        // given
        CompanyArticleEntity caEntity = createNumberedCompanyArticleEntity();
        Long number = caEntity.getNumber();
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(caEntityRepository.existsByNumber(caEntity.getNumber())).thenReturn(true);
        when(caEntityRepository.findByNumber(caEntity.getNumber())).thenReturn(Optional.of(caEntity));
        when(cacEntityRepository.findByCompanyArticle(caEntity)).thenReturn(List.of(createCompanyArticleCompanyEntity()));

        // when
        caEntityRepository.save(caEntity);

        // then
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> caEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, CompanyArticleEntity.class));
    }
}