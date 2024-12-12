package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.service.supers.ServiceWithNumberIdentifier;

import java.util.Optional;

public interface EconomyContentService extends ServiceWithNumberIdentifier<EconomyContent> {
    Optional<EconomyContent> getByName(String name);
}
