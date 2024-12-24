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

    public static BlogPostEntity map(BlogPost from, BlogPostEntity to,
                                     PostEntityRepository postEntityRepository) {
        to.updateName(from.getName());
        to.updateLink(from.getLink());
        to.updateDate(from.getDate());
        to.updatePost(postEntityRepository.findByNumber(from.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getNumber(), BlogPostEntity.class)));
        to.updateClassification(from.getClassification().name());
        return to;
    }

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

    public static CompanyEntity map(Company from, CompanyEntity to,
                                    FirstCategoryEntityRepository fcEntityRepository,
                                    SecondCategoryEntityRepository scEntityRepository) {
        to.updateKoreanName(from.getKoreanName());
        to.updateEnglishName(from.getEnglishName());
        to.updateNameListed(from.getNameListed());
        to.updateCountryListed(from.getCountryListed().name());
        to.updateScale(from.getScale().name());
        to.updateFirstCategory(fcEntityRepository.findByNumber(from.getFirstCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getFirstCategoryNumber(), FirstCategoryEntity.class)));
        to.updateSecondCategory(scEntityRepository.findByNumber(from.getSecondCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getSecondCategoryNumber(), SecondCategoryEntity.class)));
        return to;
    }

    public static CompanyArticleEntity map(CompanyArticle from, CompanyArticleEntity to,
                                           ArticleEntityRepository articleEntityRepository,
                                           PressEntityRepository pressEntityRepository) {
        to.updateName(from.getName());
        to.updateLink(from.getLink());
        to.updateDate(from.getDate());
        to.updateSubjectCountry(from.getSubjectCountry().name());
        to.updateImportance(from.getImportance().name());
        to.updateSummary(from.getSummary());
        to.updatePress(pressEntityRepository.findByNumber(from.getPressNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getPressNumber(), PressEntity.class)));
        to.updateArticle(articleEntityRepository.findByNumber(from.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getNumber(), CompanyArticleEntity.class)));
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

    public static EconomyArticleEntity map(EconomyArticle from, EconomyArticleEntity to,
                                           ArticleEntityRepository articleEntityRepository,
                                           PressEntityRepository pressEntityRepository) {
        to.updateName(from.getName());
        to.updateLink(from.getLink());
        to.updateDate(from.getDate());
        to.updateSubjectCountry(from.getSubjectCountry().name());
        to.updateImportance(from.getImportance().name());
        to.updateSummary(from.getSummary());
        to.updatePress(pressEntityRepository.findByNumber(from.getPressNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getPressNumber(), PressEntity.class)));
        to.updateArticle(articleEntityRepository.findByNumber(from.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getNumber(), EconomyArticleEntity.class)));
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

    public static IndustryArticleEntity map(IndustryArticle from, IndustryArticleEntity to,
                                            ArticleEntityRepository articleEntityRepository,
                                            PressEntityRepository pressEntityRepository,
                                            FirstCategoryEntityRepository fcEntityRepository) {
        to.updateName(from.getName());
        to.updateLink(from.getLink());
        to.updateDate(from.getDate());
        to.updateSubjectCountry(from.getSubjectCountry().name());
        to.updateImportance(from.getImportance().name());
        to.updateSummary(from.getSummary());
        to.updatePress(pressEntityRepository.findByNumber(from.getPressNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getPressNumber(), PressEntity.class)));
        to.updateArticle(articleEntityRepository.findByNumber(from.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getNumber(), EconomyArticleEntity.class)));
        to.updateFirstCategory(fcEntityRepository.findByNumber(from.getFirstCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(from.getFirstCategoryNumber(), FirstCategoryEntity.class)));
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