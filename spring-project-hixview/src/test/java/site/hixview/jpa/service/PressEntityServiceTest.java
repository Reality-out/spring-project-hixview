package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.PressEntityMapper;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.CompanyArticleEntityTestUtils;
import site.hixview.support.jpa.util.EconomyArticleEntityTestUtils;
import site.hixview.support.jpa.util.IndustryArticleEntityTestUtils;
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
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordCamel.ENGLISH_NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@OnlyRealServiceContext
@Slf4j
class PressEntityServiceTest implements PressEntityTestUtils, CompanyArticleEntityTestUtils, IndustryArticleEntityTestUtils, EconomyArticleEntityTestUtils, PressTestUtils {

    private final CompanyArticleEntityRepository caEntityRepository;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final EconomyArticleEntityRepository eaEntityRepository;
    private final PressEntityService pressEntityService;
    private final PressEntityRepository pressEntityRepository;

    private final PressEntityMapper mapper = new PressEntityMapperImpl();

    @Autowired
    PressEntityServiceTest(CompanyArticleEntityRepository caEntityRepository, IndustryArticleEntityRepository iaEntityRepository, EconomyArticleEntityRepository eaEntityRepository, PressEntityService pressEntityService, PressEntityRepository pressEntityRepository) {
        this.caEntityRepository = caEntityRepository;
        this.iaEntityRepository = iaEntityRepository;
        this.eaEntityRepository = eaEntityRepository;
        this.pressEntityService = pressEntityService;
        this.pressEntityRepository = pressEntityRepository;
    }

    @DisplayName("모든 언론사 획득")
    @Test
    void getAllTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        when(pressEntityRepository.existsByNumber(pressEntity.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
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
        PressEntity pressEntity = createNumberedPressEntity();
        Long number = pressEntity.getNumber();
        when(pressEntityRepository.existsByNumber(pressEntity.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
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
        PressEntity pressEntity = createNumberedPressEntity();
        String koreanName = pressEntity.getKoreanName();
        when(pressEntityRepository.existsByNumber(pressEntity.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByKoreanName(koreanName)).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
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
        PressEntity pressEntity = createNumberedPressEntity();
        String englishName = pressEntity.getEnglishName();
        when(pressEntityRepository.existsByNumber(pressEntity.getNumber())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.empty()).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        assertThat(pressEntityService.getByEnglishName(englishName).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("언론사 삽입")
    @Test
    void insertTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
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
        PressEntity pressEntity = createNumberedPressEntity();
        Long number = pressEntity.getNumber();
        PressEntity pressEntityExistedNumber = new PressEntity(
                number, anotherPress.getKoreanName(), anotherPress.getEnglishName());
        Press pressExistedNumber = mapper.toPress(pressEntityExistedNumber);
        when(pressEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> pressEntityService.insert(pressExistedNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, PressEntity.class));
    }

    @DisplayName("이미 존재하는 영문명으로 언론사 삽입")
    @Test
    void insertAlreadyExistedEnglishNameTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        String englishName = pressEntity.getEnglishName();
        PressEntity pressEntityExistedEnglishName = createAnotherPressEntity();
        pressEntityExistedEnglishName.updateEnglishName(englishName);
        Press pressExistedName = mapper.toPress(pressEntityExistedEnglishName);
        when(pressEntityRepository.existsByNumber(any())).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.empty()).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> pressEntityService.insert(pressExistedName));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, PressEntity.class));
    }

