package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.mapper.FirstCategoryEntityMapper;
import site.hixview.jpa.mapper.FirstCategoryEntityMapperImpl;
import site.hixview.jpa.mapper.SecondCategoryEntityMapper;
import site.hixview.jpa.mapper.SecondCategoryEntityMapperImpl;
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
class SecondCategoryEntityServiceTest implements CompanyEntityTestUtils, FirstCategoryEntityTestUtils, IndustryArticleSecondCategoryEntityTestUtils, FirstCategoryTestUtils {

    private final SecondCategoryEntityService scEntityService;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository fcEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;
    private final IndustryCategoryEntityRepository icEntityRepository;
    private final SecondCategoryEntityRepository scEntityRepository;

    private final FirstCategoryEntityMapper fcEntityMapper = new FirstCategoryEntityMapperImpl();
    private final SecondCategoryEntityMapper scEntityMapper = new SecondCategoryEntityMapperImpl();

    @Autowired
    SecondCategoryEntityServiceTest(SecondCategoryEntityService scEntityService, CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository fcEntityRepository, IndustryArticleSecondCategoryEntityRepository iascEntityRepository, IndustryCategoryEntityRepository icEntityRepository, SecondCategoryEntityRepository scEntityRepository) {
        this.scEntityService = scEntityService;
        this.companyEntityRepository = companyEntityRepository;
        this.fcEntityRepository = fcEntityRepository;
        this.iascEntityRepository = iascEntityRepository;
        this.icEntityRepository = icEntityRepository;
        this.scEntityRepository = scEntityRepository;
    }

