package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.EconomyContentEntityMapperImpl;
import site.hixview.jpa.repository.EconomyContentEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.EconomyContentEntityTestUtils;
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
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@OnlyRealServiceContext
@Slf4j
class EconomyContentEntityServiceTest implements EconomyContentEntityTestUtils, EconomyContentTestUtils {

    private final EconomyContentEntityService economyContentEntityService;
    private final EconomyContentEntityRepository economyContentEntityRepository;

    private final EconomyContentEntityMapperImpl mapper = new EconomyContentEntityMapperImpl();

    @Autowired
    EconomyContentEntityServiceTest(EconomyContentEntityService economyContentEntityService, EconomyContentEntityRepository economyContentEntityRepository) {
        this.economyContentEntityService = economyContentEntityService;
        this.economyContentEntityRepository = economyContentEntityRepository;
    }

    @DisplayName("모든 경제 컨텐츠 획득")
    @Test
    void getAllTest() {
        // given
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), economyContent.getName());
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
        Long number = economyContent.getNumber();
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                number, economyContent.getName());
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
    void getByKoreanNameTest() {
        // given
        String name = economyContent.getName();
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), name);
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false);
        when(economyContentEntityRepository.findByName(economyContent.getName()))
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
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), economyContent.getName());
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
        Long number = economyContent.getNumber();
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                number, economyContent.getName());
        EconomyContentEntity economyContentEntityExistedNumber = new EconomyContentEntity(
                number, anotherEconomyContent.getName());
        EconomyContent economyContentExistedNumber = mapper.toEconomyContent(economyContentEntityExistedNumber);
        when(economyContentEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(economyContent.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);

        // when
        economyContentEntityService.insert(economyContent);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> economyContentEntityService.insert(economyContentExistedNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, economyContent.getNumber(), EconomyContentEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 경제 컨텐츠 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        String name = economyContent.getName();
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), name);
        EconomyContentEntity economyContentEntityExistedName = new EconomyContentEntity(
                anotherEconomyContent.getNumber(), name);
        EconomyContent economyContentExistedName = mapper.toEconomyContent(economyContentEntityExistedName);
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
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), economyContent.getName());
        EconomyContent updatedEconomyContent = EconomyContent.builder()
                .economyContent(anotherEconomyContent).number(economyContent.getNumber()).build();
        EconomyContentEntity updatedEconomyContentEntity = mapper.toEconomyContentEntity(updatedEconomyContent);
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(any())).thenReturn(Optional.empty());
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
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), economyContent.getName());
        Long notFoundNumber = anotherEconomyContent.getNumber();
        EconomyContentEntity economyContentEntityNotFoundNumber = new EconomyContentEntity(
                notFoundNumber, anotherEconomyContent.getName());
        EconomyContent economyContentNotFoundNumber = mapper.toEconomyContent(economyContentEntityNotFoundNumber);
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
        String name = economyContent.getName();
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), name);
        EconomyContentEntity economyContentEntityExistedNumber = new EconomyContentEntity(
                anotherEconomyContent.getNumber(), name);
        EconomyContent economyContentExistedName = mapper.toEconomyContent(economyContentEntityExistedNumber);
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber()))
                .thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(name))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(economyContentEntity));
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
        Long number = economyContent.getNumber();
        EconomyContentEntity economyContentEntity = new EconomyContentEntity(
                economyContent.getNumber(), economyContent.getName());
        when(economyContentEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(economyContentEntityRepository.findByName(economyContent.getName())).thenReturn(Optional.empty());
        when(economyContentEntityRepository.save(any())).thenReturn(economyContentEntity);
        doNothing().when(economyContentEntityRepository).deleteByNumber(number);
        when(economyContentEntityRepository.findAll()).thenReturn(Collections.emptyList());

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
        when(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).thenReturn(false);

        // when
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> economyContentEntityService.removeByNumber(economyContent.getNumber()));

        // then
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, economyContent.getNumber(), EconomyContentEntity.class));
    }
}