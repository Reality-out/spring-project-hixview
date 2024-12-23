package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.EconomyArticleContentEntityMapper;
import site.hixview.jpa.mapper.EconomyArticleContentEntityMapperImpl;
import site.hixview.jpa.mapper.EconomyContentEntityMapper;
import site.hixview.jpa.mapper.EconomyContentEntityMapperImpl;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.EconomyArticleContentEntityTestUtils;
import site.hixview.support.spring.util.EconomyContentTestUtils;

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
class EconomyContentEntityServiceTest implements EconomyArticleContentEntityTestUtils, EconomyContentTestUtils {

    private final EconomyContentEntityService economyContentEntityService;
    private final EconomyArticleContentEntityService eacEntityService;
    private final EconomyArticleEntityRepository economyArticleEntityRepository;
    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;

    private final EconomyArticleContentEntityMapper eacEntityMapper = new EconomyArticleContentEntityMapperImpl();
    private final EconomyContentEntityMapper economyContentEntityMapper = new EconomyContentEntityMapperImpl();

    @Autowired
    EconomyContentEntityServiceTest(EconomyContentEntityService economyContentEntityService, EconomyArticleContentEntityService eacEntityService, EconomyArticleEntityRepository economyArticleEntityRepository, EconomyContentEntityRepository economyContentEntityRepository, EconomyArticleContentEntityRepository eacEntityRepository) {
        this.economyContentEntityService = economyContentEntityService;
        this.eacEntityService = eacEntityService;
        this.economyArticleEntityRepository = economyArticleEntityRepository;
        this.economyContentEntityRepository = economyContentEntityRepository;
        this.eacEntityRepository = eacEntityRepository;
    }

