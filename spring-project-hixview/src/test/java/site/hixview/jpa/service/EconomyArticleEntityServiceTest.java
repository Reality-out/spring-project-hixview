package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.EconomyArticleEntityMapper;
import site.hixview.jpa.mapper.EconomyArticleEntityMapperImpl;
import site.hixview.jpa.mapper.PressEntityMapper;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.EconomyArticleContentEntityTestUtils;
import site.hixview.support.jpa.util.EconomyArticleEntityTestUtils;
import site.hixview.support.spring.util.EconomyArticleTestUtils;

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
class EconomyArticleEntityServiceTest implements EconomyArticleEntityTestUtils, EconomyArticleContentEntityTestUtils, EconomyArticleTestUtils {

    private final EconomyArticleEntityService iaEntityService;
    private final EconomyArticleEntityRepository iaEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;

    private final EconomyArticleEntityMapper iaEntityMapper = new EconomyArticleEntityMapperImpl();
    private final PressEntityMapper pressEntityMapper = new PressEntityMapperImpl();

    @Autowired
    EconomyArticleEntityServiceTest(EconomyArticleEntityService iaEntityService, EconomyArticleEntityRepository iaEntityRepository, EconomyArticleContentEntityRepository eacEntityRepository, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository) {
        this.iaEntityService = iaEntityService;
        this.iaEntityRepository = iaEntityRepository;
        this.eacEntityRepository = eacEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
    }