    @DisplayName("모든 2차 업종 획득")
    @Test
    void getAllTest() {
        // given
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(scEntityRepository.findAll()).thenReturn(List.of(secondCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);

        // then
        assertThat(scEntityService.getAll()).isEqualTo(List.of(scEntityMapper.toSecondCategory(secondCategoryEntity)));
    }

    @DisplayName("1차 업종으로 2차 업종 획득")
    @Test
    void getByFirstCategoryTest() {
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(scEntityRepository.findByFirstCategory(firstCategoryEntity)).thenReturn(List.of(secondCategoryEntity));
        when(fcEntityRepository.findByNumber(firstCategoryEntity.getNumber())).thenReturn(Optional.of(firstCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);

        // then
        assertThat(scEntityService.getByFirstCategory(fcEntityMapper.toFirstCategory(firstCategoryEntity))).isEqualTo(List.of(scEntityMapper.toSecondCategory(secondCategoryEntity)));
    }

    @DisplayName("번호로 2차 업종 획득")
    @Test
    void getByNumberTest() {
        // given
        SecondCategoryEntity secondCategoryEntity = createNumberedSecondCategoryEntity();
        Long number = secondCategoryEntity.getNumber();
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(scEntityRepository.findByNumber(number)).thenReturn(Optional.of(secondCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);

        // then
        assertThat(scEntityService.getByNumber(number).orElseThrow()).isEqualTo(scEntityMapper.toSecondCategory(secondCategoryEntity));
    }

    @DisplayName("한글명으로 2차 업종 획득")
    @Test
    void getByKoreanNameTest() {
        // given
        SecondCategoryEntity secondCategoryEntity = createNumberedSecondCategoryEntity();
        String koreanName = secondCategoryEntity.getKoreanName();
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(scEntityRepository.findByKoreanName(koreanName)).thenReturn(Optional.of(secondCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);

        // then
        assertThat(scEntityService.getByKoreanName(koreanName).orElseThrow()).isEqualTo(scEntityMapper.toSecondCategory(secondCategoryEntity));
    }

    @DisplayName("영문명으로 2차 업종 획득")
    @Test
    void getByEnglishNameTest() {
        // given
        SecondCategoryEntity secondCategoryEntity = createNumberedSecondCategoryEntity();
        String englishName = secondCategoryEntity.getEnglishName();
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(scEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.of(secondCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);

        // then
        assertThat(scEntityService.getByEnglishName(englishName).orElseThrow()).isEqualTo(scEntityMapper.toSecondCategory(secondCategoryEntity));
    }

    @DisplayName("2차 업종 삽입")
    @Test
    void insertTest() {
        // given
        SecondCategoryEntity secondCategoryEntity = createNumberedSecondCategoryEntity();
        FirstCategoryEntity firstCategoryEntity = secondCategoryEntity.getFirstCategory();
        IndustryCategoryEntity industryCategoryEntity = secondCategoryEntity.getIndustryCategory();
        SecondCategory secondCategory = scEntityMapper.toSecondCategory(secondCategoryEntity);
        when(icEntityRepository.save(industryCategoryEntity)).thenReturn(industryCategoryEntity);
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(scEntityRepository.existsByNumber(secondCategoryEntity.getNumber())).thenReturn(false);
        when(scEntityRepository.findByEnglishName(secondCategoryEntity.getEnglishName())).thenReturn(Optional.empty());
        when(icEntityRepository.findByNumber(industryCategoryEntity.getNumber())).thenReturn(Optional.of(industryCategoryEntity));
        when(fcEntityRepository.findByNumber(firstCategoryEntity.getNumber())).thenReturn(Optional.of(firstCategoryEntity));
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(scEntityRepository.findAll()).thenReturn(List.of(secondCategoryEntity));
        icEntityRepository.save(industryCategoryEntity);
        fcEntityRepository.save(firstCategoryEntity);

        // when
        scEntityService.insert(secondCategory);

        // then
        assertThat(scEntityService.getAll()).isEqualTo(List.of(secondCategory));
    }

    @DisplayName("이미 존재하는 번호로 2차 업종 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        FirstCategoryEntity fcEntity = scEntity.getFirstCategory();
        IndustryCategoryEntity icEntity = scEntity.getIndustryCategory();
        when(icEntityRepository.save(icEntity)).thenReturn(icEntity);
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(scEntityRepository.existsByNumber(scEntity.getNumber())).thenReturn(true);

        // when
        icEntityRepository.save(icEntity);
        fcEntityRepository.save(fcEntity);
        scEntityRepository.save(scEntity);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> scEntityService.insert(SecondCategory.builder()
                        .secondCategory(anotherSecondCategory).number(scEntity.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, scEntity.getNumber(), SecondCategoryEntity.class));
    }

    @DisplayName("이미 존재하는 영문명으로 2차 업종 삽입")
    @Test
    void insertAlreadyExistedEnglishNameTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        FirstCategoryEntity fcEntity = scEntity.getFirstCategory();
        IndustryCategoryEntity icEntity = scEntity.getIndustryCategory();
        when(icEntityRepository.save(icEntity)).thenReturn(icEntity);
        when(fcEntityRepository.save(fcEntity)).thenReturn(fcEntity);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(scEntityRepository.existsByNumber(scEntity.getNumber())).thenReturn(false);
        when(scEntityRepository.findByEnglishName(scEntity.getEnglishName())).thenReturn(Optional.of(scEntity));

        // when
        icEntityRepository.save(icEntity);
        fcEntityRepository.save(fcEntity);
        scEntityRepository.save(scEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> scEntityService.insert(SecondCategory.builder()
                        .secondCategory(anotherSecondCategory).englishName(scEntity.getEnglishName()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ENGLISH_NAME, scEntity.getEnglishName(), SecondCategoryEntity.class));
    }

    @DisplayName("2차 업종 갱신")
    @Test
    void updateTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        FirstCategoryEntity fcEntity = scEntity.getFirstCategory();
        IndustryCategoryEntity icEntity = scEntity.getIndustryCategory();
        SecondCategory scUpdated = SecondCategory.builder().secondCategory(anotherSecondCategory).number(scEntity.getNumber()).industryCategoryNumber(scEntity.getIndustryCategory().getNumber()).firstCategoryNumber(scEntity.getFirstCategory().getNumber()).build();
        when(icEntityRepository.findByNumber(scUpdated.getIndustryCategoryNumber())).thenReturn(Optional.of(icEntity));
        when(fcEntityRepository.findByNumber(scUpdated.getFirstCategoryNumber())).thenReturn(Optional.of(fcEntity));
        SecondCategoryEntity scEntityUpdated = scEntityMapper.toSecondCategoryEntity(scUpdated, icEntityRepository, fcEntityRepository);
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity).thenReturn(scEntityUpdated);
        when(scEntityRepository.existsByNumber(scEntity.getNumber())).thenReturn(true);
        when(scEntityRepository.findByEnglishName(scEntity.getEnglishName())).thenReturn(Optional.empty());
        when(scEntityRepository.findByNumber(scEntity.getNumber())).thenReturn(Optional.of(scEntity));
        when(fcEntityRepository.findByNumber(fcEntity.getNumber())).thenReturn(Optional.of(fcEntity));
        when(icEntityRepository.findByNumber(icEntity.getNumber())).thenReturn(Optional.of(icEntity));
        when(scEntityRepository.findAll()).thenReturn(List.of(scEntityUpdated));
        scEntityRepository.save(scEntity);

        // when
        scEntityService.update(scUpdated);

        // then
        assertThat(scEntityService.getAll()).isEqualTo(List.of(scUpdated));
    }

    @DisplayName("발견되지 않는 번호로 2차 업종 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given & when
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        when(scEntityRepository.existsByNumber(scEntity.getNumber())).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                scEntityService.update(scEntityMapper.toSecondCategory(scEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, scEntity.getNumber(), SecondCategoryEntity.class));
    }

    @DisplayName("이미 존재하는 영문명으로 2차 업종 갱신")
    @Test
    void updateAlreadyExistedEnglishNameTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(scEntityRepository.existsByNumber(scEntity.getNumber())).thenReturn(true);
        when(scEntityRepository.findByEnglishName(scEntity.getEnglishName())).thenReturn(Optional.of(scEntity));

        // when
        scEntityRepository.save(scEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                scEntityService.update(scEntityMapper.toSecondCategory(scEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ENGLISH_NAME, scEntity.getEnglishName(), SecondCategoryEntity.class));
    }

    @DisplayName("번호로 2차 업종 제거")
    @Test
    void removeByNumberTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        Long number = scEntity.getNumber();
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(scEntityRepository.existsByNumber(number)).thenReturn(true);
        when(scEntityRepository.findByNumber(number)).thenReturn(Optional.of(scEntity));
        when(companyEntityRepository.findBySecondCategory(scEntity)).thenReturn(Collections.emptyList());
        when(iascEntityRepository.findBySecondCategory(scEntity)).thenReturn(Collections.emptyList());
        doNothing().when(scEntityRepository).deleteByNumber(number);
        when(scEntityRepository.findAll()).thenReturn(Collections.emptyList());
        scEntityRepository.save(scEntity);

        // when
        scEntityService.removeByNumber(number);

        // then
        assertThat(scEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 2차 업종 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        Long number = scEntity.getNumber();

        // when
        when(scEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> scEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, SecondCategoryEntity.class));
    }

    @DisplayName("번호로 2차 업종, 기업, 또는 산업 기사에 포함된 2차 업종 제거")
    @Test
    void removeByNumberInSecondCategoryOrCompanyOrIndustryArticleTest() {
        // given
        SecondCategoryEntity scEntity = createNumberedSecondCategoryEntity();
        Long number = scEntity.getNumber();
        when(scEntityRepository.save(scEntity)).thenReturn(scEntity);
        when(scEntityRepository.existsByNumber(scEntity.getNumber())).thenReturn(true);
        when(scEntityRepository.findByNumber(scEntity.getNumber())).thenReturn(Optional.of(scEntity));
        when(companyEntityRepository.findBySecondCategory(scEntity)).thenReturn(List.of(createCompanyEntity()));

        // when - 1
        scEntityRepository.save(scEntity);

        // then - 1
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> scEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, SecondCategoryEntity.class));

        // when - 2
        when(companyEntityRepository.findBySecondCategory(scEntity)).thenReturn(Collections.emptyList());
        when(iascEntityRepository.findBySecondCategory(scEntity)).thenReturn(List.of(createIndustryArticleSecondCategoryEntity()));

        // then - 2
        exception = assertThrows(
                DataIntegrityViolationException.class, () -> scEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, SecondCategoryEntity.class));
    }
}