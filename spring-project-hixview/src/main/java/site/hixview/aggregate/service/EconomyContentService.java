package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

import java.util.Optional;

public interface EconomyContentService extends CrudAllowedServiceWithNumberId<EconomyContent> {
    Optional<EconomyContent> getByName(String name);
}
