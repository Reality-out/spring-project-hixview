package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.service.supers.ServiceWithNumberIdentifier;

import java.util.Optional;

public interface PressService extends ServiceWithNumberIdentifier<Press> {
    Optional<Press> getByKoreanName(String koreanName);

    Optional<Press> getByEnglishName(String englishName);
}
