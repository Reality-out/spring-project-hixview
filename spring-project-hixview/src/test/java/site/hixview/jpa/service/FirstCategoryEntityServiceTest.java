package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.mapper.FirstCategoryEntityMapper;
import site.hixview.jpa.mapper.FirstCategoryEntityMapperImpl;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.CompanyEntityTestUtils;
import site.hixview.support.jpa.util.FirstCategoryEntityTestUtils;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryEntityTestUtils;
import site.hixview.support.spring.util.FirstCategoryTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordCamel.ENGLISH_NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@OnlyRealServiceContext
@Slf4j
class FirstCategoryEntityServiceTest implements CompanyEntityTestUtils, FirstCategoryEntityTestUtils, IndustryArticleSecondCategoryEntityTestUtils, FirstCategoryTestUtils {

    private final FirstCategoryEntityService fcEntityService;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository fcEntityRepository;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final IndustryCategoryEntityRepository icEntityRepository;
    private final SecondCategoryEntityRepository scEntityRepository;

    private final FirstCategoryEntityMapper fcEntityMapper = new FirstCategoryEntityMapperImpl();

    @Autowired
    FirstCategoryEntityServiceTest(FirstCategoryEntityService fcEntityService, CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository fcEntityRepository, IndustryArticleEntityRepository iaEntityRepository, IndustryCategoryEntityRepository icEntityRepository, SecondCategoryEntityRepository scEntityRepository) {
        this.fcEntityService = fcEntityService;
        this.companyEntityRepository = companyEntityRepository;
        this.fcEntityRepository = fcEntityRepository;
        this.iaEntityRepository = iaEntityRepository;
        this.icEntityRepository = icEntityRepository;
        this.scEntityRepository = scEntityRepository;
    }

    @DisplayName("모든 1차 업종 획득")
    @Test
    void getAllTest() {
        // given
        FirstCategoryEntity firstCategoryEntity = createFirstCategoryEntity();
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(fcEntityRepository.findAll()).thenReturn(List.of(firstCategoryEntity));

        // when
        fcEntityRepository.save(firstCategoryEntity);

        // then
        assertThat(fcEntityService.getAll()).isEqualTo(List.of(fcEntityMapper.toFirstCategory(firstCategoryEntity)));
    }

    @DisplayName("번호로 1차 업종 획득")
    @Test
    void getByNumberTest() {
        // given
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        Long number = firstCategoryEntity.getNumber();
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(fcEntityRepository.findByNumber(number)).thenReturn(Optional.of(firstCategoryEntity));

        // when
        fcEntityRepository.save(firstCategoryEntity);

        // then
        assertThat(fcEntityService.getByNumber(number).orElseThrow()).isEqualTo(fcEntityMapper.toFirstCategory(firstCategoryEntity));
    }

    @DisplayName("한글명으로 1차 업종 획득")
    @Test
    void getByKoreanNameTest() {
        // given
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        String koreanName = firstCategoryEntity.getKoreanName();
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(fcEntityRepository.findByKoreanName(koreanName)).thenReturn(Optional.of(firstCategoryEntity));

        // when
        fcEntityRepository.save(firstCategoryEntity);

        // then
        assertThat(fcEntityService.getByKoreanName(koreanName).orElseThrow()).isEqualTo(fcEntityMapper.toFirstCategory(firstCategoryEntity));
    }

    @DisplayName("영문명으로 1차 업종 획득")
    @Test
    void getByEnglishNameTest() {
        // given
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        String englishName = firstCategoryEntity.getEnglishName();
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(fcEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.of(firstCategoryEntity));

        // when
        fcEntityRepository.save(firstCategoryEntity);

        // then
        assertThat(fcEntityService.getByEnglishName(englishName).orElseThrow()).isEqualTo(fcEntityMapper.toFirstCategory(firstCategoryEntity));
    }

