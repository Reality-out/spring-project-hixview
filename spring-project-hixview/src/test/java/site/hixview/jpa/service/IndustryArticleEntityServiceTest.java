package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryEntityTestUtils;
import site.hixview.support.jpa.util.IndustryArticleEntityTestUtils;
import site.hixview.support.spring.util.IndustryArticleTestUtils;

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
class IndustryArticleEntityServiceTest implements IndustryArticleEntityTestUtils, IndustryArticleSecondCategoryEntityTestUtils, IndustryArticleTestUtils {

    private final IndustryArticleEntityService iaEntityService;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final FirstCategoryEntityRepository fcEntityRepository;

    private final IndustryArticleEntityMapper iaEntityMapper = new IndustryArticleEntityMapperImpl();
    private final PressEntityMapper pressEntityMapper = new PressEntityMapperImpl();
    private final FirstCategoryEntityMapper fcEntityMapper = new FirstCategoryEntityMapperImpl();

    @Autowired
    IndustryArticleEntityServiceTest(IndustryArticleEntityService iaEntityService, IndustryArticleEntityRepository iaEntityRepository, IndustryArticleSecondCategoryEntityRepository iascEntityRepository, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository, FirstCategoryEntityRepository fcEntityRepository) {
        this.iaEntityService = iaEntityService;
        this.iaEntityRepository = iaEntityRepository;
        this.iascEntityRepository = iascEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
        this.fcEntityRepository = fcEntityRepository;
    }

    @DisplayName("모든 산업 기사 획득")
    @Test
    void getAllTest() {
        // given
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findAll()).thenReturn(List.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getAll()).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("날짜로 산업 기사 획득")
    @Test
    void getByDateTest() {
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        LocalDate date = industryArticleEntity.getDate();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByDate(date)).thenReturn(List.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByDate(date)).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("날짜 범위로 산업 기사 획득")
    @Test
    void getByDateRangeTest() {
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        LocalDate date = industryArticleEntity.getDate();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByDateBetween(date, date)).thenReturn(List.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByDateRange(date, date)).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("대상 국가로 산업 기사 획득")
    @Test
    void getBySubjectCountryTest() {
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        String subjectCountry = industryArticleEntity.getSubjectCountry();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findBySubjectCountry(subjectCountry)).thenReturn(List.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getBySubjectCountry(Country.valueOf(subjectCountry))).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("중요도로 산업 기사 획득")
    @Test
    void getByImportanceTest() {
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        String importance = industryArticleEntity.getImportance();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByImportance(importance)).thenReturn(List.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByImportance(Importance.valueOf(importance))).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("언론사로 산업 기사 획득")
    @Test
    void getByPressTest() {
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        PressEntity pressEntity = createNumberedPressEntity();
        industryArticleEntity.updatePress(pressEntity);
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByPress(pressEntity)).thenReturn(List.of(industryArticleEntity));
        when(pressEntityRepository.findByNumber(pressEntity.getNumber())).thenReturn(Optional.of(pressEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByPress(pressEntityMapper.toPress(pressEntity))).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("1차 업종으로 산업 기사 획득")
    @Test
    void getByFirstCategoryTest() {
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        industryArticleEntity.updateFirstCategory(firstCategoryEntity);
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByFirstCategory(firstCategoryEntity)).thenReturn(List.of(industryArticleEntity));
        when(fcEntityRepository.findByNumber(firstCategoryEntity.getNumber())).thenReturn(Optional.of(firstCategoryEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByFirstCategory(fcEntityMapper.toFirstCategory(firstCategoryEntity))).isEqualTo(List.of(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository)));
    }

