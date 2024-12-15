package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

import java.util.List;

public interface CompanyArticleCompanyService extends CrudAllowedServiceWithNumberId<CompanyArticleCompany> {
    List<CompanyArticleCompany> getByCompanyArticle(CompanyArticle article);

    List<CompanyArticleCompany> getByCompany(Company company);
}