    @DisplayName("1차 업종 삽입")
    @Test
    void insertTest() {
        // given
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        IndustryCategoryEntity industryCategoryEntity = firstCategoryEntity.getIndustryCategory();
        FirstCategory firstCategory = fcEntityMapper.toFirstCategory(firstCategoryEntity);
        when(icEntityRepository.save(industryCategoryEntity)).thenReturn(industryCategoryEntity);
        when(fcEntityRepository.existsByNumber(firstCategoryEntity.getNumber())).thenReturn(false);
        when(fcEntityRepository.findByEnglishName(firstCategoryEntity.getEnglishName())).thenReturn(Optional.empty());
        when(icEntityRepository.findByNumber(industryCategoryEntity.getNumber())).thenReturn(Optional.of(industryCategoryEntity));
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(fcEntityRepository.findAll()).thenReturn(List.of(firstCategoryEntity));
        icEntityRepository.save(industryCategoryEntity);

        // when
        fcEntityService.insert(firstCategory);

        // then
        assertThat(fcEntityService.getAll()).isEqualTo(List.of(firstCategory));
    }

    @DisplayName("이미 존재하는 번호로 1차 업종 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        IndustryCategoryEntity industryCategoryEntity = firstCategoryEntity.getIndustryCategory();
        FirstCategory firstCategory = fcEntityMapper.toFirstCategory(firstCategoryEntity);
        when(icEntityRepository.save(industryCategoryEntity)).thenReturn(industryCategoryEntity);
        when(fcEntityRepository.existsByNumber(firstCategoryEntity.getNumber())).thenReturn(false).thenReturn(true);
        when(fcEntityRepository.findByEnglishName(firstCategoryEntity.getEnglishName())).thenReturn(Optional.empty());
        when(icEntityRepository.findByNumber(industryCategoryEntity.getNumber())).thenReturn(Optional.of(industryCategoryEntity));
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        icEntityRepository.save(industryCategoryEntity);

        // when
        fcEntityService.insert(firstCategory);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> fcEntityService.insert(FirstCategory.builder()
                        .firstCategory(anotherFirstCategory).number(firstCategory.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, firstCategory.getNumber(), FirstCategoryEntity.class));
    }

    @DisplayName("이미 존재하는 영문명으로 1차 업종 삽입")
    @Test
    void insertAlreadyExistedEnglishNameTest() {
        // given
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        FirstCategoryEntity fcEntityExistedEnglishName = createAnotherFirstCategoryEntity();
        fcEntityExistedEnglishName.updateEnglishName(fcEntity.getEnglishName());
        IndustryCategoryEntity icEntity = fcEntity.getIndustryCategory();
        FirstCategory firstCategory = fcEntityMapper.toFirstCategory(fcEntity);
        when(icEntityRepository.save(icEntity)).thenReturn(icEntity);
        when(fcEntityRepository.existsByNumber(fcEntity.getNumber())).thenReturn(false);
        when(fcEntityRepository.findByEnglishName(fcEntity.getEnglishName())).thenReturn(Optional.empty()).thenReturn(Optional.of(fcEntity));
        when(icEntityRepository.findByNumber(icEntity.getNumber())).thenReturn(Optional.of(icEntity));
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity);
        icEntityRepository.save(icEntity);

