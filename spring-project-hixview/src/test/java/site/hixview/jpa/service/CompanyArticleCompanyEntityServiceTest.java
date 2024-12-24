package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.CompanyEntityRepository;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.CompanyArticleCompanyEntityTestUtils;
import site.hixview.support.jpa.util.CompanyArticleEntityTestUtils;
import site.hixview.support.spring.util.CompanyArticleCompanyTestUtils;

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
class CompanyArticleCompanyEntityServiceTest implements CompanyArticleEntityTestUtils, CompanyArticleCompanyEntityTestUtils, CompanyArticleCompanyTestUtils {

    private final CompanyArticleCompanyEntityService cacEntityService;
    private final CompanyEntityRepository companyEntityRepository;
    private final CompanyArticleEntityRepository caEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;

    private final CompanyArticleCompanyEntityMapper cacEntityMapper = new CompanyArticleCompanyEntityMapperImpl();
    private final CompanyArticleEntityMapper caEntityMapper = new CompanyArticleEntityMapperImpl();
    private final CompanyEntityMapper companyEntityMapper = new CompanyEntityMapperImpl();

    @Autowired
    CompanyArticleCompanyEntityServiceTest(CompanyArticleCompanyEntityService cacEntityService, CompanyEntityRepository companyEntityRepository, CompanyArticleEntityRepository caEntityRepository, CompanyArticleCompanyEntityRepository cacEntityRepository) {
        this.cacEntityService = cacEntityService;
        this.companyEntityRepository = companyEntityRepository;
        this.caEntityRepository = caEntityRepository;
        this.cacEntityRepository = cacEntityRepository;
    }

