package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.EconomyArticleContentEntityTestUtils;
import site.hixview.support.jpa.util.EconomyArticleEntityTestUtils;
import site.hixview.support.spring.util.EconomyArticleContentTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.*;

@OnlyRealServiceContext
@Slf4j
class EconomyArticleContentEntityServiceTest implements EconomyArticleEntityTestUtils, EconomyArticleContentEntityTestUtils, EconomyArticleContentTestUtils {

    private final EconomyArticleContentEntityService eacEntityService;
    private final EconomyArticleEntityRepository eaEntityRepository;
    private final EconomyContentEntityRepository ecEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;

    private final EconomyArticleContentEntityMapper eacEntityMapper = new EconomyArticleContentEntityMapperImpl();
    private final EconomyArticleEntityMapper eaEntityMapper = new EconomyArticleEntityMapperImpl();
    private final EconomyContentEntityMapper ecEntityMapper = new EconomyContentEntityMapperImpl();

    @Autowired
    EconomyArticleContentEntityServiceTest(EconomyArticleContentEntityService eacEntityService, EconomyArticleEntityRepository eaEntityRepository, EconomyContentEntityRepository ecEntityRepository, EconomyArticleContentEntityRepository eacEntityRepository) {
        this.eacEntityService = eacEntityService;
        this.eaEntityRepository = eaEntityRepository;
        this.ecEntityRepository = ecEntityRepository;
        this.eacEntityRepository = eacEntityRepository;
    }

    @DisplayName("모든 경제 기사와 컨텐츠 간 매퍼 획득")
    @Test
    void getAllTest() {
        // given
        EconomyArticleContentEntity eacEntity = createEconomyArticleContentEntity();
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.findAll()).thenReturn(List.of(eacEntity));

        // when
        eacEntityRepository.save(eacEntity);

