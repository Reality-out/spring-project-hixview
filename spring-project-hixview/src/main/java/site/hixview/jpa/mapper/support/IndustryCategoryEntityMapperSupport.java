package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.jpa.entity.IndustryCategoryEntity;

public interface IndustryCategoryEntityMapperSupport {
    @AfterMapping
    default IndustryCategoryEntity afterMappingToEntity(
            @MappingTarget IndustryCategoryEntity industryCategoryEntity, IndustryCategory industryCategory) {
        return new IndustryCategoryEntity(industryCategory.getNumber(),
                industryCategory.getKoreanName(),
                industryCategory.getEnglishName());
    }
}