        // when
        fcEntityService.insert(firstCategory);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> fcEntityService.insert(fcEntityMapper.toFirstCategory(fcEntityExistedEnglishName)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ENGLISH_NAME, fcEntity.getEnglishName(), FirstCategoryEntity.class));
    }

    @DisplayName("1차 업종 갱신")
    @Test
    void updateTest() {
        // given
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        IndustryCategoryEntity icEntity = fcEntity.getIndustryCategory();
        FirstCategory fcUpdated = FirstCategory.builder().firstCategory(anotherFirstCategory).number(fcEntity.getNumber()).industryCategoryNumber(fcEntity.getIndustryCategory().getNumber()).build();
        when(icEntityRepository.findByNumber(fcUpdated.getIndustryCategoryNumber())).thenReturn(Optional.of(icEntity));
        FirstCategoryEntity fcEntityUpdated = fcEntityMapper.toFirstCategoryEntity(fcUpdated, icEntityRepository);
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity).thenReturn(fcEntityUpdated);
        when(fcEntityRepository.existsByNumber(fcEntity.getNumber())).thenReturn(true);
        when(fcEntityRepository.findByEnglishName(fcEntity.getEnglishName())).thenReturn(Optional.empty());
        when(fcEntityRepository.findByNumber(fcEntity.getNumber())).thenReturn(Optional.of(fcEntity));
        when(icEntityRepository.findByNumber(icEntity.getNumber())).thenReturn(Optional.of(icEntity));
        when(fcEntityRepository.findAll()).thenReturn(List.of(fcEntityUpdated));
        fcEntityRepository.save(fcEntity);

        // when
        fcEntityService.update(fcUpdated);

        // then
        assertThat(fcEntityService.getAll()).isEqualTo(List.of(fcUpdated));
    }

    @DisplayName("발견되지 않는 번호로 1차 업종 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given & when
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        when(fcEntityRepository.existsByNumber(fcEntity.getNumber())).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                fcEntityService.update(fcEntityMapper.toFirstCategory(fcEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, fcEntity.getNumber(), FirstCategoryEntity.class));
    }

    @DisplayName("이미 존재하는 영문명으로 1차 업종 갱신")
    @Test
    void updateAlreadyExistedEnglishNameTest() {
        // given
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity);
        when(fcEntityRepository.existsByNumber(fcEntity.getNumber())).thenReturn(true);
        when(fcEntityRepository.findByEnglishName(fcEntity.getEnglishName())).thenReturn(Optional.of(fcEntity));

        // when
        fcEntityRepository.save(fcEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                fcEntityService.update(fcEntityMapper.toFirstCategory(fcEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ENGLISH_NAME, fcEntity.getEnglishName(), FirstCategoryEntity.class));
    }

    @DisplayName("번호로 1차 업종 제거")
    @Test
    void removeByNumberTest() {
        // given
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        Long number = fcEntity.getNumber();
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity);
        when(fcEntityRepository.existsByNumber(number)).thenReturn(true);
        when(fcEntityRepository.findByNumber(number)).thenReturn(Optional.of(fcEntity));
        when(scEntityRepository.findByFirstCategory(fcEntity)).thenReturn(Collections.emptyList());
        when(companyEntityRepository.findByFirstCategory(fcEntity)).thenReturn(Collections.emptyList());
        when(iaEntityRepository.findByFirstCategory(fcEntity)).thenReturn(Collections.emptyList());
        doNothing().when(fcEntityRepository).deleteByNumber(number);
        when(fcEntityRepository.findAll()).thenReturn(Collections.emptyList());
        fcEntityRepository.save(fcEntity);

        // when
        fcEntityService.removeByNumber(number);

        // then
        assertThat(fcEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 1차 업종 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        Long number = fcEntity.getNumber();

        // when
        when(fcEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> fcEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, FirstCategoryEntity.class));
    }

    @DisplayName("번호로 2차 업종, 기업, 또는 산업 기사에 포함된 1차 업종 제거")
    @Test
    void removeByNumberInSecondCategoryOrCompanyOrIndustryArticleTest() {
        // given
        FirstCategoryEntity fcEntity = createNumberedFirstCategoryEntity();
        Long number = fcEntity.getNumber();
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity);
        when(fcEntityRepository.existsByNumber(number)).thenReturn(true);
        when(fcEntityRepository.findByNumber(number)).thenReturn(Optional.of(fcEntity));
        when(scEntityRepository.findByFirstCategory(fcEntity)).thenReturn(List.of(createSecondCategoryEntity()));

        // when - 1
        fcEntityRepository.save(fcEntity);

        // then - 1
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> fcEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, FirstCategoryEntity.class));

        // when - 2
        when(scEntityRepository.findByFirstCategory(fcEntity)).thenReturn(Collections.emptyList());
        when(companyEntityRepository.findByFirstCategory(fcEntity)).thenReturn(List.of(createCompanyEntity()));

        // then - 2
        exception = assertThrows(
                DataIntegrityViolationException.class, () -> fcEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, FirstCategoryEntity.class));

        // when - 3
        when(companyEntityRepository.findByFirstCategory(fcEntity)).thenReturn(Collections.emptyList());
        when(iaEntityRepository.findByFirstCategory(fcEntity)).thenReturn(List.of(createIndustryArticleEntity()));

        // then - 3
        exception = assertThrows(
                DataIntegrityViolationException.class, () -> fcEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, FirstCategoryEntity.class));
    }
}