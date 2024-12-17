package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.service.supers.CategorySuperService;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

public interface FirstCategoryService extends CrudAllowedServiceWithNumberId<FirstCategory>, CategorySuperService<FirstCategory> {
}
