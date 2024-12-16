package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.PressEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.PressEntityTestUtils;
import site.hixview.support.spring.util.PressTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.vo.ExceptionMessage.*;

@OnlyRealServiceContext
@Slf4j
class PressEntityServiceTest implements PressEntityTestUtils, PressTestUtils {

    private final PressEntityService pressEntityService;
    private final PressEntityRepository pressEntityRepository;

    private final PressEntityMapperImpl mapper = new PressEntityMapperImpl();

    @Autowired
    PressEntityServiceTest(PressEntityService pressEntityService, PressEntityRepository pressEntityRepository) {
        this.pressEntityService = pressEntityService;
        this.pressEntityRepository = pressEntityRepository;
    }

    @DisplayName("모든 언론사 획득")
    @Test
    void getAllTest() {
        // given
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), press.getEnglishName());
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(press.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);
        when(pressEntityRepository.findAll()).thenReturn(List.of(pressEntity));

        // when
        pressEntityService.insert(press);

        // then
        assertThat(pressEntityService.getAll()).isEqualTo(List.of(press));
    }

    @DisplayName("번호로 언론사 획득")
    @Test
    void getByNumberTest() {
        // given
        Long number = press.getNumber();
        PressEntity pressEntity = new PressEntity(
                number, press.getKoreanName(), press.getEnglishName());
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(press.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);
        when(pressEntityRepository.findByNumber(number)).thenReturn(Optional.of(pressEntity));

        // when
        pressEntityService.insert(press);

        // then
        assertThat(pressEntityService.getByNumber(number).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("한글명으로 언론사 획득")
    @Test
    void getByKoreanNameTest() {
        // given
        String koreanName = press.getKoreanName();
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), koreanName, press.getEnglishName());
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByKoreanName(koreanName)).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.findByEnglishName(press.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        assertThat(pressEntityService.getByKoreanName(koreanName).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("영문명으로 언론사 획득")
    @Test
    void getByEnglishNameTest() {
        // given
        String englishName = press.getEnglishName();
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), englishName);
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(englishName))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        assertThat(pressEntityService.getByEnglishName(press.getEnglishName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("언론사 삽입")
    @Test
    void insertTest() {
        // given
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), press.getEnglishName());
        PressEntity anotherPressEntity = new PressEntity(
                anotherPress.getNumber(), anotherPress.getKoreanName(), anotherPress.getEnglishName());
        when(pressEntityRepository.existsByNumber(any())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(any())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity).thenReturn(anotherPressEntity);
        when(pressEntityRepository.findAll()).thenReturn(List.of(pressEntity, anotherPressEntity));

        // when
        pressEntityService.insert(press);
        pressEntityService.insert(anotherPress);

        // then
        assertThat(pressEntityService.getAll()).isEqualTo(List.of(press, anotherPress));
    }

    @DisplayName("이미 존재하는 번호로 언론사 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        Long number = press.getNumber();
        PressEntity pressEntity = new PressEntity(
                number, press.getKoreanName(), press.getEnglishName());
        PressEntity pressEntityExistedNumber = new PressEntity(
                number, anotherPress.getKoreanName(), anotherPress.getEnglishName());
        Press pressExistedNumber = mapper.toPress(pressEntityExistedNumber);
        when(pressEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(press.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> pressEntityService.insert(pressExistedNumber));
        assertThat(exception.getMessage()).isEqualTo(
                ALREADY_EXISTED_ENTITY_WITH_NUMBER + press.getNumber() +
                        FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
    }

    @DisplayName("이미 존재하는 영문명으로 언론사 삽입")
    @Test
    void insertAlreadyExistedEnglishNameTest() {
        // given
        String englishName = press.getEnglishName();
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), englishName);
        PressEntity pressEntityExistedEnglishName = new PressEntity(
                anotherPress.getNumber(), anotherPress.getKoreanName(), englishName);
        Press pressExistedName = mapper.toPress(pressEntityExistedEnglishName);
        when(pressEntityRepository.existsByNumber(any())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.empty()).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class,
                () -> pressEntityService.insert(pressExistedName));
        assertThat(exception.getMessage()).isEqualTo(
                ALREADY_EXISTED_ENTITY_WITH_ENGLISH_NAME + press.getEnglishName() +
                        FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
    }

    @DisplayName("언론사 갱신")
    @Test
    void updateTest() {
        // given
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), press.getEnglishName());
        Press updatedPress = Press.builder()
                .press(anotherPress).number(press.getNumber()).build();
        PressEntity updatedPressEntity = mapper.toPressEntity(updatedPress);
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(any())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity).thenReturn(updatedPressEntity);
        when(pressEntityRepository.findAll()).thenReturn(List.of(updatedPressEntity));
        pressEntityService.insert(press);

        // when
        Press updatePress = pressEntityService.update(updatedPress);

        // then
        assertThat(pressEntityService.getAll()).isEqualTo(List.of(updatePress));
    }

    @DisplayName("존재하지 않는 번호로 언론사 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), press.getEnglishName());
        Long notFoundNumber = anotherPress.getNumber();
        PressEntity pressEntityNotFoundNumber = new PressEntity(
                notFoundNumber, anotherPress.getKoreanName(), anotherPress.getEnglishName());
        Press pressNotFoundNumber = mapper.toPress(pressEntityNotFoundNumber);
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false);
        when(pressEntityRepository.existsByNumber(notFoundNumber)).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(press.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> pressEntityService.update(pressNotFoundNumber));
        assertThat(exception.getMessage()).isEqualTo(
                CANNOT_FOUND_ENTITY_WITH_NUMBER + notFoundNumber +
                        FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
    }

    @DisplayName("이미 존재하는 영문명으로 언론사 갱신")
    @Test
    void updateAlreadyExistedEnglishNameTest() {
        // given
        String englishName = press.getEnglishName();
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), englishName);
        PressEntity pressEntityExistedNumber = new PressEntity(
                anotherPress.getNumber(), anotherPress.getKoreanName(), englishName);
        Press pressExistedName = mapper.toPress(pressEntityExistedNumber);
        when(pressEntityRepository.existsByNumber(press.getNumber()))
                .thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(englishName))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class,
                () -> pressEntityService.insert(pressExistedName));
        assertThat(exception.getMessage()).isEqualTo(
                ALREADY_EXISTED_ENTITY_WITH_ENGLISH_NAME + press.getEnglishName() +
                        FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
    }

    @DisplayName("번호로 언론사 제거")
    @Test
    void removeByNumberTest() {
        // given
        Long number = press.getNumber();
        PressEntity pressEntity = new PressEntity(
                press.getNumber(), press.getKoreanName(), press.getEnglishName());
        when(pressEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(press.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);
        doNothing().when(pressEntityRepository).deleteByNumber(number);
        when(pressEntityRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        pressEntityService.insert(press);
        pressEntityService.removeByNumber(number);

        // then
        assertThat(pressEntityService.getAll()).isEqualTo(Collections.emptyList());
    }

    @DisplayName("존재하지 않는 번호로 언론사 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        when(pressEntityRepository.existsByNumber(press.getNumber())).thenReturn(false);

        // when
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> pressEntityService.removeByNumber(press.getNumber()));

        // then
        assertThat(exception.getMessage()).isEqualTo(
                CANNOT_FOUND_ENTITY_WITH_NUMBER + press.getNumber() +
                        FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
    }
}