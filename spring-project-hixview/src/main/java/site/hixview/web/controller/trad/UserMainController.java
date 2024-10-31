package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static site.hixview.domain.vo.ExceptionMessage.*;
import static site.hixview.domain.vo.RequestPath.ROOT_PATH;
import static site.hixview.domain.vo.RequestPath.STATIC_RESOURCE_PATH;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.user.RequestPath.*;
import static site.hixview.domain.vo.user.ViewName.LOGIN_VIEW;
import static site.hixview.domain.vo.user.ViewName.USER_HOME_VIEW;
import static site.hixview.util.FilterUtils.IMAGE_PATH_SUFFIX;

@Controller
@RequiredArgsConstructor
public class UserMainController {
    private static final Logger log = LoggerFactory.getLogger(UserMainController.class);
    private final HomeService homeService;
    private final MemberService memberService;
    private final CompanyArticleService companyArticleService;
    private final IndustryArticleService industryArticleService;
    private final EconomyArticleService economyArticleService;
    private final ArticleMainService articleMainService;
    private final BlogPostService blogPostService;

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String processUserMainPage(Model model) {
        addLatestArticlesInModel(model);
        addLatestBlogPostsInModel(model);
        return USER_HOME_VIEW;
    }

    private void addLatestArticlesInModel(Model model) {
        List<CompanyArticle> latestCompanyArticleList = homeService.findUsableLatestCompanyArticles();
        List<IndustryArticle> latestIndustryArticleList = homeService.findUsableLatestIndustryArticles();
        List<EconomyArticle> latestDomesticEconomyArticleList = homeService.findUsableLatestDomesticEconomyArticles();
        List<EconomyArticle> latestForeignEconomyArticleOrEmpty = homeService.findUsableLatestForeignEconomyArticles();
        errorHandle(latestCompanyArticleList, latestIndustryArticleList, latestDomesticEconomyArticleList, latestForeignEconomyArticleOrEmpty);
        CompanyArticle latestCompanyArticle = latestCompanyArticleList.getFirst();
        IndustryArticle latestIndustryArticle = latestIndustryArticleList.getFirst();
        EconomyArticle latestDomesticEconomyArticle = latestDomesticEconomyArticleList.getFirst();
        EconomyArticle latestForeignEconomyArticle = latestForeignEconomyArticleOrEmpty.getFirst();
        model.addAttribute("latestCompanyArticle", latestCompanyArticle);
        model.addAttribute("latestIndustryArticle", latestIndustryArticle);
        model.addAttribute("latestDomesticEconomyArticle", latestDomesticEconomyArticle);
        model.addAttribute("latestForeignEconomyArticle", latestForeignEconomyArticle);
        model.addAttribute("latestCompanyArticleMain",
                articleMainService.findArticleByName(latestCompanyArticle.getName()).orElseThrow());
        model.addAttribute("latestIndustryArticleMain",
                articleMainService.findArticleByName(latestIndustryArticle.getName()).orElseThrow());
        model.addAttribute("latestDomesticEconomyArticleMain",
                articleMainService.findArticleByName(latestDomesticEconomyArticle.getName()).orElseThrow());
        model.addAttribute("latestForeignEconomyArticleMain",
                articleMainService.findArticleByName(latestForeignEconomyArticle.getName()).orElseThrow());
    }

    private void errorHandle(List<CompanyArticle> latestCompanyArticleList, List<IndustryArticle> latestIndustryArticleList, List<EconomyArticle> latestDomesticEconomyArticleList, List<EconomyArticle> latestForeignEconomyArticleOrEmpty) {
        if (latestCompanyArticleList.isEmpty()) {
            throw new NotFoundException(NO_COMPANY_ARTICLE_WITH_THAT_CONDITION);
        }
        if (latestIndustryArticleList.isEmpty()) {
            throw new NotFoundException(NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION);
        }
        if (latestDomesticEconomyArticleList.isEmpty()) {
            throw new NotFoundException(NO_DOMESTIC_ECONOMY_ARTICLE_WITH_THAT_CONDITION);
        }
        if (latestForeignEconomyArticleOrEmpty.isEmpty()) {
            throw new NotFoundException(NO_FOREIGN_ECONOMY_ARTICLE_WITH_THAT_CONDITION);
        }
    }

