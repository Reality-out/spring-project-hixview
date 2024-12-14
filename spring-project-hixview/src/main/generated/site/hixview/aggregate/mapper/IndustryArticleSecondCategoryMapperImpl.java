package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T11:41:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryArticleSecondCategoryMapperImpl implements IndustryArticleSecondCategoryMapper {

    @Override
    public IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto) {
        if ( industryArticleSecondCategoryDto == null ) {
            return null;
        }

        IndustryArticleSecondCategory.IndustryArticleSecondCategoryBuilder industryArticleSecondCategory = IndustryArticleSecondCategory.builder();

        industryArticleSecondCategory.number( industryArticleSecondCategoryDto.getNumber() );
        industryArticleSecondCategory.articleNumber( industryArticleSecondCategoryDto.getArticleNumber() );
        industryArticleSecondCategory.secondCategoryNumber( industryArticleSecondCategoryDto.getSecondCategoryNumber() );

        return industryArticleSecondCategory.build();
    }

    @Override
    public IndustryArticleSecondCategoryDto toIndustryArticleSecondCategoryDto(IndustryArticleSecondCategory industryArticleSecondCategory) {
        if ( industryArticleSecondCategory == null ) {
            return null;
        }

        IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto = new IndustryArticleSecondCategoryDto();

        industryArticleSecondCategoryDto.setNumber( industryArticleSecondCategory.getNumber() );
        industryArticleSecondCategoryDto.setArticleNumber( industryArticleSecondCategory.getArticleNumber() );
        industryArticleSecondCategoryDto.setSecondCategoryNumber( industryArticleSecondCategory.getSecondCategoryNumber() );

        return industryArticleSecondCategoryDto;
    }
}
