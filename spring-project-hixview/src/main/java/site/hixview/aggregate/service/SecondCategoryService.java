package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.service.supers.CategorySuperService;
import site.hixview.aggregate.service.supers.CrudAllowedService;

public interface SecondCategoryService extends CrudAllowedService<SecondCategory>, CategorySuperService<SecondCategory> {
}
