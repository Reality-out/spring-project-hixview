package site.hixview.aggregate.mapper.support;

import org.mapstruct.Named;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

interface ArticleMapperSupport {
    @Named("subjectCountryToDomain")
    default Country subjectCountryToDomain(String country) {
        return Country.valueOf(country);
    }

    @Named("subjectCountryToDto")
    default String subjectCountryToDto(Country country) {
        return country.name();
    }

    @Named("importanceToDomain")
    default Importance importanceToDomain(String importance) {
        return Importance.valueOf(importance);
    }

    @Named("importanceToDto")
    default String importanceToDto(Importance importance) {
        return importance.name();
    }
}
