package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.PostEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.PostEntityTestUtils;
import site.hixview.support.spring.util.PostTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@OnlyRealServiceContext
@Slf4j
class PostEntityServiceTest implements PostEntityTestUtils, PostTestUtils {

    private final PostEntityService postEntityService;
    private final PostEntityRepository postEntityRepository;

    @Autowired
    PostEntityServiceTest(PostEntityService postEntityService, PostEntityRepository postEntityRepository) {
        this.postEntityService = postEntityService;
        this.postEntityRepository = postEntityRepository;
    }

    @DisplayName("모든 포스트 획득")
    @Test
    void getAllTest() {
        // given
        PostEntity postEntity = createNumberedPostEntity();
        when(postEntityRepository.save(any())).thenReturn(postEntity);
        when(postEntityRepository.findAll()).thenReturn(List.of(postEntity));

        // when
        postEntityRepository.save(postEntity);

        // then
        assertThat(postEntityService.getAll()).isEqualTo(List.of(post));
    }

    @DisplayName("번호로 포스트 획득")
    @Test
    void getByNumberTest() {
        // given
        PostEntity postEntity = createNumberedPostEntity();
        Long number = postEntity.getNumber();
        when(postEntityRepository.save(any())).thenReturn(postEntity);
        when(postEntityRepository.findByNumber(number)).thenReturn(Optional.of(postEntity));

        // when
        postEntityRepository.save(postEntity);

        // then
        assertThat(postEntityService.getByNumber(number).orElseThrow()).isEqualTo(post);
    }
}