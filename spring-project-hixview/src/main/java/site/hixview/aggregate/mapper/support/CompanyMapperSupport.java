package site.hixview.aggregate.mapper.support;

import org.mapstruct.Named;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

public interface CompanyMapperSupport {
    @Named("countryListedToDomain")
    default Country countryListedToDomain(String country) {
        return Country.valueOf(country);
    }

    @Named("scaleToDomain")
    default Scale scaleToDomain(String scale) {
        return Scale.valueOf(scale);
    }

    @Named("countryListedToDto")
    default String countryListedToDto(Country country) {
        return country.name();
    }

    @Named("scaleToDto")
    default String scaleToDto(Scale scale) {
        return scale.name();
    }
}
