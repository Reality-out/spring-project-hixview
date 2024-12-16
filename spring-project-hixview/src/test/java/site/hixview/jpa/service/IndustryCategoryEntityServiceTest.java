package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.IndustryCategoryEntityTestUtils;
import site.hixview.support.spring.util.IndustryCategoryTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@OnlyRealServiceContext
@Slf4j
class IndustryCategoryEntityServiceTest implements IndustryCategoryEntityTestUtils, IndustryCategoryTestUtils {

    private final IndustryCategoryEntityService industryCategoryEntityService;
    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;

    @Autowired
    IndustryCategoryEntityServiceTest(IndustryCategoryEntityService industryCategoryEntityService, IndustryCategoryEntityRepository industryCategoryEntityRepository) {
        this.industryCategoryEntityService = industryCategoryEntityService;
        this.industryCategoryEntityRepository = industryCategoryEntityRepository;
    }

    @DisplayName("모든 산업 업종 획득")
    @Test
    void getAllTest() {
        // given
        IndustryCategoryEntity industryCategoryEntity = new IndustryCategoryEntity(
                industryCategory.getNumber(), industryCategory.getKoreanName(), industryCategory.getEnglishName());
        when(industryCategoryEntityRepository.save(any())).thenReturn(industryCategoryEntity);
        when(industryCategoryEntityRepository.findAll()).thenReturn(List.of(industryCategoryEntity));

        // when
        industryCategoryEntityRepository.save(industryCategoryEntity);

        // then
        assertThat(industryCategoryEntityService.getAll()).isEqualTo(List.of(industryCategory));
    }

    @DisplayName("번호로 산업 업종 획득")
    @Test
    void getByNumberTest() {
        // given
        Long number = industryCategory.getNumber();
        IndustryCategoryEntity industryCategoryEntity = new IndustryCategoryEntity(
                industryCategory.getNumber(), industryCategory.getKoreanName(), industryCategory.getEnglishName());
        when(industryCategoryEntityRepository.save(any())).thenReturn(industryCategoryEntity);
        when(industryCategoryEntityRepository.findByNumber(number)).thenReturn(Optional.of(industryCategoryEntity));

        // when
        industryCategoryEntityRepository.save(industryCategoryEntity);

        // then
        assertThat(industryCategoryEntityService.getByNumber(number).orElseThrow()).isEqualTo(industryCategory);
    }

    @DisplayName("한글명으로 산업 업종 획득")
    @Test
    void getByKoreanNameTest() {
        // given
        String koreanName = industryCategory.getKoreanName();
        IndustryCategoryEntity industryCategoryEntity = new IndustryCategoryEntity(
                industryCategory.getNumber(), industryCategory.getKoreanName(), industryCategory.getEnglishName());
        when(industryCategoryEntityRepository.save(any())).thenReturn(industryCategoryEntity);
        when(industryCategoryEntityRepository.findByKoreanName(koreanName)).thenReturn(Optional.of(industryCategoryEntity));

        // when
        industryCategoryEntityRepository.save(industryCategoryEntity);

        // then
        assertThat(industryCategoryEntityService.getByKoreanName(koreanName).orElseThrow()).isEqualTo(industryCategory);
    }

    @DisplayName("영문명으로 산업 업종 획득")
    @Test
    void getByEnglishNameTest() {
        // given
        String englishName = industryCategory.getEnglishName();
        IndustryCategoryEntity industryCategoryEntity = new IndustryCategoryEntity(
                industryCategory.getNumber(), industryCategory.getKoreanName(), industryCategory.getEnglishName());
        when(industryCategoryEntityRepository.save(any())).thenReturn(industryCategoryEntity);
        when(industryCategoryEntityRepository.findByEnglishName(englishName)).thenReturn(Optional.of(industryCategoryEntity));

        // when
        industryCategoryEntityRepository.save(industryCategoryEntity);

        // then
        assertThat(industryCategoryEntityService.getByEnglishName(englishName).orElseThrow()).isEqualTo(industryCategory);
    }
}