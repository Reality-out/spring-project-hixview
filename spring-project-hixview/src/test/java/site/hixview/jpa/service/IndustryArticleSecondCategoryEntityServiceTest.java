package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.IndustryArticleSecondCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryEntityTestUtils;
import site.hixview.support.jpa.util.IndustryArticleEntityTestUtils;
import site.hixview.support.spring.util.IndustryArticleSecondCategoryTestUtils;

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
class IndustryArticleSecondCategoryEntityServiceTest implements IndustryArticleEntityTestUtils, IndustryArticleSecondCategoryEntityTestUtils, IndustryArticleSecondCategoryTestUtils {

    private final IndustryArticleSecondCategoryEntityService iascEntityService;
    private final SecondCategoryEntityRepository scEntityRepository;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;

    private final IndustryArticleSecondCategoryEntityMapper iascEntityMapper = new IndustryArticleSecondCategoryEntityMapperImpl();
    private final IndustryArticleEntityMapper iaEntityMapper = new IndustryArticleEntityMapperImpl();
    private final SecondCategoryEntityMapper scEntityMapper = new SecondCategoryEntityMapperImpl();

    @Autowired
    IndustryArticleSecondCategoryEntityServiceTest(IndustryArticleSecondCategoryEntityService iascEntityService, SecondCategoryEntityRepository scEntityRepository, IndustryArticleEntityRepository iaEntityRepository, IndustryArticleSecondCategoryEntityRepository iascEntityRepository) {
        this.iascEntityService = iascEntityService;
        this.scEntityRepository = scEntityRepository;
        this.iaEntityRepository = iaEntityRepository;
        this.iascEntityRepository = iascEntityRepository;
    }

