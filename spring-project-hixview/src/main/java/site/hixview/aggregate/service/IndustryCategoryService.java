package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.service.supers.CategorySuperService;
import site.hixview.aggregate.service.supers.OnlyGetAllowedService;

public interface IndustryCategoryService extends OnlyGetAllowedService<IndustryCategory>, CategorySuperService<IndustryCategory> {
}
