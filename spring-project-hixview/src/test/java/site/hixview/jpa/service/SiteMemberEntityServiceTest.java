package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.jpa.mapper.SiteMemberEntityMapper;
import site.hixview.jpa.mapper.SiteMemberEntityMapperImpl;
import site.hixview.jpa.repository.SiteMemberEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.SiteMemberEntityTestUtils;
import site.hixview.support.spring.util.SiteMemberTestUtils;

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
import static site.hixview.aggregate.vo.WordCamel.ID;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@OnlyRealServiceContext
@Slf4j
class SiteMemberEntityServiceTest implements SiteMemberEntityTestUtils, SiteMemberTestUtils {

    private final SiteMemberEntityService siteMemberEntityService;
    private final SiteMemberEntityRepository siteMemberEntityRepository;

    private final SiteMemberEntityMapper mapper = new SiteMemberEntityMapperImpl();

    @Autowired
    SiteMemberEntityServiceTest(SiteMemberEntityService siteMemberEntityService, SiteMemberEntityRepository siteMemberEntityRepository) {
        this.siteMemberEntityService = siteMemberEntityService;
        this.siteMemberEntityRepository = siteMemberEntityRepository;
    }

    @DisplayName("모든 사이트 회원 획득")
    @Test
    void getAllTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.findById(siteMemberEntity.getId())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findAll()).thenReturn(List.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(List.of(siteMember));
    }

    @DisplayName("이름으로 사이트 회원 획득")
    @Test
    void getByNameTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        String name = siteMemberEntity.getName();
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.findById(siteMemberEntity.getId())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findByName(name)).thenReturn(List.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getByName(name)).isEqualTo(List.of(siteMember));
    }

    @DisplayName("번호로 사이트 회원 획득")
    @Test
    void getByNumberTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        Long number = siteMemberEntity.getNumber();
        when(siteMemberEntityRepository.existsByNumber(number)).thenReturn(false);
        when(siteMemberEntityRepository.findById(siteMemberEntity.getId())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findByNumber(number)).thenReturn(Optional.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getByNumber(number).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("ID로 사이트 회원 획득")
    @Test
    void getByIdTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        String id = siteMemberEntity.getId();
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.findById(id))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(siteMemberEntity));
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getById(id).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("ID와 PW로 사이트 회원 획득")
    @Test
    void getByIdAndPwTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        String id = siteMemberEntity.getId();
        String pw = siteMemberEntity.getPw();
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.findById(id)).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.findByIdAndPw(id, pw)).thenReturn(Optional.of(siteMemberEntity));
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getByIdAndPw(id, pw).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("이메일로 사이트 회원 획득")
    @Test
    void getByEmailTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        String email = siteMemberEntity.getEmail();
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.findById(email)).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.findByEmail(email)).thenReturn(Optional.of(siteMemberEntity));
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getByEmail(email).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("사이트 회원 삽입")
    @Test
    void insertTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        SiteMemberEntity anotherSiteMemberEntity = new SiteMemberEntity(
                anotherSiteMember.getNumber(), anotherSiteMember.getId(), anotherSiteMember.getPw(), anotherSiteMember.getName(), anotherSiteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(any())).thenReturn(false);
        when(siteMemberEntityRepository.findById((String) any())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity).thenReturn(anotherSiteMemberEntity);
        when(siteMemberEntityRepository.findAll()).thenReturn(List.of(siteMemberEntity, anotherSiteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);
        siteMemberEntityService.insert(anotherSiteMember);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(List.of(siteMember, anotherSiteMember));
    }

    @DisplayName("이미 존재하는 번호로 사이트 회원 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        Long number = siteMemberEntity.getNumber();
        SiteMemberEntity siteMemberEntityExistedNumber = new SiteMemberEntity(
                number, anotherSiteMember.getId(), anotherSiteMember.getPw(), anotherSiteMember.getName(), anotherSiteMember.getEmail());
        SiteMember siteMemberExistedNumber = mapper.toSiteMember(siteMemberEntityExistedNumber);
        when(siteMemberEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(siteMemberEntityRepository.findById(siteMemberEntity.getId())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> siteMemberEntityService.insert(siteMemberExistedNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, SiteMemberEntity.class));
    }

    @DisplayName("이미 존재하는 ID로 사이트 회원 삽입")
    @Test
    void insertAlreadyExistedIdTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        String id = siteMemberEntity.getId();
        SiteMemberEntity siteMemberEntityExistedId = createAnotherSiteMemberEntity();
        siteMemberEntityExistedId.updateId(id);
        SiteMember siteMemberExistedName = mapper.toSiteMember(siteMemberEntityExistedId);
        when(siteMemberEntityRepository.existsByNumber(any())).thenReturn(false);
        when(siteMemberEntityRepository.findById(id)).thenReturn(Optional.empty()).thenReturn(Optional.of(siteMemberEntity));
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> siteMemberEntityService.insert(siteMemberExistedName));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ID, id, SiteMemberEntity.class));
    }

    @DisplayName("사이트 회원 갱신")
    @Test
    void updateTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        SiteMember updatedSiteMember = SiteMember.builder()
                .siteMember(anotherSiteMember).number(siteMemberEntity.getNumber()).build();
        SiteMemberEntity updatedSiteMemberEntity = mapper.toSiteMemberEntity(updatedSiteMember);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity).thenReturn(updatedSiteMemberEntity);
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber())).thenReturn(true);
        when(siteMemberEntityRepository.findById((String) any())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.findByNumber(siteMemberEntity.getNumber())).thenReturn(Optional.of(siteMemberEntity));
        when(siteMemberEntityRepository.findAll()).thenReturn(List.of(updatedSiteMemberEntity));
        siteMemberEntityRepository.save(siteMemberEntity);

        // when
        SiteMember updateSiteMember = siteMemberEntityService.update(updatedSiteMember);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(List.of(updateSiteMember));
    }

    @DisplayName("존재하지 않는 번호로 사이트 회원 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        SiteMemberEntity siteMemberEntityNotFoundNumber = createAnotherSiteMemberEntity();
        Long notFoundNumber = siteMemberEntityNotFoundNumber.getNumber();
        SiteMember siteMemberNotFoundNumber = mapper.toSiteMember(siteMemberEntityNotFoundNumber);
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.existsByNumber(notFoundNumber)).thenReturn(false);
        when(siteMemberEntityRepository.findById(siteMemberEntity.getId())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> siteMemberEntityService.update(siteMemberNotFoundNumber));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, notFoundNumber, SiteMemberEntity.class));
    }

    @DisplayName("이미 존재하는 Id로 사이트 회원 갱신")
    @Test
    void updateAlreadyExistedEnglishNameTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        String id = siteMemberEntity.getId();
        SiteMemberEntity siteMemberEntityExistedNumber = createAnotherSiteMemberEntity();
        siteMemberEntityExistedNumber.updateId(id);
        SiteMember siteMemberExistedName = mapper.toSiteMember(siteMemberEntityExistedNumber);
        when(siteMemberEntityRepository.existsByNumber(siteMemberEntity.getNumber()))
                .thenReturn(false).thenReturn(true);
        when(siteMemberEntityRepository.findById(id))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(siteMemberEntity));
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> siteMemberEntityService.insert(siteMemberExistedName));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, ID, id, SiteMemberEntity.class));
    }

    @DisplayName("번호로 사이트 회원 제거")
    @Test
    void removeByNumberTest() {
        // given
        SiteMemberEntity siteMemberEntity = createNumberedSiteMemberEntity();
        Long number = siteMemberEntity.getNumber();
        when(siteMemberEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(siteMemberEntityRepository.findById(siteMemberEntity.getId())).thenReturn(Optional.empty());
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        doNothing().when(siteMemberEntityRepository).deleteByNumber(number);
        when(siteMemberEntityRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        siteMemberEntityService.insert(siteMember);
        siteMemberEntityService.removeByNumber(number);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(Collections.emptyList());
    }

    @DisplayName("존재하지 않는 번호로 사이트 회원 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        Long number = siteMember.getNumber();
        when(siteMemberEntityRepository.existsByNumber(number)).thenReturn(false);

        // when
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> siteMemberEntityService.removeByNumber(number));

        // then
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, SiteMemberEntity.class));
    }
}