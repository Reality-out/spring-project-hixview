package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.service.supers.ServiceWithNumberIdentifier;

import java.util.List;

public interface CompanyArticleCompanyService extends ServiceWithNumberIdentifier<CompanyArticleCompany> {
    List<CompanyArticleCompany> getByCompanyArticle(CompanyArticle article);

    List<CompanyArticleCompany> getByCompany(Company company);
}
