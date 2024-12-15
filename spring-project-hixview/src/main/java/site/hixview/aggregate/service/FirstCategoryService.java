package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.service.supers.CategorySuperService;
import site.hixview.aggregate.service.supers.CrudAllowedService;

public interface FirstCategoryService extends CrudAllowedService<FirstCategory>, CategorySuperService<FirstCategory> {
}
