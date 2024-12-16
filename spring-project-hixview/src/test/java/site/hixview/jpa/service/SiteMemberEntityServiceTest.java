package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.jpa.mapper.SiteMemberEntityMapperImpl;
import site.hixview.jpa.repository.SiteMemberEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.SiteMemberEntityTestUtils;
import site.hixview.support.spring.util.SiteMemberTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@OnlyRealServiceContext
@Slf4j
class SiteMemberEntityServiceTest implements SiteMemberEntityTestUtils, SiteMemberTestUtils {

    private final SiteMemberEntityService siteMemberEntityService;
    private final SiteMemberEntityRepository siteMemberEntityRepository;

    private final SiteMemberEntityMapperImpl mapper = new SiteMemberEntityMapperImpl();

    @Autowired
    SiteMemberEntityServiceTest(SiteMemberEntityService siteMemberEntityService, SiteMemberEntityRepository siteMemberEntityRepository) {
        this.siteMemberEntityService = siteMemberEntityService;
        this.siteMemberEntityRepository = siteMemberEntityRepository;
    }

    @DisplayName("모든 사이트 회원 획득")
    @Test
    void getAllTest() {
        // given
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                siteMember.getNumber(), siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findAll()).thenReturn(List.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(List.of(siteMember));
    }

    @DisplayName("번호로 사이트 회원 획득")
    @Test
    void getByNumberTest() {
        // given
        Long number = siteMember.getNumber();
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                number, siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
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
        String id = siteMember.getId();
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                siteMember.getNumber(), id, siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findById(id)).thenReturn(Optional.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getById(id).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("ID와 PW로 사이트 회원 획득")
    @Test
    void getByIdAndPwTest() {
        // given
        String id = siteMember.getId();
        String pw = siteMember.getPw();
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                siteMember.getNumber(), id, pw, siteMember.getName(), siteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findByIdAndPw(id, pw)).thenReturn(Optional.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getByIdAndPw(id, pw).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("이메일로 사이트 회원 획득")
    @Test
    void getByEmailTest() {
        // given
        String email = siteMember.getEmail();
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                siteMember.getNumber(), siteMember.getId(), siteMember.getPw(), siteMember.getName(), email);
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        when(siteMemberEntityRepository.findByEmail(email)).thenReturn(Optional.of(siteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);

        // then
        assertThat(siteMemberEntityService.getByEmail(email).orElseThrow()).isEqualTo(siteMember);
    }

    @DisplayName("사이트 회원 삽입")
    @Test
    void insertTest() {
        // given
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                siteMember.getNumber(), siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
        SiteMemberEntity anotherSiteMemberEntity = new SiteMemberEntity(
                anotherSiteMember.getNumber(), anotherSiteMember.getId(), anotherSiteMember.getPw(), anotherSiteMember.getName(), anotherSiteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.existsByNumber(anotherSiteMember.getNumber())).thenReturn(false);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity).thenReturn(anotherSiteMemberEntity);
        when(siteMemberEntityRepository.findAll()).thenReturn(List.of(siteMemberEntity, anotherSiteMemberEntity));

        // when
        siteMemberEntityService.insert(siteMember);
        siteMemberEntityService.insert(anotherSiteMember);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(List.of(siteMember, anotherSiteMember));
    }

    @DisplayName("사이트 회원 갱신")
    @Test
    void updateTest() {
        // given
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                siteMember.getNumber(), siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
        SiteMember updatedSiteMember = SiteMember.builder()
                .siteMember(anotherSiteMember).number(siteMember.getNumber()).build();
        SiteMemberEntity updatedSiteMemberEntity = mapper.toSiteMemberEntity(updatedSiteMember);
        when(siteMemberEntityRepository.existsByNumber(siteMember.getNumber())).thenReturn(false).thenReturn(true);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity).thenReturn(updatedSiteMemberEntity);
        when(siteMemberEntityRepository.findAll()).thenReturn(List.of(updatedSiteMemberEntity));
        siteMemberEntityService.insert(siteMember);

        // when
        SiteMember updateSiteMember = siteMemberEntityService.update(updatedSiteMember);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(List.of(updateSiteMember));
    }

    @DisplayName("번호로 사이트 회원 제거")
    @Test
    void removeByNumberTest() {
        // given
        Long number = siteMember.getNumber();
        SiteMemberEntity siteMemberEntity = new SiteMemberEntity(
                number, siteMember.getId(), siteMember.getPw(), siteMember.getName(), siteMember.getEmail());
        when(siteMemberEntityRepository.existsByNumber(number)).thenReturn(false).thenReturn(true);
        when(siteMemberEntityRepository.save(any())).thenReturn(siteMemberEntity);
        doNothing().when(siteMemberEntityRepository).deleteByNumber(number);
        when(siteMemberEntityRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        siteMemberEntityService.insert(siteMember);
        siteMemberEntityService.removeByNumber(number);

        // then
        assertThat(siteMemberEntityService.getAll()).isEqualTo(Collections.emptyList());
    }
}