    @DisplayName("모든 산업 기사와 2차 업종 간 매퍼 획득")
    @Test
    void getAllTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createIndustryArticleSecondCategoryEntity();
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.findAll()).thenReturn(List.of(iascEntity));

        // when
        iascEntityRepository.save(iascEntity);

        // then
        assertThat(iascEntityService.getAll()).isEqualTo(List.of(iascEntityMapper.toIndustryArticleSecondCategory(iascEntity)));
    }


    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 획득")
    @Test
    void getByNumberTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        Long number = iascEntity.getNumber();
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.findByNumber(number)).thenReturn(Optional.of(iascEntity));

        // when
        iascEntityRepository.save(iascEntity);

        // then
        assertThat(iascEntityService.getByNumber(number).orElseThrow()).isEqualTo(iascEntityMapper.toIndustryArticleSecondCategory(iascEntity));
    }

    @DisplayName("기업 기사로 산업 기사와 2차 업종 간 매퍼 획득")
    @Test
    void getByIndustryArticleTest() {
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.findByIndustryArticle(iaEntity)).thenReturn(List.of(iascEntity));
        when(iaEntityRepository.findByNumber(iaEntity.getArticle().getNumber())).thenReturn(Optional.of(iaEntity));

        // when
        iascEntityRepository.save(iascEntity);

        // then
        assertThat(iascEntityService.getByIndustryArticle(iaEntityMapper.toIndustryArticle(iaEntity, iascEntityRepository))).isEqualTo(List.of(iascEntityMapper.toIndustryArticleSecondCategory(iascEntity)));
    }

    @DisplayName("기업으로 산업 기사와 2차 업종 간 매퍼 획득")
    @Test
    void getByArticleTest() {
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        SecondCategoryEntity scEntity = iascEntity.getSecondCategory();
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.findBySecondCategory(scEntity)).thenReturn(List.of(iascEntity));
        when(scEntityRepository.findByNumber(scEntity.getNumber())).thenReturn(Optional.of(scEntity));

        // when
        iascEntityRepository.save(iascEntity);

        // then
        assertThat(iascEntityService.getBySecondCategory(scEntityMapper.toSecondCategory(scEntity))).isEqualTo(List.of(iascEntityMapper.toIndustryArticleSecondCategory(iascEntity)));
    }

    @DisplayName("산업 기사와 2차 업종 간 매퍼 삽입")
    @Test
    void insertTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        SecondCategoryEntity scEntity = iascEntity.getSecondCategory();
        IndustryArticleSecondCategory industryArticleSecondCategory = iascEntityMapper.toIndustryArticleSecondCategory(iascEntity);
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(iascEntityRepository.existsByNumber(industryArticleSecondCategory.getNumber())).thenReturn(false);
        when(iascEntityRepository.findByIndustryArticleAndSecondCategory(iascEntity.getIndustryArticle(), scEntity)).thenReturn(Optional.empty());
        when(iaEntityRepository.findByNumber(industryArticleSecondCategory.getArticleNumber())).thenReturn(Optional.of(iaEntity));
        when(scEntityRepository.findByNumber(industryArticleSecondCategory.getSecondCategoryNumber())).thenReturn(Optional.of(scEntity));
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.findAll()).thenReturn(List.of(iascEntity));
        iaEntityRepository.save(iaEntity);
        scEntityRepository.save(scEntity);

        // when
        iascEntityService.insert(industryArticleSecondCategory);

        // then
        assertThat(iascEntityService.getAll()).isEqualTo(List.of(industryArticleSecondCategory));
    }

    @DisplayName("이미 존재하는 번호로 산업 기사와 2차 업종 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        SecondCategoryEntity scEntity = iascEntity.getSecondCategory();
        IndustryArticleSecondCategory industryArticleSecondCategory = iascEntityMapper.toIndustryArticleSecondCategory(iascEntity);
        Long number = industryArticleSecondCategory.getNumber();
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.existsByNumber(number)).thenReturn(true);

        // when
        iaEntityRepository.save(iaEntity);
        scEntityRepository.save(scEntity);
        iascEntityRepository.save(iascEntity);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> iascEntityService.insert(IndustryArticleSecondCategory.builder()
                        .industryArticleSecondCategory(anotherIndustryArticleSecondCategory).number(number).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, IndustryArticleSecondCategoryEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 산업 기사와 2차 업종 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberPairTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        SecondCategoryEntity scEntity = iascEntity.getSecondCategory();
        IndustryArticleSecondCategory industryArticleSecondCategory = IndustryArticleSecondCategory.builder().industryArticleSecondCategory(iascEntityMapper.toIndustryArticleSecondCategory(iascEntity)).articleNumber(iaEntity.getArticle().getNumber()).build();
        IndustryArticleSecondCategoryEntity iascEntityExistedNumberPair = new IndustryArticleSecondCategoryEntity(anotherIndustryArticleSecondCategory.getNumber(), iaEntity, scEntity);
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.existsByNumber(industryArticleSecondCategory.getNumber())).thenReturn(false);
        when(iascEntityRepository.findByIndustryArticleAndSecondCategory(iaEntity, scEntity)).thenReturn(Optional.of(iascEntityExistedNumberPair));
        when(iaEntityRepository.findByNumber(industryArticleSecondCategory.getArticleNumber())).thenReturn(Optional.of(iaEntity));
        when(scEntityRepository.findByNumber(industryArticleSecondCategory.getSecondCategoryNumber())).thenReturn(Optional.of(scEntity));

        // when
        iaEntityRepository.save(iaEntity);
        scEntityRepository.save(scEntity);
        iascEntityRepository.save(iascEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> iascEntityService.insert(IndustryArticleSecondCategory.builder().
                        industryArticleSecondCategory(iascEntityMapper.toIndustryArticleSecondCategory(iascEntityExistedNumberPair)).
                        articleNumber(iaEntity.getArticle().getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, iaEntity.getArticle().getNumber(), IndustryArticleEntity.class,
                SECOND_CATEGORY_NUMBER, scEntity.getNumber(), SecondCategoryEntity.class));
    }

    @DisplayName("산업 기사와 2차 업종 간 매퍼 갱신")
    @Test
    void updateTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        SecondCategoryEntity scEntity = iascEntity.getSecondCategory();
        IndustryArticleEntity iaEntityUpdated = createAnotherIndustryArticleEntity();
        ArticleEntity articleEntityUpdated = createAnotherArticleEntity();
        articleEntityUpdated.updateNumber(anotherArticle.getNumber());
        iaEntityUpdated.updateArticle(articleEntityUpdated);
        SecondCategoryEntity scEntityUpdated = createAnotherSecondCategoryEntity();

        IndustryArticleSecondCategory iascUpdated = IndustryArticleSecondCategory.builder().number(iascEntity.getNumber()).articleNumber(articleEntityUpdated.getNumber()).secondCategoryNumber(scEntityUpdated.getNumber()).build();
        when(iaEntityRepository.findByNumber(iaEntityUpdated.getArticle().getNumber())).thenReturn(Optional.of(iaEntityUpdated));
        when(scEntityRepository.findByNumber(scEntityUpdated.getNumber())).thenReturn(Optional.of(scEntityUpdated));
        IndustryArticleSecondCategoryEntity iascEntityUpdated = iascEntityMapper.toIndustryArticleSecondCategoryEntity(iascUpdated, iaEntityRepository, scEntityRepository);
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity).thenReturn(iascEntityUpdated);
        when(iaEntityRepository.save(iaEntityUpdated)).thenReturn(iaEntityUpdated);
        when(scEntityRepository.save(scEntityUpdated)).thenReturn(scEntityUpdated);
        when(iascEntityRepository.existsByNumber(iascUpdated.getNumber())).thenReturn(true);
        when(iascEntityRepository.findByIndustryArticleAndSecondCategory(iaEntityUpdated, scEntityUpdated)).thenReturn(Optional.empty());
        when(iascEntityRepository.findByNumber(iascEntityUpdated.getNumber())).thenReturn(Optional.of(iascEntityUpdated));
        when(iaEntityRepository.findByNumber(iascUpdated.getArticleNumber())).thenReturn(Optional.of(iaEntityUpdated));
        when(scEntityRepository.findByNumber(iascUpdated.getSecondCategoryNumber())).thenReturn(Optional.of(scEntityUpdated));
        when(iascEntityRepository.findAll()).thenReturn(List.of(iascEntityUpdated));

        iaEntityRepository.save(iaEntity);
        scEntityRepository.save(scEntity);
        iascEntityRepository.save(iascEntity);
        iaEntityRepository.save(iaEntityUpdated);
        scEntityRepository.save(scEntityUpdated);

        // when
        iascEntityService.update(iascUpdated);

        // then
        assertThat(iascEntityService.getAll()).isEqualTo(List.of(IndustryArticleSecondCategory.builder().industryArticleSecondCategory(iascUpdated).articleNumber(null).build()));
    }

    @DisplayName("발견되지 않는 번호로 산업 기사와 2차 업종 간 매퍼 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        Long number = iascEntity.getNumber();

        // when
        when(iascEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                iascEntityService.update(iascEntityMapper.toIndustryArticleSecondCategory(iascEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, IndustryArticleSecondCategoryEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 산업 기사와 2차 업종 간 매퍼 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        SecondCategoryEntity scEntity = iascEntity.getSecondCategory();

        IndustryArticleSecondCategory iascUpdated = IndustryArticleSecondCategory.builder().number(iascEntity.getNumber()).articleNumber(iaEntity.getArticle().getNumber()).secondCategoryNumber(scEntity.getNumber()).build();
        when(iaEntityRepository.findByNumber(iaEntity.getArticle().getNumber())).thenReturn(Optional.of(iaEntity));
        when(scEntityRepository.findByNumber(scEntity.getNumber())).thenReturn(Optional.of(scEntity));
        IndustryArticleSecondCategoryEntity iascEntityUpdated = iascEntityMapper.toIndustryArticleSecondCategoryEntity(iascUpdated, iaEntityRepository, scEntityRepository);
        when(iaEntityRepository.save(iaEntity)).thenReturn(iaEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity).thenReturn(iascEntityUpdated);
        when(iascEntityRepository.existsByNumber(iascUpdated.getNumber())).thenReturn(true);
        when(iascEntityRepository.findByIndustryArticleAndSecondCategory(iaEntity, scEntity)).thenReturn(Optional.of(iascEntity));
        when(iascEntityRepository.findByNumber(iascEntityUpdated.getNumber())).thenReturn(Optional.of(iascEntityUpdated));
        when(iaEntityRepository.findByNumber(iascUpdated.getArticleNumber())).thenReturn(Optional.of(iaEntity));
        when(scEntityRepository.findByNumber(iascUpdated.getSecondCategoryNumber())).thenReturn(Optional.of(scEntity));

        // when
        iaEntityRepository.save(iaEntity);
        scEntityRepository.save(scEntity);
        iascEntityRepository.save(iascEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                iascEntityService.update(iascEntityService.update(iascUpdated)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, iaEntity.getArticle().getNumber(), IndustryArticleEntity.class,
                SECOND_CATEGORY_NUMBER, scEntity.getNumber(), SecondCategoryEntity.class));
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 제거")
    @Test
    void removeByNumberTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        when(iascEntityRepository.save(iascEntity)).thenReturn(iascEntity);
        when(iascEntityRepository.existsByNumber(iascEntity.getNumber())).thenReturn(true);
        doNothing().when(iascEntityRepository).deleteByNumber(iascEntity.getNumber());
        when(iascEntityRepository.findAll()).thenReturn(Collections.emptyList());
        iascEntityRepository.save(iascEntity);

        // when
        iascEntityService.removeByNumber(iascEntity.getNumber());

        // then
        assertThat(iascEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 산업 기사와 2차 업종 간 매퍼 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        IndustryArticleSecondCategoryEntity iascEntity = createNumberedIndustryArticleSecondCategoryEntity();
        Long number = iascEntity.getNumber();

        // when
        when(iascEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> iascEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, IndustryArticleSecondCategoryEntity.class));
    }
}