    @DisplayName("모든 경제 기사 획득")
    @Test
    void getAllTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findAll()).thenReturn(List.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getAll()).isEqualTo(List.of(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("날짜로 경제 기사 획득")
    @Test
    void getByDateTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        LocalDate date = economyArticleEntity.getDate();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByDate(date)).thenReturn(List.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByDate(date)).isEqualTo(List.of(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("날짜 범위로 경제 기사 획득")
    @Test
    void getByDateRangeTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        LocalDate date = economyArticleEntity.getDate();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByDateBetween(date, date)).thenReturn(List.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByDateRange(date, date)).isEqualTo(List.of(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("대상 국가로 경제 기사 획득")
    @Test
    void getBySubjectCountryTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String subjectCountry = economyArticleEntity.getSubjectCountry();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findBySubjectCountry(subjectCountry)).thenReturn(List.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getBySubjectCountry(Country.valueOf(subjectCountry))).isEqualTo(List.of(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("중요도로 경제 기사 획득")
    @Test
    void getByImportanceTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String importance = economyArticleEntity.getImportance();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByImportance(importance)).thenReturn(List.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByImportance(Importance.valueOf(importance))).isEqualTo(List.of(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("언론사로 경제 기사 획득")
    @Test
    void getByPressTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        PressEntity pressEntity = createNumberedPressEntity();
        economyArticleEntity.updatePress(pressEntity);
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByPress(pressEntity)).thenReturn(List.of(economyArticleEntity));
        when(pressEntityRepository.findByNumber(pressEntity.getNumber())).thenReturn(Optional.of(pressEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByPress(pressEntityMapper.toPress(pressEntity))).isEqualTo(List.of(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("번호로 경제 기사 획득")
    @Test
    void getByNumberTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createNumberedEconomyArticleEntity();
        Long number = economyArticleEntity.getNumber();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByNumber(number)).thenReturn(Optional.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByNumber(number).orElseThrow()).isEqualTo(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository));
    }

    @DisplayName("이름으로 경제 기사 획득")
    @Test
    void getByNameTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String name = economyArticleEntity.getName();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByName(name)).thenReturn(Optional.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByName(name).orElseThrow()).isEqualTo(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository));
    }

    @DisplayName("링크로 경제 기사 획득")
    @Test
    void getByLinkTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String link = economyArticleEntity.getLink();
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findByLink(link)).thenReturn(Optional.of(economyArticleEntity));

        // when
        iaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(iaEntityService.getByLink(link).orElseThrow()).isEqualTo(iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository));
    }

    @DisplayName("경제 기사 삽입")
    @Test
    void insertTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createNumberedEconomyArticleEntity();
        ArticleEntity articleEntity = economyArticleEntity.getArticle();
        EconomyArticle economyArticle = iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(iaEntityRepository.existsByNumber(economyArticle.getNumber())).thenReturn(false);
        when(iaEntityRepository.findByName(economyArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(iaEntityRepository.findAll()).thenReturn(List.of(economyArticleEntity));
        articleEntityRepository.save(articleEntity);

        // when
        iaEntityService.insert(economyArticle);

        // then
        assertThat(iaEntityService.getAll()).isEqualTo(List.of(economyArticle));
    }

    @DisplayName("이미 존재하는 번호로 경제 기사 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createNumberedEconomyArticleEntity();
        ArticleEntity articleEntity = economyArticleEntity.getArticle();
        EconomyArticle economyArticle = iaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(iaEntityRepository.existsByNumber(economyArticle.getNumber())).thenReturn(false).thenReturn(true);
        when(iaEntityRepository.findByName(economyArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(iaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        articleEntityRepository.save(articleEntity);

        // when
        iaEntityService.insert(economyArticle);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> iaEntityService.insert(EconomyArticle.builder()
                        .economyArticle(anotherEconomyArticle).number(economyArticle.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, economyArticle.getNumber(), EconomyArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 기사 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        EconomyArticleEntity iaEntity = createNumberedEconomyArticleEntity();
        EconomyArticleEntity iaEntityExistedEnglishName = EconomyArticleEntity.builder()
                .economyArticle(createAnotherEconomyArticleEntity()).article(iaEntity.getArticle()).name(iaEntity.getName()).build();
        ArticleEntity articleEntity = iaEntity.getArticle();
        Long number = articleEntity.getNumber();
        EconomyArticle economyArticle = iaEntityMapper.toEconomyArticle(iaEntity, eacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(iaEntityRepository.existsByNumber(number)).thenReturn(false);
        when(iaEntityRepository.findByName(economyArticle.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(iaEntity));
        when(articleEntityRepository.findByNumber(number)).thenReturn(Optional.of(articleEntity));
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        articleEntityRepository.save(articleEntity);

        // when
        iaEntityService.insert(economyArticle);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> iaEntityService.insert(iaEntityMapper.toEconomyArticle(iaEntityExistedEnglishName, eacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, iaEntity.getName(), EconomyArticleEntity.class));
    }

    @DisplayName("경제 기사 갱신")
    @Test
    void updateTest() {
        // given
        EconomyArticleEntity iaEntity = createNumberedEconomyArticleEntity();
        ArticleEntity articleEntity = iaEntity.getArticle();
        Long articleNumber = articleEntity.getNumber();
        Long pressNumber = iaEntity.getPress().getNumber();
        EconomyArticle iaUpdated = EconomyArticle.builder().economyArticle(anotherEconomyArticle).number(articleNumber).pressNumber(pressNumber).mappedEconomyContentNumbers(Collections.emptyList()).build();
        when(articleEntityRepository.findByNumber(articleNumber)).thenReturn(Optional.of(articleEntity));
        when(pressEntityRepository.findByNumber(pressNumber)).thenReturn(Optional.of(iaEntity.getPress()));
        EconomyArticleEntity iaEntityUpdated = iaEntityMapper.toEconomyArticleEntity(iaUpdated, articleEntityRepository, pressEntityRepository);
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.save(iaEntityUpdated)).thenReturn(iaEntityUpdated);
        when(iaEntityRepository.existsByNumber(articleNumber)).thenReturn(true);
        when(iaEntityRepository.findByName(iaEntityUpdated.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(eacEntityRepository.findByEconomyArticle(iaEntityUpdated)).thenReturn(Collections.emptyList());
        when(iaEntityRepository.findAll()).thenReturn(List.of(iaEntityUpdated));
        iaEntityRepository.save(iaEntity);

        // when
        iaEntityService.update(iaUpdated);

        // then
        assertThat(iaEntityService.getAll()).isEqualTo(List.of(iaUpdated));
    }

    @DisplayName("발견되지 않는 번호로 경제 기사 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        EconomyArticleEntity iaEntity = createNumberedEconomyArticleEntity();
        Long number = iaEntity.getArticle().getNumber();

        // when
        when(iaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                iaEntityService.update(iaEntityMapper.toEconomyArticle(iaEntity, eacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 기사 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        EconomyArticleEntity iaEntity = createEconomyArticleEntity();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.existsByNumber(any())).thenReturn(true);
        when(iaEntityRepository.findByName(iaEntity.getName())).thenReturn(Optional.of(iaEntity));

        // when
        iaEntityRepository.save(iaEntity);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class, () ->
                iaEntityService.update(iaEntityMapper.toEconomyArticle(iaEntity, eacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, iaEntity.getName(), EconomyArticleEntity.class));
    }

    @DisplayName("번호로 경제 기사 제거")
    @Test
    void removeByNumberTest() {
        // given
        EconomyArticleEntity iaEntity = createNumberedEconomyArticleEntity();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.existsByNumber(iaEntity.getNumber())).thenReturn(true);
        when(iaEntityRepository.findByNumber(iaEntity.getNumber())).thenReturn(Optional.of(iaEntity));
        when(eacEntityRepository.findByEconomyArticle(iaEntity)).thenReturn(Collections.emptyList());
        doNothing().when(iaEntityRepository).deleteByNumber(iaEntity.getNumber());
        when(iaEntityRepository.findAll()).thenReturn(Collections.emptyList());
        iaEntityRepository.save(iaEntity);

        // when
        iaEntityService.removeByNumber(iaEntity.getNumber());

        // then
        assertThat(iaEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 경제 기사 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        EconomyArticleEntity iaEntity = createNumberedEconomyArticleEntity();
        Long number = iaEntity.getNumber();

        // when
        when(iaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> iaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyArticleEntity.class));
    }

    @DisplayName("번호로 경제 기사와 기업 간 매퍼에 포함된 경제 기사 제거")
    @Test
    void removeByNumberInMapperTest() {
        // given
        EconomyArticleEntity iaEntity = createNumberedEconomyArticleEntity();
        Long number = iaEntity.getNumber();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(iaEntityRepository.existsByNumber(number)).thenReturn(true);
        when(iaEntityRepository.findByNumber(number)).thenReturn(Optional.of(iaEntity));
        when(eacEntityRepository.findByEconomyArticle(iaEntity)).thenReturn(List.of(createEconomyArticleContentEntity()));

        // when
        iaEntityRepository.save(iaEntity);

        // then
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> iaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, EconomyArticleEntity.class));
    }
}