package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.CompanyArticleCompanyMapperDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticleCompanyMapper implements ConvertibleToDto<CompanyArticleCompanyMapperDto> {
    private final Long number;
    private final Long articleNumber;
    private final String companyCode;

    @Override
    public CompanyArticleCompanyMapperDto toDto() {
        CompanyArticleCompanyMapperDto mapperDto = new CompanyArticleCompanyMapperDto();
        mapperDto.setNumber(number);
        mapperDto.setArticleNumber(articleNumber);
        mapperDto.setCompanyCode(companyCode);
        return mapperDto;
    }

    public static final class CompanyArticleCompanyMapperBuilder {
        private Long number;
        private Long articleNumber;
        private String companyCode;

        public CompanyArticleCompanyMapperBuilder mapper(final CompanyArticleCompanyMapper mapper) {
            this.number = mapper.getNumber();
            this.articleNumber = mapper.getArticleNumber();
            this.companyCode = mapper.getCompanyCode();
            return this;
        }

        public CompanyArticleCompanyMapperBuilder mapperDto(final CompanyArticleCompanyMapperDto mapperDto) {
            this.number = mapperDto.getNumber();
            this.articleNumber = mapperDto.getArticleNumber();
            this.companyCode = mapperDto.getCompanyCode();
            return this;
        }
    }
}