        // then
        assertThat(eacEntityService.getAll()).isEqualTo(List.of(eacEntityMapper.toEconomyArticleContent(eacEntity)));
    }


    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 획득")
    @Test
    void getByNumberTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        Long number = eacEntity.getNumber();
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.findByNumber(number)).thenReturn(Optional.of(eacEntity));

        // when
        eacEntityRepository.save(eacEntity);

        // then
        assertThat(eacEntityService.getByNumber(number).orElseThrow()).isEqualTo(eacEntityMapper.toEconomyArticleContent(eacEntity));
    }

    @DisplayName("경제 기사로 경제 기사와 컨텐츠 간 매퍼 획득")
    @Test
    void getByEconomyArticleTest() {
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.findByEconomyArticle(eaEntity)).thenReturn(List.of(eacEntity));
        when(eaEntityRepository.findByNumber(eaEntity.getArticle().getNumber())).thenReturn(Optional.of(eaEntity));

        // when
        eacEntityRepository.save(eacEntity);

        // then
        assertThat(eacEntityService.getByEconomyArticle(eaEntityMapper.toEconomyArticle(eaEntity, eacEntityRepository))).isEqualTo(List.of(eacEntityMapper.toEconomyArticleContent(eacEntity)));
    }

    @DisplayName("경제 컨텐츠로 경제 기사와 컨텐츠 간 매퍼 획득")
    @Test
    void getByArticleTest() {
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyContentEntity ecEntity = eacEntity.getEconomyContent();
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.findByEconomyContent(ecEntity)).thenReturn(List.of(eacEntity));
        when(ecEntityRepository.findByNumber(ecEntity.getNumber())).thenReturn(Optional.of(ecEntity));

        // when
        eacEntityRepository.save(eacEntity);

        // then
        assertThat(eacEntityService.getByEconomyContent(ecEntityMapper.toEconomyContent(ecEntity))).isEqualTo(List.of(eacEntityMapper.toEconomyArticleContent(eacEntity)));
    }

    @DisplayName("경제 기사와 컨텐츠 간 매퍼 삽입")
    @Test
    void insertTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        EconomyContentEntity ecEntity = eacEntity.getEconomyContent();
        EconomyArticleContent economyArticleContent = eacEntityMapper.toEconomyArticleContent(eacEntity);
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(ecEntityRepository.save(ecEntity)).thenReturn(ecEntity);
        when(eacEntityRepository.existsByNumber(economyArticleContent.getNumber())).thenReturn(false);
        when(eacEntityRepository.findByEconomyArticleAndEconomyContent(eacEntity.getEconomyArticle(), ecEntity)).thenReturn(Optional.empty());
        when(eaEntityRepository.findByNumber(economyArticleContent.getArticleNumber())).thenReturn(Optional.of(eaEntity));
        when(ecEntityRepository.findByNumber(economyArticleContent.getContentNumber())).thenReturn(Optional.of(ecEntity));
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.findAll()).thenReturn(List.of(eacEntity));
        eaEntityRepository.save(eaEntity);
        ecEntityRepository.save(ecEntity);

        // when
        eacEntityService.insert(economyArticleContent);

        // then
        assertThat(eacEntityService.getAll()).isEqualTo(List.of(economyArticleContent));
    }

    @DisplayName("이미 존재하는 번호로 경제 기사와 컨텐츠 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        EconomyContentEntity ecEntity = eacEntity.getEconomyContent();
        EconomyArticleContent economyArticleContent = eacEntityMapper.toEconomyArticleContent(eacEntity);
        Long number = economyArticleContent.getNumber();
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(ecEntityRepository.save(ecEntity)).thenReturn(ecEntity);
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.existsByNumber(number)).thenReturn(true);

        // when
        eaEntityRepository.save(eaEntity);
        ecEntityRepository.save(ecEntity);
        eacEntityRepository.save(eacEntity);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> eacEntityService.insert(EconomyArticleContent.builder()
                        .economyArticleContent(anotherEconomyArticleContent).number(number).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, EconomyArticleContentEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 경제 기사와 컨텐츠 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberPairTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        EconomyContentEntity ecEntity = eacEntity.getEconomyContent();
        EconomyArticleContent economyArticleContent = EconomyArticleContent.builder().economyArticleContent(eacEntityMapper.toEconomyArticleContent(eacEntity)).articleNumber(eaEntity.getArticle().getNumber()).build();
        EconomyArticleContentEntity eacEntityExistedNumberPair = new EconomyArticleContentEntity(anotherEconomyArticleContent.getNumber(), eaEntity, ecEntity);
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(ecEntityRepository.save(ecEntity)).thenReturn(ecEntity);
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.existsByNumber(economyArticleContent.getNumber())).thenReturn(false);
        when(eacEntityRepository.findByEconomyArticleAndEconomyContent(eaEntity, ecEntity)).thenReturn(Optional.of(eacEntityExistedNumberPair));
        when(eaEntityRepository.findByNumber(economyArticleContent.getArticleNumber())).thenReturn(Optional.of(eaEntity));
        when(ecEntityRepository.findByNumber(economyArticleContent.getContentNumber())).thenReturn(Optional.of(ecEntity));

        // when
        eaEntityRepository.save(eaEntity);
        ecEntityRepository.save(ecEntity);
        eacEntityRepository.save(eacEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> eacEntityService.insert(EconomyArticleContent.builder().
                        economyArticleContent(eacEntityMapper.toEconomyArticleContent(eacEntityExistedNumberPair)).
                        articleNumber(eaEntity.getArticle().getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, eaEntity.getArticle().getNumber(), EconomyArticleEntity.class,
                CONTENT_NUMBER, ecEntity.getNumber(), EconomyContentEntity.class));
    }

    @DisplayName("경제 기사와 컨텐츠 간 매퍼 갱신")
    @Test
    void updateTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        EconomyContentEntity ecEntity = eacEntity.getEconomyContent();
        EconomyArticleEntity eaEntityUpdated = createAnotherEconomyArticleEntity();
        ArticleEntity articleEntityUpdated = createAnotherArticleEntity();
        articleEntityUpdated.updateNumber(anotherArticle.getNumber());
        eaEntityUpdated.updateArticle(articleEntityUpdated);
        EconomyContentEntity ecEntityUpdated = createAnotherEconomyContentEntity();

        EconomyArticleContent eacUpdated = EconomyArticleContent.builder().number(eacEntity.getNumber()).articleNumber(articleEntityUpdated.getNumber()).contentNumber(ecEntityUpdated.getNumber()).build();
        when(eaEntityRepository.findByNumber(eaEntityUpdated.getArticle().getNumber())).thenReturn(Optional.of(eaEntityUpdated));
        when(ecEntityRepository.findByNumber(ecEntityUpdated.getNumber())).thenReturn(Optional.of(ecEntityUpdated));
        EconomyArticleContentEntity eacEntityUpdated = eacEntityMapper.toEconomyArticleContentEntity(eacUpdated, eaEntityRepository, ecEntityRepository);
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(ecEntityRepository.save(ecEntity)).thenReturn(ecEntity);
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity).thenReturn(eacEntityUpdated);
        when(eaEntityRepository.save(eaEntityUpdated)).thenReturn(eaEntityUpdated);
        when(ecEntityRepository.save(ecEntityUpdated)).thenReturn(ecEntityUpdated);
        when(eacEntityRepository.existsByNumber(eacUpdated.getNumber())).thenReturn(true);
        when(eacEntityRepository.findByEconomyArticleAndEconomyContent(eaEntityUpdated, ecEntityUpdated)).thenReturn(Optional.empty());
        when(eacEntityRepository.findByNumber(eacEntityUpdated.getNumber())).thenReturn(Optional.of(eacEntityUpdated));
        when(eaEntityRepository.findByNumber(eacUpdated.getArticleNumber())).thenReturn(Optional.of(eaEntityUpdated));
        when(ecEntityRepository.findByNumber(eacUpdated.getContentNumber())).thenReturn(Optional.of(ecEntityUpdated));
        when(eacEntityRepository.findAll()).thenReturn(List.of(eacEntityUpdated));

        eaEntityRepository.save(eaEntity);
        ecEntityRepository.save(ecEntity);
        eacEntityRepository.save(eacEntity);
        eaEntityRepository.save(eaEntityUpdated);
        ecEntityRepository.save(ecEntityUpdated);

        // when
        eacEntityService.update(eacUpdated);

        // then
        assertThat(eacEntityService.getAll()).isEqualTo(List.of(EconomyArticleContent.builder().economyArticleContent(eacUpdated).articleNumber(null).build()));
    }

    @DisplayName("발견되지 않는 번호로 경제 기사와 컨텐츠 간 매퍼 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        Long number = eacEntity.getNumber();

        // when
        when(eacEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                eacEntityService.update(eacEntityMapper.toEconomyArticleContent(eacEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyArticleContentEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 경제 기사와 컨텐츠 간 매퍼 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        EconomyContentEntity ecEntity = eacEntity.getEconomyContent();

        EconomyArticleContent eacUpdated = EconomyArticleContent.builder().number(eacEntity.getNumber()).articleNumber(eaEntity.getArticle().getNumber()).contentNumber(ecEntity.getNumber()).build();
        when(eaEntityRepository.findByNumber(eaEntity.getArticle().getNumber())).thenReturn(Optional.of(eaEntity));
        when(ecEntityRepository.findByNumber(ecEntity.getNumber())).thenReturn(Optional.of(ecEntity));
        EconomyArticleContentEntity eacEntityUpdated = eacEntityMapper.toEconomyArticleContentEntity(eacUpdated, eaEntityRepository, ecEntityRepository);
        when(eaEntityRepository.save(eaEntity)).thenReturn(eaEntity);
        when(ecEntityRepository.save(ecEntity)).thenReturn(ecEntity);
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity).thenReturn(eacEntityUpdated);
        when(eacEntityRepository.existsByNumber(eacUpdated.getNumber())).thenReturn(true);
        when(eacEntityRepository.findByEconomyArticleAndEconomyContent(eaEntity, ecEntity)).thenReturn(Optional.of(eacEntity));
        when(eacEntityRepository.findByNumber(eacEntityUpdated.getNumber())).thenReturn(Optional.of(eacEntityUpdated));
        when(eaEntityRepository.findByNumber(eacUpdated.getArticleNumber())).thenReturn(Optional.of(eaEntity));
        when(ecEntityRepository.findByNumber(eacUpdated.getContentNumber())).thenReturn(Optional.of(ecEntity));

        // when
        eaEntityRepository.save(eaEntity);
        ecEntityRepository.save(ecEntity);
        eacEntityRepository.save(eacEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                eacEntityService.update(eacEntityService.update(eacUpdated)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, eaEntity.getArticle().getNumber(), EconomyArticleEntity.class,
                CONTENT_NUMBER, ecEntity.getNumber(), EconomyContentEntity.class));
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 제거")
    @Test
    void removeByNumberTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(eacEntityRepository.existsByNumber(eacEntity.getNumber())).thenReturn(true);
        doNothing().when(eacEntityRepository).deleteByNumber(eacEntity.getNumber());
        when(eacEntityRepository.findAll()).thenReturn(Collections.emptyList());
        eacEntityRepository.save(eacEntity);

        // when
        eacEntityService.removeByNumber(eacEntity.getNumber());

        // then
        assertThat(eacEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 경제 기사와 컨텐츠 간 매퍼 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        Long number = eacEntity.getNumber();

        // when
        when(eacEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> eacEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyArticleContentEntity.class));
    }
}