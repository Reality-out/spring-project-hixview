package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.service.IndustryCategoryService;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.mapper.IndustryCategoryEntityMapperImpl;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndustryCategoryEntityService implements IndustryCategoryService {

    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final IndustryCategoryEntityMapperImpl mapper = new IndustryCategoryEntityMapperImpl();

    @Override
    public List<IndustryCategory> getAll() {
        return industryCategoryEntityRepository.findAll().stream().map(mapper::toIndustryCategory).toList();
    }

    @Override
    public Optional<IndustryCategory> getByNumber(Long number) {
        return getOptionalIndustryCategory(industryCategoryEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<IndustryCategory> getByKoreanName(String koreanName) {
        return getOptionalIndustryCategory(industryCategoryEntityRepository.findByKoreanName(koreanName).orElse(null));
    }

    @Override
    public Optional<IndustryCategory> getByEnglishName(String englishName) {
        return getOptionalIndustryCategory(industryCategoryEntityRepository.findByEnglishName(englishName).orElse(null));
    }

    private Optional<IndustryCategory> getOptionalIndustryCategory(IndustryCategoryEntity industryCategoryEntity) {
        if (industryCategoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toIndustryCategory(industryCategoryEntity));
    }
}