    @DisplayName("모든 경제 컨텐츠 획득")
    @Test
    void getAllTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false);
        when(economyContentEntityRepository.findByName(economyContent.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);
        when(economyContentEntityRepository.findAll()).thenReturn(List.of(economyContentEntity));

        // when
        economyContentEntityService.insert(economyContent);

        // then
        assertThat(economyContentEntityService.getAll()).isEqualTo(List.of(economyContent));
    }

    @DisplayName("번호로 경제 컨텐츠 획득")
    @Test
    void getByNumberTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        Long number = economyContentEntity.getNumber();
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false);
        when(economyContentEntityRepository.findByName(economyContent.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);
        when(economyContentEntityRepository.findByNumber(number)).thenReturn(Optional.of(economyContentEntity));

        // when
        economyContentEntityService.insert(economyContent);

        // then
        assertThat(economyContentEntityService.getByNumber(number).orElseThrow()).isEqualTo(economyContent);
    }

    @DisplayName("이름으로 경제 컨텐츠 획득")
    @Test
    void getByNameTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        String name = economyContentEntity.getName();
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false);
        when(economyContentEntityRepository.findByName(name))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(economyContentEntity));
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);

        // when
        economyContentEntityService.insert(economyContent);

        // then
        assertThat(economyContentEntityService.getByName(name).orElseThrow()).isEqualTo(economyContent);
    }

    @DisplayName("경제 컨텐츠 삽입")
    @Test
    void insertTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        EconomyContentEntity anotherEconomyContentEntity = new EconomyContentEntity(
                anotherEconomyContent.getNumber(), anotherEconomyContent.getName());
        when(economyContentEntityRepository.existsByNumber(any())).thenReturn(false);
        when(economyContentEntityRepository.findByName(any())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity).thenReturn(anotherEconomyContentEntity);
        when(economyContentEntityRepository.findAll()).thenReturn(List.of(economyContentEntity, anotherEconomyContentEntity));

        // when
        economyContentEntityService.insert(economyContent);
        economyContentEntityService.insert(anotherEconomyContent);

        // then
        assertThat(economyContentEntityService.getAll()).isEqualTo(List.of(economyContent, anotherEconomyContent));
    }

    @DisplayName("이미 존재하는 번호로 경제 컨텐츠 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        Long number = economyContentEntity.getNumber();
        EconomyContentEntity economyContentEntityExistedNumber = new EconomyContentEntity(number, anotherEconomyContent.getName());
        EconomyContent economyContentExistedNumber = economyContentEntityMapper.toEconomyContent(economyContentEntityExistedNumber);
        when(economyContentEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(economyContent.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);

        // when
        economyContentEntityService.insert(economyContent);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> economyContentEntityService.insert(economyContentExistedNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, EconomyContentEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 컨텐츠 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        String name = economyContentEntity.getName();
        EconomyContentEntity economyContentEntityExistedName = new EconomyContentEntity(
                anotherEconomyContent.getNumber(), name);
        EconomyContent economyContentExistedName = economyContentEntityMapper.toEconomyContent(economyContentEntityExistedName);
        when(economyContentEntityRepository.existsByNumber(any())).thenReturn(false);
        when(economyContentEntityRepository.findByName(name)).thenReturn(Optional.empty()).thenReturn(Optional.of(economyContentEntity));
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);

        // when
        economyContentEntityService.insert(economyContent);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class,
                () -> economyContentEntityService.insert(economyContentExistedName));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, name, EconomyContentEntity.class));
    }

    @DisplayName("경제 컨텐츠 갱신")
    @Test
    void updateTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        Long number = economyContentEntity.getNumber();
        EconomyContent updatedEconomyContent = EconomyContent.builder().economyContent(anotherEconomyContent).number(number).build();
        EconomyContentEntity updatedEconomyContentEntity = economyContentEntityMapper.toEconomyContentEntity(updatedEconomyContent);
        when(economyContentEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(any())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.findByNumber(any())).thenReturn(Optional.of(economyContentEntity));
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity).thenReturn(updatedEconomyContentEntity);
        when(economyContentEntityRepository.findAll()).thenReturn(List.of(updatedEconomyContentEntity));
        economyContentEntityService.insert(economyContent);

        // when
        EconomyContent updateEconomyContent = economyContentEntityService.update(updatedEconomyContent);

        // then
        assertThat(economyContentEntityService.getAll()).isEqualTo(List.of(updateEconomyContent));
    }

    @DisplayName("존재하지 않는 번호로 경제 컨텐츠 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        EconomyContentEntity ecEntityNotFoundNumber = createAnotherEconomyContentEntity();
        Long notFoundNumber = ecEntityNotFoundNumber.getNumber();
        EconomyContent economyContentNotFoundNumber = economyContentEntityMapper.toEconomyContent(ecEntityNotFoundNumber);
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false);
        when(economyContentEntityRepository.existsByNumber(notFoundNumber)).thenReturn(false);
        when(economyContentEntityRepository.findByName(economyContent.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);

        // when
        economyContentEntityService.insert(economyContent);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> economyContentEntityService.update(economyContentNotFoundNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, notFoundNumber, EconomyContentEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 컨텐츠 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        String name = economyContentEntity.getName();
        EconomyContentEntity economyContentEntityExistedNumber = new EconomyContentEntity(
                anotherEconomyContent.getNumber(), name);
        EconomyContent economyContentExistedName = economyContentEntityMapper.toEconomyContent(economyContentEntityExistedNumber);
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(name)).thenReturn(Optional.empty())
                .thenReturn(Optional.of(economyContentEntity));
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);

        // when
        economyContentEntityService.insert(economyContent);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class,
                () -> economyContentEntityService.insert(economyContentExistedName));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, economyContent.getName(), EconomyContentEntity.class));
    }

    @DisplayName("번호로 경제 컨텐츠 제거")
    @Test
    void removeByNumberTest() {
        // given
        EconomyContentEntity economyContentEntity = createNumberedEconomyContentEntity();
        Long number = economyContentEntity.getNumber();
        when(economyContentEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(economyContentEntity.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);
        when(economyContentEntityRepository.findAll()).thenReturn(Collections.emptyList());
        when(eacEntityRepository.findByEconomyContent(any())).thenReturn(Collections.emptyList());
        when(economyContentEntityRepository.findByNumber(economyContentEntity.getNumber())).thenReturn(Optional.of(economyContentEntity));
        doNothing().when(economyContentEntityRepository).deleteByNumber(number);

        // when
        economyContentEntityService.insert(economyContent);
        economyContentEntityService.removeByNumber(number);

        // then
        assertThat(economyContentEntityService.getAll()).isEqualTo(Collections.emptyList());
    }

    @DisplayName("존재하지 않는 번호로 경제 컨텐츠 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        Long number = economyContent.getNumber();
        when(economyContentEntityRepository.existsByNumber(number)).thenReturn(false);

        // when
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> economyContentEntityService.removeByNumber(number));

        // then
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, EconomyContentEntity.class));
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼에 포함된 경제 컨텐츠 제거")
    @Test
    void removeByNumberInMapperTest() {
        EconomyArticleContentEntity eacEntity = createNumberedEconomyArticleContentEntity();
        EconomyArticleEntity economyArticleEntity = eacEntity.getEconomyArticle();
        EconomyContentEntity economyContentEntity = eacEntity.getEconomyContent();
        Long contentNumber = economyContentEntity.getNumber();
        when(eacEntityRepository.existsByNumber(eacEntity.getNumber())).thenReturn(false);
        when(economyArticleEntityRepository.findByNumber(economyArticleEntity.getNumber())).thenReturn(Optional.of(economyArticleEntity));
        when(economyContentEntityRepository.findByNumber(contentNumber)).thenReturn(Optional.of(economyContentEntity));
        when(eacEntityRepository.findByEconomyArticleAndEconomyContent(
                economyArticleEntity, economyContentEntity)).thenReturn(Optional.empty());
        when(eacEntityRepository.save(eacEntity)).thenReturn(eacEntity);
        when(economyContentEntityRepository.existsByNumber(contentNumber)).thenReturn(true);
        when(eacEntityRepository.findByEconomyContent(economyContentEntity)).thenReturn(List.of(eacEntity));

        // when
        eacEntityService.insert(eacEntityMapper.toEconomyArticleContent(eacEntity));
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> economyContentEntityService.removeByNumber(contentNumber));

        // then
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, contentNumber, EconomyContentEntity.class));
    }
}