    private void addLatestBlogPostsInModel(Model model) {
        Random random = new Random();
        List<BlogPost> latestCompanyBlogPostList = blogPostService.findLatestPosts(Classification.COMPANY);
        List<BlogPost> latestIndustryBlogPostList = blogPostService.findLatestPosts(Classification.INDUSTRY);
        List<BlogPost> latestEconomyBlogPostList = blogPostService.findLatestPosts(Classification.ECONOMY);
        errorHandle(latestCompanyBlogPostList, latestIndustryBlogPostList, latestEconomyBlogPostList);
        BlogPost latestIndustryBlogPost = latestIndustryBlogPostList.get(random.nextInt(latestIndustryBlogPostList.size()));
        BlogPost latestEconomyBlogPost = latestEconomyBlogPostList.get(random.nextInt(latestEconomyBlogPostList.size()));
        latestIndustryBlogPost = BlogPost.builder().blogPost(latestIndustryBlogPost).targetImagePath(getTargetImagePath(latestIndustryBlogPost, random)).build();
        latestEconomyBlogPost = BlogPost.builder().blogPost(latestEconomyBlogPost).targetImagePath(getTargetImagePath(latestEconomyBlogPost, random)).build();
        model.addAttribute("latestCompanyBlogPost", latestCompanyBlogPostList.get(random.nextInt(latestCompanyBlogPostList.size())));
        model.addAttribute("latestIndustryBlogPost", latestIndustryBlogPost);
        model.addAttribute("latestEconomyBlogPost", latestEconomyBlogPost);
    }

    private void errorHandle(List<BlogPost> latestCompanyBlogPostList, List<BlogPost> latestIndustryBlogPostList, List<BlogPost> latestEconomyBlogPostList) {
        if (latestCompanyBlogPostList.isEmpty()) {
            throw new NotFoundException(NO_COMPANY_BLOG_POST_WITH_THAT_CONDITION);
        }
        if (latestIndustryBlogPostList.isEmpty()) {
            throw new NotFoundException(NO_INDUSTRY_BLOG_POST_WITH_THAT_CONDITION);
        }
        if (latestEconomyBlogPostList.isEmpty()) {
            throw new NotFoundException(NO_ECONOMY_BLOG_POST_WITH_THAT_CONDITION);
        }
    }

    private String getTargetImagePath(BlogPost blogPost, Random random) {
        String targetImagePath = blogPost.getTargetImagePath();
        String pathPrefix = StringUtils.substringBeforeLast(targetImagePath, "/");
        String pathSuffix = StringUtils.substringAfterLast(targetImagePath, "/");
        try (Stream<Path> fileList = Files.list(Paths.get(ROOT_PATH).resolve(STATIC_RESOURCE_PATH).resolve(pathPrefix.substring(1)))) {
            Integer maxNumber = fileList.map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> name.substring(0, name.length() - 4))
                    .filter(name -> name.startsWith(pathSuffix))
                    .map(name -> name.substring(name.length() - 3))
                    .map(Integer::parseInt)
                    .max(Integer::compareTo)
                    .orElseThrow();
            int chosenNumber = random.nextInt(maxNumber) + 1;
            if (chosenNumber < 10) {
                return blogPost.getTargetImagePath() + "_00" + chosenNumber + IMAGE_PATH_SUFFIX;
            } else if (chosenNumber < 100) {
                return blogPost.getTargetImagePath() + "_0" + chosenNumber + IMAGE_PATH_SUFFIX;
            } else {
                return blogPost.getTargetImagePath() + "_" + chosenNumber + IMAGE_PATH_SUFFIX;
            }
        } catch(IOException e) {
            throw new InvalidPathException(e.toString(), NO_TARGET_IMAGE_DIRECTORY);
        }
    }

    /**
     * Login
     */
    @GetMapping(LOGIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processLoginPage(Model model) {
        model.addAttribute("membership", MEMBERSHIP_PATH);
        model.addAttribute("findId", FIND_ID_PATH);
        return LOGIN_VIEW + VIEW_SHOW;
    }
}