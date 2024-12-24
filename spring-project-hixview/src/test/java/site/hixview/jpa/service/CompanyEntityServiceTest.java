package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.CompanyArticleCompanyEntityTestUtils;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryEntityTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordCamel.CODE;

@OnlyRealServiceContext
@Slf4j
class CompanyEntityServiceTest implements CompanyArticleCompanyEntityTestUtils, IndustryArticleSecondCategoryEntityTestUtils {

    private final CompanyEntityService companyEntityService;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository fcEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;
    private final SecondCategoryEntityRepository scEntityRepository;

    private final CompanyEntityMapper companyEntityMapper = new CompanyEntityMapperImpl();
    private final FirstCategoryEntityMapper fcEntityMapper = new FirstCategoryEntityMapperImpl();
    private final SecondCategoryEntityMapper scEntityMapper = new SecondCategoryEntityMapperImpl();

    @Autowired
    CompanyEntityServiceTest(CompanyEntityService companyEntityService, CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository fcEntityRepository, CompanyArticleCompanyEntityRepository cacEntityRepository, SecondCategoryEntityRepository scEntityRepository) {
        this.companyEntityService = companyEntityService;
        this.companyEntityRepository = companyEntityRepository;
        this.fcEntityRepository = fcEntityRepository;
        this.cacEntityRepository = cacEntityRepository;
        this.scEntityRepository = scEntityRepository;
    }

