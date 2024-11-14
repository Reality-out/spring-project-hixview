package site.hixview.support.property;

import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestPropertySource(properties = {
        "schema.article=test_article",
        "schema.article.company=test_company_article",
        "schema.article.economy=test_economy_article",
        "schema.article.industry=test_industry_article",
        "schema.category.first=test_first_category",
        "schema.category.industry=test_industry_category",
        "schema.category.second=test_second_category",
        "schema.company=test_company",
        "schema.content.economy=test_economy_content",
        "schema.post=test_post",
        "schema.post.blog=test_blog_post",
        "schema.press=test_press",
        "schema.mapper.blog.post.article=test_blog_post_arti_mapper",
        "schema.mapper.company.article.company=test_comp_arti_comp_mapper",
        "schema.mapper.economy.article.content=test_econ_arti_cont_mapper",
        "schema.mapper.industry.article.second.category=test_indu_arti_sec_cate_mapper",
        "schema.member.site=test_site_member"
})
public @interface TestSchemaName {
}
