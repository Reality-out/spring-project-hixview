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

    private final EconomyArticleEntityService eaEntityService;
    private final EconomyArticleEntityRepository eaEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;

    private final EconomyArticleEntityMapper eaEntityMapper = new EconomyArticleEntityMapperImpl();
    private final PressEntityMapper pressEntityMapper = new PressEntityMapperImpl();

    @Autowired
    EconomyArticleEntityServiceTest(EconomyArticleEntityService eaEntityService, EconomyArticleEntityRepository eaEntityRepository, EconomyArticleContentEntityRepository eacEntityRepository, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository) {
        this.eaEntityService = eaEntityService;
        this.eaEntityRepository = eaEntityRepository;
        this.eacEntityRepository = eacEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
    }

    @DisplayName("모든 경제 기사 획득")
    @Test
    void getAllTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findAll()).thenReturn(List.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getAll()).isEqualTo(List.of(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("날짜로 경제 기사 획득")
    @Test
    void getByDateTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        LocalDate date = economyArticleEntity.getDate();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByDate(date)).thenReturn(List.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByDate(date)).isEqualTo(List.of(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("날짜 범위로 경제 기사 획득")
    @Test
    void getByDateRangeTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        LocalDate date = economyArticleEntity.getDate();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByDateBetween(date, date)).thenReturn(List.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByDateRange(date, date)).isEqualTo(List.of(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("대상 국가로 경제 기사 획득")
    @Test
    void getBySubjectCountryTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String subjectCountry = economyArticleEntity.getSubjectCountry();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findBySubjectCountry(subjectCountry)).thenReturn(List.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getBySubjectCountry(Country.valueOf(subjectCountry))).isEqualTo(List.of(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("중요도로 경제 기사 획득")
    @Test
    void getByImportanceTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String importance = economyArticleEntity.getImportance();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByImportance(importance)).thenReturn(List.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByImportance(Importance.valueOf(importance))).isEqualTo(List.of(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("언론사로 경제 기사 획득")
    @Test
    void getByPressTest() {
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        PressEntity pressEntity = createNumberedPressEntity();
        economyArticleEntity.updatePress(pressEntity);
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByPress(pressEntity)).thenReturn(List.of(economyArticleEntity));
        when(pressEntityRepository.findByNumber(pressEntity.getNumber())).thenReturn(Optional.of(pressEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByPress(pressEntityMapper.toPress(pressEntity))).isEqualTo(List.of(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository)));
    }

    @DisplayName("번호로 경제 기사 획득")
    @Test
    void getByNumberTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createNumberedEconomyArticleEntity();
        Long number = economyArticleEntity.getNumber();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByNumber(number)).thenReturn(Optional.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByNumber(number).orElseThrow()).isEqualTo(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository));
    }

    @DisplayName("이름으로 경제 기사 획득")
    @Test
    void getByNameTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String name = economyArticleEntity.getName();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByName(name)).thenReturn(Optional.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByName(name).orElseThrow()).isEqualTo(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository));
    }

    @DisplayName("링크로 경제 기사 획득")
    @Test
    void getByLinkTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createEconomyArticleEntity();
        String link = economyArticleEntity.getLink();
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findByLink(link)).thenReturn(Optional.of(economyArticleEntity));

        // when
        eaEntityRepository.save(economyArticleEntity);

        // then
        assertThat(eaEntityService.getByLink(link).orElseThrow()).isEqualTo(eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository));
    }

    @DisplayName("경제 기사 삽입")
    @Test
    void insertTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createNumberedEconomyArticleEntity();
        ArticleEntity articleEntity = economyArticleEntity.getArticle();
        EconomyArticle economyArticle = eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(eaEntityRepository.existsByNumber(economyArticle.getNumber())).thenReturn(false);
        when(eaEntityRepository.findByName(economyArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        when(eaEntityRepository.findAll()).thenReturn(List.of(economyArticleEntity));
        articleEntityRepository.save(articleEntity);

        // when
        eaEntityService.insert(economyArticle);

        // then
        assertThat(eaEntityService.getAll()).isEqualTo(List.of(economyArticle));
    }

    @DisplayName("이미 존재하는 번호로 경제 기사 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        EconomyArticleEntity economyArticleEntity = createNumberedEconomyArticleEntity();
        ArticleEntity articleEntity = economyArticleEntity.getArticle();
        EconomyArticle economyArticle = eaEntityMapper.toEconomyArticle(economyArticleEntity, eacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(eaEntityRepository.existsByNumber(economyArticle.getNumber())).thenReturn(false).thenReturn(true);
        when(eaEntityRepository.findByName(economyArticle.getName())).thenReturn(Optional.empty());
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(eaEntityRepository.save(economyArticleEntity)).thenReturn(economyArticleEntity);
        articleEntityRepository.save(articleEntity);

        // when
        eaEntityService.insert(economyArticle);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> eaEntityService.insert(EconomyArticle.builder()
                        .economyArticle(anotherEconomyArticle).number(economyArticle.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, economyArticle.getNumber(), EconomyArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 기사 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        EconomyArticleEntity eaEntity = createNumberedEconomyArticleEntity();
        EconomyArticleEntity eaEntityExistedEnglishName = EconomyArticleEntity.builder()
                .economyArticle(createAnotherEconomyArticleEntity()).article(eaEntity.getArticle()).name(eaEntity.getName()).build();
        ArticleEntity articleEntity = eaEntity.getArticle();
        Long number = articleEntity.getNumber();
        EconomyArticle economyArticle = eaEntityMapper.toEconomyArticle(eaEntity, eacEntityRepository);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(eaEntityRepository.existsByNumber(number)).thenReturn(false);
        when(eaEntityRepository.findByName(economyArticle.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(eaEntity));
        when(articleEntityRepository.findByNumber(number)).thenReturn(Optional.of(articleEntity));
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        articleEntityRepository.save(articleEntity);

        // when
        eaEntityService.insert(economyArticle);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> eaEntityService.insert(eaEntityMapper.toEconomyArticle(eaEntityExistedEnglishName, eacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, eaEntity.getName(), EconomyArticleEntity.class));
    }

    @DisplayName("경제 기사 갱신")
    @Test
    void updateTest() {
        // given
        EconomyArticleEntity eaEntity = createNumberedEconomyArticleEntity();
        ArticleEntity articleEntity = eaEntity.getArticle();
        Long articleNumber = articleEntity.getNumber();
        Long pressNumber = eaEntity.getPress().getNumber();
        EconomyArticle iaUpdated = EconomyArticle.builder().economyArticle(anotherEconomyArticle).number(articleNumber).pressNumber(pressNumber).mappedEconomyContentNumbers(Collections.emptyList()).build();
        when(articleEntityRepository.findByNumber(articleNumber)).thenReturn(Optional.of(articleEntity));
        when(pressEntityRepository.findByNumber(pressNumber)).thenReturn(Optional.of(eaEntity.getPress()));
        EconomyArticleEntity eaEntityUpdated = eaEntityMapper.toEconomyArticleEntity(iaUpdated, articleEntityRepository, pressEntityRepository);
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(eaEntityRepository.save(eaEntityUpdated)).thenReturn(eaEntityUpdated);
        when(eaEntityRepository.existsByNumber(articleNumber)).thenReturn(true);
        when(eaEntityRepository.findByName(eaEntityUpdated.getName())).thenReturn(Optional.empty());
        when(eaEntityRepository.findByNumber(articleNumber)).thenReturn(Optional.of(eaEntity));
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        when(eacEntityRepository.findByEconomyArticle(eaEntityUpdated)).thenReturn(Collections.emptyList());
        when(eaEntityRepository.findAll()).thenReturn(List.of(eaEntityUpdated));
        eaEntityRepository.save(eaEntity);

        // when
        eaEntityService.update(iaUpdated);

        // then
        assertThat(eaEntityService.getAll()).isEqualTo(List.of(iaUpdated));
    }

    @DisplayName("발견되지 않는 번호로 경제 기사 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        EconomyArticleEntity eaEntity = createNumberedEconomyArticleEntity();
        Long number = eaEntity.getArticle().getNumber();

        // when
        when(eaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                eaEntityService.update(eaEntityMapper.toEconomyArticle(eaEntity, eacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyArticleEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 기사 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        EconomyArticleEntity eaEntity = createEconomyArticleEntity();
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(eaEntityRepository.existsByNumber(any())).thenReturn(true);
        when(eaEntityRepository.findByName(eaEntity.getName())).thenReturn(Optional.of(eaEntity));

        // when
        eaEntityRepository.save(eaEntity);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class, () ->
                eaEntityService.update(eaEntityMapper.toEconomyArticle(eaEntity, eacEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, eaEntity.getName(), EconomyArticleEntity.class));
    }

    @DisplayName("번호로 경제 기사 제거")
    @Test
    void removeByNumberTest() {
        // given
        EconomyArticleEntity eaEntity = createNumberedEconomyArticleEntity();
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(eaEntityRepository.existsByNumber(eaEntity.getNumber())).thenReturn(true);
        when(eaEntityRepository.findByNumber(eaEntity.getNumber())).thenReturn(Optional.of(eaEntity));
        when(eacEntityRepository.findByEconomyArticle(eaEntity)).thenReturn(Collections.emptyList());
        doNothing().when(eaEntityRepository).deleteByNumber(eaEntity.getNumber());
        when(eaEntityRepository.findAll()).thenReturn(Collections.emptyList());
        eaEntityRepository.save(eaEntity);

        // when
        eaEntityService.removeByNumber(eaEntity.getNumber());

        // then
        assertThat(eaEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 경제 기사 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        EconomyArticleEntity eaEntity = createNumberedEconomyArticleEntity();
        Long number = eaEntity.getNumber();

        // when
        when(eaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> eaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyArticleEntity.class));
    }

    @DisplayName("번호로 경제 기사와 기업 간 매퍼에 포함된 경제 기사 제거")
    @Test
    void removeByNumberInMapperTest() {
        // given
        EconomyArticleEntity eaEntity = createNumberedEconomyArticleEntity();
        Long number = eaEntity.getNumber();
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(eaEntityRepository.existsByNumber(number)).thenReturn(true);
        when(eaEntityRepository.findByNumber(number)).thenReturn(Optional.of(eaEntity));
        when(eacEntityRepository.findByEconomyArticle(eaEntity)).thenReturn(List.of(createEconomyArticleContentEntity()));

        // when
        eaEntityRepository.save(eaEntity);

        // then
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> eaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, EconomyArticleEntity.class));
    }
}