    @DisplayName("모든 기업 획득")
    @Test
    void getAllTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findAll()).thenReturn(List.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getAll()).isEqualTo(List.of(companyEntityMapper.toCompany(companyEntity)));
    }

    @DisplayName("상장된 국가로 기업 획득")
    @Test
    void getByCountryListedTest() {
        CompanyEntity companyEntity = createCompanyEntity();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByCountryListed(companyEntity.getCountryListed())).thenReturn(List.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByCountryListed(Country.valueOf(companyEntity.getCountryListed()))).isEqualTo(List.of(companyEntityMapper.toCompany(companyEntity)));
    }

    @DisplayName("기업 규모로 기업 획득")
    @Test
    void getByScaleTest() {
        CompanyEntity companyEntity = createCompanyEntity();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByScale(companyEntity.getScale())).thenReturn(List.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByScale(Scale.valueOf(companyEntity.getScale()))).isEqualTo(List.of(companyEntityMapper.toCompany(companyEntity)));
    }

    @DisplayName("1차 업종으로 기업 획득")
    @Test
    void getByFirstCategoryTest() {
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        CompanyEntity companyEntity = createCompanyEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateSecondCategory(secondCategoryEntity);
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByFirstCategory(firstCategoryEntity)).thenReturn(List.of(companyEntity));
        when(fcEntityRepository.findByNumber(firstCategoryEntity.getNumber())).thenReturn(Optional.of(firstCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);
        fcEntityRepository.save(firstCategoryEntity);
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByFirstCategory(fcEntityMapper.toFirstCategory(firstCategoryEntity))).isEqualTo(List.of(companyEntityMapper.toCompany(companyEntity)));
    }

    @DisplayName("2차 업종으로 기업 획득")
    @Test
    void getBySecondCategoryTest() {
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        FirstCategoryEntity firstCategoryEntity = createNumberedFirstCategoryEntity();
        CompanyEntity companyEntity = createCompanyEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateSecondCategory(secondCategoryEntity);
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findBySecondCategory(secondCategoryEntity)).thenReturn(List.of(companyEntity));
        when(scEntityRepository.findByNumber(secondCategoryEntity.getNumber())).thenReturn(Optional.of(secondCategoryEntity));

        // when
        scEntityRepository.save(secondCategoryEntity);
        fcEntityRepository.save(firstCategoryEntity);
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getBySecondCategory(scEntityMapper.toSecondCategory(secondCategoryEntity))).isEqualTo(List.of(companyEntityMapper.toCompany(companyEntity)));
    }

    @DisplayName("코드로 기업 획득")
    @Test
    void getByCodeTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        String code = companyEntity.getCode();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByCode(code)).thenReturn(Optional.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByCode(code).orElseThrow()).isEqualTo(companyEntityMapper.toCompany(companyEntity));
    }

    @DisplayName("한글명으로 기업 획득")
    @Test
    void getByKoreanNameTest() {
        CompanyEntity companyEntity = createCompanyEntity();
        String koreanName = companyEntity.getKoreanName();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByKoreanName(koreanName)).thenReturn(Optional.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByKoreanName(koreanName).orElseThrow()).isEqualTo(companyEntityMapper.toCompany(companyEntity));
    }

    @DisplayName("영문명으로 기업 획득")
    @Test
    void getByEnglishNameTest() {
        CompanyEntity companyEntity = createCompanyEntity();
        String englishName = companyEntity.getEnglishName();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByEnglishName(englishName).orElseThrow()).isEqualTo(companyEntityMapper.toCompany(companyEntity));
    }

    @DisplayName("상장된 이름으로 기업 획득")
    @Test
    void getByNameListedTest() {
        CompanyEntity companyEntity = createCompanyEntity();
        String nameListed = companyEntity.getNameListed();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findByNameListed(nameListed)).thenReturn(Optional.of(companyEntity));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(companyEntityService.getByNameListed(nameListed).orElseThrow()).isEqualTo(companyEntityMapper.toCompany(companyEntity));
    }

    @DisplayName("기업 삽입")
    @Test
    void insertTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategoryEntity = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategoryEntity = companyEntity.getSecondCategory();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        Company company = companyEntityMapper.toCompany(companyEntity);
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(companyEntityRepository.existsByCode(companyEntity.getCode())).thenReturn(false);
        when(fcEntityRepository.findByNumber(firstCategoryEntity.getNumber())).thenReturn(Optional.of(firstCategoryEntity));
        when(scEntityRepository.findByNumber(secondCategoryEntity.getNumber())).thenReturn(Optional.of(secondCategoryEntity));
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.findAll()).thenReturn(List.of(companyEntity));
        fcEntityRepository.save(firstCategoryEntity);
        scEntityRepository.save(secondCategoryEntity);

        // when
        companyEntityService.insert(company);

        // then
        assertThat(companyEntityService.getAll()).isEqualTo(List.of(company));
    }

    @DisplayName("이미 존재하는 코드로 기업 삽입")
    @Test
    void insertAlreadyExistedCodeTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategoryEntity = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategoryEntity = companyEntity.getSecondCategory();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.existsByCode(companyEntity.getCode())).thenReturn(true);

        // when
        fcEntityRepository.save(firstCategoryEntity);
        scEntityRepository.save(secondCategoryEntity);
        companyEntityRepository.save(companyEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> companyEntityService.insert(Company.builder()
                        .company(anotherCompany).code(companyEntity.getCode()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, CODE, companyEntity.getCode(), CompanyEntity.class));
    }

    @DisplayName("기업 갱신")
    @Test
    void updateTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategoryEntity = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategoryEntity = companyEntity.getSecondCategory();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        Company companyUpdated = Company.builder().company(anotherCompany).code(companyEntity.getCode()).firstCategoryNumber(companyEntity.getFirstCategory().getNumber()).secondCategoryNumber(companyEntity.getSecondCategory().getNumber()).build();
        when(fcEntityRepository.findByNumber(companyUpdated.getFirstCategoryNumber())).thenReturn(Optional.of(firstCategoryEntity));
        when(scEntityRepository.findByNumber(companyUpdated.getSecondCategoryNumber())).thenReturn(Optional.of(secondCategoryEntity));
        CompanyEntity companyEntityUpdated = companyEntityMapper.toCompanyEntity(companyUpdated, fcEntityRepository, scEntityRepository);
        when(fcEntityRepository.save(firstCategoryEntity)).thenReturn(firstCategoryEntity);
        when(scEntityRepository.save(secondCategoryEntity)).thenReturn(secondCategoryEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.existsByCode(companyUpdated.getCode())).thenReturn(true);
        when(companyEntityRepository.findAll()).thenReturn(List.of(companyEntityUpdated));
        fcEntityRepository.save(firstCategoryEntity);
        scEntityRepository.save(secondCategoryEntity);
        companyEntityRepository.save(companyEntity);

        // when
        companyEntityService.update(companyUpdated);

        // then
        assertThat(companyEntityService.getAll()).isEqualTo(List.of(companyUpdated));
    }

    @DisplayName("발견되지 않는 코드로 기업 갱신")
    @Test
    void updateNotFoundCodeTest() {
        // given & when
        CompanyEntity companyEntity = createCompanyEntity();
        when(companyEntityRepository.existsByCode(companyEntity.getCode())).thenReturn(false);

        // then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                companyEntityService.update(companyEntityMapper.toCompany(companyEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, CODE, companyEntity.getCode(), CompanyEntity.class));
    }

    @DisplayName("코드로 기업 제거")
    @Test
    void removeByCodeTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        String code = companyEntity.getCode();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.existsByCode(code)).thenReturn(true);
        when(companyEntityRepository.findByCode(code)).thenReturn(Optional.of(companyEntity));
        when(cacEntityRepository.findByCompany(companyEntity)).thenReturn(Collections.emptyList());
        doNothing().when(companyEntityRepository).deleteByCode(code);
        when(companyEntityRepository.findAll()).thenReturn(Collections.emptyList());
        companyEntityRepository.save(companyEntity);

        // when
        companyEntityService.removeByCode(code);

        // then
        assertThat(companyEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 코드로 기업 제거")
    @Test
    void removeByNotFoundCodeTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        String code = companyEntity.getCode();

        // when
        when(companyEntityRepository.existsByCode(code)).thenReturn(false);

        // then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> companyEntityService.removeByCode(code));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, CODE, code, CompanyEntity.class));
    }

    @DisplayName("코드로 기업 기사와 기업 간 매퍼에 포함된 기업 제거")
    @Test
    void removeByCodeInMapperTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        String code = companyEntity.getCode();
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyEntityRepository.existsByCode(code)).thenReturn(true);
        when(companyEntityRepository.findByCode(code)).thenReturn(Optional.of(companyEntity));
        when(cacEntityRepository.findByCompany(companyEntity)).thenReturn(List.of(createCompanyArticleCompanyEntity()));

        // when
        companyEntityRepository.save(companyEntity);

        // then
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> companyEntityService.removeByCode(code));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, CODE, code, CompanyEntity.class));
    }
}