    @DisplayName("언론사 갱신")
    @Test
    void updateTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        Long number = pressEntity.getNumber();
        Press updatedPress = Press.builder().press(anotherPress).number(number).build();
        PressEntity updatedPressEntity = mapper.toPressEntity(updatedPress);
        when(pressEntityRepository.save(any())).thenReturn(pressEntity).thenReturn(updatedPressEntity);
        when(pressEntityRepository.existsByNumber(number)).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.findByNumber(number)).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.findAll()).thenReturn(List.of(updatedPressEntity));
        pressEntityRepository.save(pressEntity);

        // when
        Press updatePress = pressEntityService.update(updatedPress);

        // then
        assertThat(pressEntityService.getAll()).isEqualTo(List.of(updatePress));
    }

    @DisplayName("발견되지 않는 번호로 언론사 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        Long notFoundNumber = anotherPress.getNumber();
        PressEntity pressEntityNotFoundNumber = new PressEntity(
                notFoundNumber, anotherPress.getKoreanName(), anotherPress.getEnglishName());
        Press pressNotFoundNumber = mapper.toPress(pressEntityNotFoundNumber);
        when(pressEntityRepository.existsByNumber(pressEntity.getNumber())).thenReturn(false);
        when(pressEntityRepository.existsByNumber(notFoundNumber)).thenReturn(false);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> pressEntityService.update(pressNotFoundNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, notFoundNumber, PressEntity.class));
    }

    @DisplayName("이미 존재하는 영문명으로 언론사 갱신")
    @Test
    void updateAlreadyExistedEnglishNameTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        String englishName = pressEntity.getEnglishName();
        PressEntity pressEntityExistedNumber = createAnotherPressEntity();
        pressEntityExistedNumber.updateEnglishName(englishName);
        Press pressExistedName = mapper.toPress(pressEntityExistedNumber);
        when(pressEntityRepository.existsByNumber(press.getNumber()))
                .thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(englishName))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(pressEntity));
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);

        // when
        pressEntityService.insert(press);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> pressEntityService.insert(pressExistedName));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, PressEntity.class));
    }

    @DisplayName("번호로 언론사 제거")
    @Test
    void removeByNumberTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        Long number = pressEntity.getNumber();
        when(pressEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);
        when(pressEntityRepository.findByNumber(number)).thenReturn(Optional.of(pressEntity));
        when(caEntityRepository.findByPress(pressEntity)).thenReturn(Collections.emptyList());
        when(iaEntityRepository.findByPress(pressEntity)).thenReturn(Collections.emptyList());
        when(eaEntityRepository.findByPress(pressEntity)).thenReturn(Collections.emptyList());
        doNothing().when(pressEntityRepository).deleteByNumber(number);
        when(pressEntityRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        pressEntityService.insert(press);
        pressEntityService.removeByNumber(number);

        // then
        assertThat(pressEntityService.getAll()).isEqualTo(Collections.emptyList());
    }

    @DisplayName("발견되지 않는 번호로 언론사 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        Long number = press.getNumber();
        when(pressEntityRepository.existsByNumber(number)).thenReturn(false);

        // when
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> pressEntityService.removeByNumber(number));

        // then
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, PressEntity.class));
    }

    @DisplayName("번호로 기사에 포함된 언론사 제거")
    @Test
    void removeByNumberInArticleTest() {
        // given
        PressEntity pressEntity = createNumberedPressEntity();
        Long number = pressEntity.getNumber();
        when(pressEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(pressEntityRepository.findByEnglishName(pressEntity.getEnglishName())).thenReturn(Optional.empty());
        when(pressEntityRepository.save(any())).thenReturn(pressEntity);
        when(pressEntityRepository.findByNumber(number)).thenReturn(Optional.of(pressEntity));
        when(caEntityRepository.findByPress(pressEntity)).thenReturn(List.of(createCompanyArticleEntity()));

        // when - 1
        pressEntityService.insert(press);

        // then - 1
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> pressEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, PressEntity.class));

        // when - 2
        when(caEntityRepository.findByPress(pressEntity)).thenReturn(Collections.emptyList());
        when(iaEntityRepository.findByPress(pressEntity)).thenReturn(List.of(createIndustryArticleEntity()));

        // then - 2
        exception = assertThrows(
                DataIntegrityViolationException.class, () -> pressEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, PressEntity.class));

        // when - 3
        when(iaEntityRepository.findByPress(pressEntity)).thenReturn(Collections.emptyList());
        when(eaEntityRepository.findByPress(pressEntity)).thenReturn(List.of(createEconomyArticleEntity()));

        // then - 3
        exception = assertThrows(
                DataIntegrityViolationException.class, () -> pressEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, PressEntity.class));
    }
}