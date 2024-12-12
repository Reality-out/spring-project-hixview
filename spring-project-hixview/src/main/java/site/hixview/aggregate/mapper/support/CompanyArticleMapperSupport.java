package site.hixview.aggregate.mapper.support;

import org.mapstruct.Named;

import java.util.List;

import static site.hixview.aggregate.util.JsonUtils.*;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_COMPANY_CODES_SNAKE;

public interface CompanyArticleMapperSupport extends ArticleMapperSupport {
    @Named("mappedEconomyContentNumbersToDomain")
    default List<String> mappedCompanyCodesToDomain(String mappedCompanyCodes) {
        return parseToStringList(mappedCompanyCodes, MAPPED_COMPANY_CODES_SNAKE);
    }

    @Named("mappedEconomyContentNumbersToDto")
    default String mappedCompanyCodesToDto(List<String> mappedCompanyCodes) {
        return mapStringList(mappedCompanyCodes, MAPPED_COMPANY_CODES_SNAKE);
    }
}