package site.hixview.jpa.utils;

import jakarta.persistence.EntityNotFoundException;
import site.hixview.aggregate.domain.*;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.CODE;

public abstract class MapperUtils {

    public static BlogPostArticleEntity map(BlogPostArticle from, BlogPostArticleEntity to,
                                            BlogPostEntityRepository bpRepository,
                                            ArticleEntityRepository aRepository) {
        Long postNumber = from.getPostNumber();
        Long articleNumber = from.getArticleNumber();
        to.updateBlogPost(bpRepository.findByNumber(postNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(postNumber, BlogPostEntity.class)));
        to.updateArticle(aRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, ArticleEntity.class)));
        return to;
    }

    public static CompanyArticleCompanyEntity map(CompanyArticleCompany from, CompanyArticleCompanyEntity to,
                                                  CompanyArticleEntityRepository caRepository,
                                                  CompanyEntityRepository cRepository) {
        Long articleNumber = from.getArticleNumber();
        String companyCode = from.getCompanyCode();
        to.updateCompanyArticle(caRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, CompanyArticleEntity.class)));
        to.updateCompany(cRepository.findByCode(companyCode).orElseThrow(() ->
                new EntityNotFoundException(getFormattedExceptionMessage(
                        CANNOT_FOUND_ENTITY, CODE, companyCode, CompanyEntity.class))));
        return to;
    }

    public static EconomyArticleContentEntity map(EconomyArticleContent from,
                                                  EconomyArticleContentEntity to,
                                                  EconomyArticleEntityRepository eaRepository,
                                                  EconomyContentEntityRepository ecRepository) {
        Long articleNumber = from.getArticleNumber();
        Long contentNumber = from.getContentNumber();
        to.updateEconomyArticle(eaRepository.findByNumber(articleNumber)
                .orElseThrow(() -> new EntityNotFoundWithNumberException(
                        articleNumber, EconomyArticleEntity.class)));
        to.updateEconomyContent(ecRepository.findByNumber(contentNumber)
                .orElseThrow(() -> new EntityNotFoundWithNumberException(
                        contentNumber, EconomyContentEntity.class)));
        return to;
    }

    public static EconomyContentEntity map(EconomyContent from,
                                           EconomyContentEntity to) {
        to.updateName(from.getName());
        return to;
    }

    public static FirstCategoryEntity map(FirstCategory from, FirstCategoryEntity to,
                                          IndustryCategoryEntityRepository icRepository) {
        to.updateKoreanName(from.getKoreanName());
        to.updateEnglishName(from.getEnglishName());
        to.updateIndustryCategory(icRepository.findByNumber(
                from.getIndustryCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getIndustryCategoryNumber(), IndustryCategoryEntity.class)));
        return to;
    }

    public static IndustryArticleSecondCategoryEntity map(IndustryArticleSecondCategory from,
                                                          IndustryArticleSecondCategoryEntity to,
                                                          IndustryArticleEntityRepository iaRepository,
                                                          SecondCategoryEntityRepository scRepository) {
        Long articleNumber = from.getArticleNumber();
        Long secondCategoryNumber = from.getSecondCategoryNumber();
        to.updateIndustryArticle(iaRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, IndustryArticleEntity.class)));
        to.updateSecondCategory(scRepository.findByNumber(secondCategoryNumber).orElseThrow(
                () -> new EntityNotFoundWithNumberException(secondCategoryNumber, SecondCategoryEntity.class)));
        return to;
    }

    public static IndustryCategoryEntity map(IndustryCategory from, IndustryCategoryEntity to) {
        to.updateKoreanName(from.getKoreanName());
        to.updateEnglishName(from.getEnglishName());
        return to;
    }

    public static PressEntity map(Press from, PressEntity to) {
        to.updateKoreanName(from.getKoreanName());
        to.updateEnglishName(from.getEnglishName());
        return to;
    }

    public static SecondCategoryEntity map(SecondCategory from, SecondCategoryEntity to,
                                           IndustryCategoryEntityRepository icRepository,
                                           FirstCategoryEntityRepository fcRepository) {
        to.updateKoreanName(from.getKoreanName());
        to.updateEnglishName(from.getEnglishName());
        to.updateIndustryCategory(icRepository.findByNumber(
                from.getIndustryCategoryNumber()).orElseThrow(() -> new EntityNotFoundWithNumberException(
                from.getIndustryCategoryNumber(), IndustryCategoryEntity.class)));
        to.updateFirstCategory(fcRepository.findByNumber(
                from.getFirstCategoryNumber()).orElseThrow(() -> new EntityNotFoundWithNumberException(
                from.getFirstCategoryNumber(), FirstCategoryEntity.class)));
        return to;
    }

    public static SiteMemberEntity map(SiteMember from, SiteMemberEntity to) {
        to.updateId(from.getId());
        to.updatePw(from.getPw());
        to.updateName(from.getName());
        to.updateEmail(from.getEmail());
        return to;
    }
}