    @DisplayName("번호로 산업 기사 획득")
    @Test
    void getByNumberTest() {
        // given
        IndustryArticleEntity industryArticleEntity = createNumberedIndustryArticleEntity();
        Long number = industryArticleEntity.getNumber();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByNumber(number)).thenReturn(Optional.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByNumber(number).orElseThrow()).isEqualTo(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository));
    }

    @DisplayName("이름으로 산업 기사 획득")
    @Test
    void getByNameTest() {
        // given
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        String name = industryArticleEntity.getName();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByName(name)).thenReturn(Optional.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByName(name).orElseThrow()).isEqualTo(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository));
    }

    @DisplayName("링크로 산업 기사 획득")
    @Test
    void getByLinkTest() {
        // given
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        String link = industryArticleEntity.getLink();
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findByLink(link)).thenReturn(Optional.of(industryArticleEntity));

        // when
        iaEntityRepository.save(industryArticleEntity);

        // then
        assertThat(iaEntityService.getByLink(link).orElseThrow()).isEqualTo(iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository));
    }

    @DisplayName("산업 기사 삽입")
    @Test
    void insertTest() {
        // given
        IndustryArticleEntity industryArticleEntity = createNumberedIndustryArticleEntity();
        ArticleEntity articleEntity = industryArticleEntity.getArticle();
        IndustryArticle industryArticle = iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(iaEntityRepository.existsByNumber(industryArticle.getNumber())).thenReturn(false);
        when(iaEntityRepository.findByName(industryArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        when(iaEntityRepository.findAll()).thenReturn(List.of(industryArticleEntity));
        articleEntityRepository.save(articleEntity);

        // when
        iaEntityService.insert(industryArticle);

        // then
        assertThat(iaEntityService.getAll()).isEqualTo(List.of(industryArticle));
    }

    @DisplayName("이미 존재하는 번호로 산업 기사 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        IndustryArticleEntity industryArticleEntity = createNumberedIndustryArticleEntity();
        ArticleEntity articleEntity = industryArticleEntity.getArticle();
        IndustryArticle industryArticle = iaEntityMapper.toIndustryArticle(industryArticleEntity, iascEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(iaEntityRepository.existsByNumber(industryArticle.getNumber())).thenReturn(false).thenReturn(true);
        when(iaEntityRepository.findByName(industryArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(iaEntityRepository.save(industryArticleEntity)).thenReturn(industryArticleEntity);
        articleEntityRepository.save(articleEntity);

        // when
        iaEntityService.insert(industryArticle);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> iaEntityService.insert(IndustryArticle.builder()
                        .industryArticle(anotherIndustryArticle).number(industryArticle.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, industryArticle.getNumber(), IndustryArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 산업 기사 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        IndustryArticleEntity iaEntity = createNumberedIndustryArticleEntity();
        IndustryArticleEntity iaEntityExistedEnglishName = IndustryArticleEntity.builder()
                .industryArticle(createAnotherIndustryArticleEntity()).article(iaEntity.getArticle()).name(iaEntity.getName()).build();
        ArticleEntity articleEntity = iaEntity.getArticle();
        Long number = articleEntity.getNumber();
        IndustryArticle industryArticle = iaEntityMapper.toIndustryArticle(iaEntity, iascEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(iaEntityRepository.existsByNumber(number)).thenReturn(false);
        when(iaEntityRepository.findByName(industryArticle.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(iaEntity));
        when(articleEntityRepository.findByNumber(number)).thenReturn(Optional.of(articleEntity));
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        articleEntityRepository.save(articleEntity);

        // when
        iaEntityService.insert(industryArticle);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> iaEntityService.insert(iaEntityMapper.toIndustryArticle(iaEntityExistedEnglishName, iascEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, iaEntity.getName(), IndustryArticleEntity.class));
    }

    @DisplayName("산업 기사 갱신")
    @Test
    void updateTest() {
        // given
        IndustryArticleEntity iaEntity = createNumberedIndustryArticleEntity();
        ArticleEntity articleEntity = iaEntity.getArticle();
        Long articleNumber = articleEntity.getNumber();
        Long pressNumber = iaEntity.getPress().getNumber();
        Long firstCategoryNumber = iaEntity.getFirstCategory().getNumber();
        IndustryArticle iaUpdated = IndustryArticle.builder().industryArticle(anotherIndustryArticle).number(articleNumber).pressNumber(pressNumber).firstCategoryNumber(firstCategoryNumber).mappedSecondCategoryNumbers(Collections.emptyList()).build();
        when(articleEntityRepository.findByNumber(articleNumber)).thenReturn(Optional.of(articleEntity));
        when(pressEntityRepository.findByNumber(pressNumber)).thenReturn(Optional.of(iaEntity.getPress()));
        when(fcEntityRepository.findByNumber(firstCategoryNumber)).thenReturn(Optional.of(iaEntity.getFirstCategory()));
        IndustryArticleEntity iaEntityUpdated = iaEntityMapper.toIndustryArticleEntity(iaUpdated, articleEntityRepository, pressEntityRepository, fcEntityRepository);
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.save(iaEntityUpdated)).thenReturn(iaEntityUpdated);
        when(iaEntityRepository.existsByNumber(articleNumber)).thenReturn(true);
        when(iaEntityRepository.findByName(iaEntityUpdated.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(iascEntityRepository.findByIndustryArticle(iaEntityUpdated)).thenReturn(Collections.emptyList());
        when(iaEntityRepository.findAll()).thenReturn(List.of(iaEntityUpdated));
        iaEntityRepository.save(iaEntity);

        // when
        iaEntityService.update(iaUpdated);

        // then
        assertThat(iaEntityService.getAll()).isEqualTo(List.of(iaUpdated));
    }

    @DisplayName("발견되지 않는 번호로 산업 기사 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        IndustryArticleEntity iaEntity = createNumberedIndustryArticleEntity();
        Long number = iaEntity.getArticle().getNumber();

        // when
        when(iaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                iaEntityService.update(iaEntityMapper.toIndustryArticle(iaEntity, iascEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, IndustryArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 산업 기사 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        IndustryArticleEntity iaEntity = createIndustryArticleEntity();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.existsByNumber(any())).thenReturn(true);
        when(iaEntityRepository.findByName(iaEntity.getName())).thenReturn(Optional.of(iaEntity));

        // when
        iaEntityRepository.save(iaEntity);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class, () ->
                iaEntityService.update(iaEntityMapper.toIndustryArticle(iaEntity, iascEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, iaEntity.getName(), IndustryArticleEntity.class));
    }

    @DisplayName("번호로 산업 기사 제거")
    @Test
    void removeByNumberTest() {
        // given
        IndustryArticleEntity iaEntity = createNumberedIndustryArticleEntity();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.existsByNumber(iaEntity.getNumber())).thenReturn(true);
        when(iaEntityRepository.findByNumber(iaEntity.getNumber())).thenReturn(Optional.of(iaEntity));
        when(iascEntityRepository.findByIndustryArticle(iaEntity)).thenReturn(Collections.emptyList());
        doNothing().when(iaEntityRepository).deleteByNumber(iaEntity.getNumber());
        when(iaEntityRepository.findAll()).thenReturn(Collections.emptyList());
        iaEntityRepository.save(iaEntity);

        // when
        iaEntityService.removeByNumber(iaEntity.getNumber());

        // then
        assertThat(iaEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 산업 기사 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        IndustryArticleEntity iaEntity = createNumberedIndustryArticleEntity();
        Long number = iaEntity.getNumber();

        // when
        when(iaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> iaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, IndustryArticleEntity.class));
    }

    @DisplayName("번호로 산업 기사와 산업 간 매퍼에 포함된 산업 기사 제거")
    @Test
    void removeByNumberInMapperTest() {
        // given
        IndustryArticleEntity iaEntity = createNumberedIndustryArticleEntity();
        Long number = iaEntity.getNumber();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.existsByNumber(iaEntity.getNumber())).thenReturn(true);
        when(iaEntityRepository.findByNumber(iaEntity.getNumber())).thenReturn(Optional.of(iaEntity));
        when(iascEntityRepository.findByIndustryArticle(iaEntity)).thenReturn(List.of(createIndustryArticleSecondCategoryEntity()));

        // when
        iaEntityRepository.save(iaEntity);

        // then
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> iaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, IndustryArticleEntity.class));
    }
}