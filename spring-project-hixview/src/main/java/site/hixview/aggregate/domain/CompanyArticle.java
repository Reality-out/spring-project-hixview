package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.CompanyArticleDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticle implements ConvertibleToDto<CompanyArticleDto> {

    private final Long number;

    @Override
    public CompanyArticleDto toDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(number);
        return companyArticleDto;
    }

    public static final class CompanyArticleBuilder {
        private Long number;

        public CompanyArticleBuilder companyArticle(final CompanyArticle companyArticle) {
            this.number = companyArticle.getNumber();
            return this;
        }

        public CompanyArticleBuilder companyArticleDto(final CompanyArticleDto companyArticleDto) {
            this.number = companyArticleDto.getNumber();
            return this;
        }
    }
}