    @DisplayName("모든 기업 기사와 기업 간 매퍼 획득")
    @Test
    void getAllTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createCompanyArticleCompanyEntity();
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.findAll()).thenReturn(List.of(cacEntity));

        // when
        cacEntityRepository.save(cacEntity);

        // then
        assertThat(cacEntityService.getAll()).isEqualTo(List.of(cacEntityMapper.toCompanyArticleCompany(cacEntity)));
    }


    @DisplayName("번호로 기업 기사와 기업 간 매퍼 획득")
    @Test
    void getByNumberTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        Long number = cacEntity.getNumber();
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.findByNumber(number)).thenReturn(Optional.of(cacEntity));

        // when
        cacEntityRepository.save(cacEntity);

        // then
        assertThat(cacEntityService.getByNumber(number).orElseThrow()).isEqualTo(cacEntityMapper.toCompanyArticleCompany(cacEntity));
    }

    @DisplayName("기업 기사로 기업 기사와 기업 간 매퍼 획득")
    @Test
    void getByCompanyArticleTest() {
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.findByCompanyArticle(caEntity)).thenReturn(List.of(cacEntity));
        when(caEntityRepository.findByNumber(caEntity.getArticle().getNumber())).thenReturn(Optional.of(caEntity));

        // when
        cacEntityRepository.save(cacEntity);

        // then
        assertThat(cacEntityService.getByCompanyArticle(caEntityMapper.toCompanyArticle(caEntity, cacEntityRepository))).isEqualTo(List.of(cacEntityMapper.toCompanyArticleCompany(cacEntity)));
    }

    @DisplayName("기업으로 기업 기사와 기업 간 매퍼 획득")
    @Test
    void getByArticleTest() {
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyEntity companyEntity = cacEntity.getCompany();
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.findByCompany(companyEntity)).thenReturn(List.of(cacEntity));
        when(companyEntityRepository.findByCode(companyEntity.getCode())).thenReturn(Optional.of(companyEntity));

        // when
        cacEntityRepository.save(cacEntity);

        // then
        assertThat(cacEntityService.getByCompany(companyEntityMapper.toCompany(companyEntity))).isEqualTo(List.of(cacEntityMapper.toCompanyArticleCompany(cacEntity)));
    }

    @DisplayName("기업 기사와 기업 간 매퍼 삽입")
    @Test
    void insertTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        CompanyEntity companyEntity = cacEntity.getCompany();
        CompanyArticleCompany companyArticleCompany = cacEntityMapper.toCompanyArticleCompany(cacEntity);
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(cacEntityRepository.existsByNumber(companyArticleCompany.getNumber())).thenReturn(false);
        when(cacEntityRepository.findByCompanyArticleAndCompany(cacEntity.getCompanyArticle(), companyEntity)).thenReturn(Optional.empty());
        when(caEntityRepository.findByNumber(companyArticleCompany.getArticleNumber())).thenReturn(Optional.of(caEntity));
        when(companyEntityRepository.findByCode(companyArticleCompany.getCompanyCode())).thenReturn(Optional.of(companyEntity));
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.findAll()).thenReturn(List.of(cacEntity));
        caEntityRepository.save(caEntity);
        companyEntityRepository.save(companyEntity);

        // when
        cacEntityService.insert(companyArticleCompany);

        // then
        assertThat(cacEntityService.getAll()).isEqualTo(List.of(companyArticleCompany));
    }

    @DisplayName("이미 존재하는 번호로 기업 기사와 기업 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        CompanyEntity companyEntity = cacEntity.getCompany();
        CompanyArticleCompany companyArticleCompany = cacEntityMapper.toCompanyArticleCompany(cacEntity);
        Long number = companyArticleCompany.getNumber();
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.existsByNumber(number)).thenReturn(true);

        // when
        caEntityRepository.save(caEntity);
        companyEntityRepository.save(companyEntity);
        cacEntityRepository.save(cacEntity);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> cacEntityService.insert(CompanyArticleCompany.builder()
                        .companyArticleCompany(anotherCompanyArticleCompany).number(number).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, CompanyArticleCompanyEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 기업 기사와 기업 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberPairTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        CompanyEntity companyEntity = cacEntity.getCompany();
        CompanyArticleCompany companyArticleCompany = CompanyArticleCompany.builder().companyArticleCompany(cacEntityMapper.toCompanyArticleCompany(cacEntity)).articleNumber(caEntity.getArticle().getNumber()).build();
        CompanyArticleCompanyEntity cacEntityExistedNumberPair = new CompanyArticleCompanyEntity(anotherCompanyArticleCompany.getNumber(), caEntity, companyEntity);
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.existsByNumber(companyArticleCompany.getNumber())).thenReturn(false);
        when(cacEntityRepository.findByCompanyArticleAndCompany(caEntity, companyEntity)).thenReturn(Optional.of(cacEntityExistedNumberPair));
        when(caEntityRepository.findByNumber(companyArticleCompany.getArticleNumber())).thenReturn(Optional.of(caEntity));
        when(companyEntityRepository.findByCode(companyArticleCompany.getCompanyCode())).thenReturn(Optional.of(companyEntity));

        // when
        caEntityRepository.save(caEntity);
        companyEntityRepository.save(companyEntity);
        cacEntityRepository.save(cacEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> cacEntityService.insert(CompanyArticleCompany.builder().
                        companyArticleCompany(cacEntityMapper.toCompanyArticleCompany(cacEntityExistedNumberPair)).
                        articleNumber(caEntity.getArticle().getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, caEntity.getArticle().getNumber(), CompanyArticleEntity.class,
                COMPANY_CODE, companyEntity.getCode(), CompanyEntity.class));
    }

    @DisplayName("기업 기사와 기업 간 매퍼 갱신")
    @Test
    void updateTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        CompanyEntity companyEntity = cacEntity.getCompany();
        CompanyArticleEntity caEntityUpdated = createAnotherCompanyArticleEntity();
        ArticleEntity articleEntityUpdated = createAnotherArticleEntity();
        articleEntityUpdated.updateNumber(anotherArticle.getNumber());
        caEntityUpdated.updateArticle(articleEntityUpdated);
        CompanyEntity companyEntityUpdated = createAnotherCompanyEntity();

        CompanyArticleCompany cacUpdated = CompanyArticleCompany.builder().number(cacEntity.getNumber()).articleNumber(articleEntityUpdated.getNumber()).companyCode(companyEntityUpdated.getCode()).build();
        when(caEntityRepository.findByNumber(caEntityUpdated.getArticle().getNumber())).thenReturn(Optional.of(caEntityUpdated));
        when(companyEntityRepository.findByCode(companyEntityUpdated.getCode())).thenReturn(Optional.of(companyEntityUpdated));
        CompanyArticleCompanyEntity cacEntityUpdated = cacEntityMapper.toCompanyArticleCompanyEntity(cacUpdated, caEntityRepository, companyEntityRepository);
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity).thenReturn(cacEntityUpdated);
        when(caEntityRepository.save(caEntityUpdated)).thenReturn(caEntityUpdated);
        when(companyEntityRepository.save(companyEntityUpdated)).thenReturn(companyEntityUpdated);
        when(cacEntityRepository.existsByNumber(cacUpdated.getNumber())).thenReturn(true);
        when(cacEntityRepository.findByCompanyArticleAndCompany(caEntityUpdated, companyEntityUpdated)).thenReturn(Optional.empty());
        when(cacEntityRepository.findByNumber(cacEntityUpdated.getNumber())).thenReturn(Optional.of(cacEntityUpdated));
        when(caEntityRepository.findByNumber(cacUpdated.getArticleNumber())).thenReturn(Optional.of(caEntityUpdated));
        when(companyEntityRepository.findByCode(cacUpdated.getCompanyCode())).thenReturn(Optional.of(companyEntityUpdated));
        when(cacEntityRepository.findAll()).thenReturn(List.of(cacEntityUpdated));

        caEntityRepository.save(caEntity);
        companyEntityRepository.save(companyEntity);
        cacEntityRepository.save(cacEntity);
        caEntityRepository.save(caEntityUpdated);
        companyEntityRepository.save(companyEntityUpdated);

        // when
        cacEntityService.update(cacUpdated);

        // then
        assertThat(cacEntityService.getAll()).isEqualTo(List.of(CompanyArticleCompany.builder().companyArticleCompany(cacUpdated).articleNumber(null).build()));
    }

    @DisplayName("발견되지 않는 번호로 기업 기사와 기업 간 매퍼 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        Long number = cacEntity.getNumber();

        // when
        when(cacEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                cacEntityService.update(cacEntityMapper.toCompanyArticleCompany(cacEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, CompanyArticleCompanyEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 기업 기사와 기업 간 매퍼 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        CompanyEntity companyEntity = cacEntity.getCompany();

        CompanyArticleCompany cacUpdated = CompanyArticleCompany.builder().number(cacEntity.getNumber()).articleNumber(caEntity.getArticle().getNumber()).companyCode(companyEntity.getCode()).build();
        when(caEntityRepository.findByNumber(caEntity.getArticle().getNumber())).thenReturn(Optional.of(caEntity));
        when(companyEntityRepository.findByCode(companyEntity.getCode())).thenReturn(Optional.of(companyEntity));
        CompanyArticleCompanyEntity cacEntityUpdated = cacEntityMapper.toCompanyArticleCompanyEntity(cacUpdated, caEntityRepository, companyEntityRepository);
        when(caEntityRepository.save(caEntity)).thenReturn(caEntity);
        when(companyEntityRepository.save(companyEntity)).thenReturn(companyEntity);
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity).thenReturn(cacEntityUpdated);
        when(cacEntityRepository.existsByNumber(cacUpdated.getNumber())).thenReturn(true);
        when(cacEntityRepository.findByCompanyArticleAndCompany(caEntity, companyEntity)).thenReturn(Optional.of(cacEntity));
        when(cacEntityRepository.findByNumber(cacEntityUpdated.getNumber())).thenReturn(Optional.of(cacEntityUpdated));
        when(caEntityRepository.findByNumber(cacUpdated.getArticleNumber())).thenReturn(Optional.of(caEntity));
        when(companyEntityRepository.findByCode(cacUpdated.getCompanyCode())).thenReturn(Optional.of(companyEntity));

        // when
        caEntityRepository.save(caEntity);
        companyEntityRepository.save(companyEntity);
        cacEntityRepository.save(cacEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                cacEntityService.update(cacEntityService.update(cacUpdated)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ARTICLE_NUMBER, caEntity.getArticle().getNumber(), CompanyArticleEntity.class,
                COMPANY_CODE, companyEntity.getCode(), CompanyEntity.class));
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼 제거")
    @Test
    void removeByNumberTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        when(cacEntityRepository.save(cacEntity)).thenReturn(cacEntity);
        when(cacEntityRepository.existsByNumber(cacEntity.getNumber())).thenReturn(true);
        doNothing().when(cacEntityRepository).deleteByNumber(cacEntity.getNumber());
        when(cacEntityRepository.findAll()).thenReturn(Collections.emptyList());
        cacEntityRepository.save(cacEntity);

        // when
        cacEntityService.removeByNumber(cacEntity.getNumber());

        // then
        assertThat(cacEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 기업 기사와 기업 간 매퍼 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        CompanyArticleCompanyEntity cacEntity = createNumberedCompanyArticleCompanyEntity();
        Long number = cacEntity.getNumber();

        // when
        when(cacEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> cacEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, CompanyArticleCompanyEntity.class));
    }
}