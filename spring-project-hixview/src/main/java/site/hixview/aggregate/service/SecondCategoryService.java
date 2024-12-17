package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.service.supers.CategorySuperService;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

public interface SecondCategoryService extends CrudAllowedServiceWithNumberId<SecondCategory>, CategorySuperService<SecondCategory> {
}
