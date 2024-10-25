package site.hixview.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.IndustryArticle;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {
    private final ArticleMainService articleMainService;
    private final CompanyArticleService companyArticleService;
    private final IndustryArticleService industryArticleService;
    private final EconomyArticleService economyArticleService;

    public List<CompanyArticle> findUsableLatestCompanyArticles() {
        return companyArticleService.findLatestArticles().stream()
                .filter(article -> articleMainService.findArticleByName(article.getName()).isPresent()).toList();
    }

    public List<IndustryArticle> findUsableLatestIndustryArticles() {
        return industryArticleService.findLatestArticles().stream()
                .filter(article -> articleMainService.findArticleByName(article.getName()).isPresent()).toList();
    }

    public List<EconomyArticle> findUsableLatestDomesticEconomyArticles() {
        return economyArticleService.findLatestDomesticArticles().stream()
                .filter(article -> articleMainService.findArticleByName(article.getName()).isPresent()).toList();
    }

    public List<EconomyArticle> findUsableLatestForeignEconomyArticles() {
        return economyArticleService.findLatestForeignArticles().stream()
                .filter(article -> articleMainService.findArticleByName(article.getName()).isPresent()).toList();
    }
}
