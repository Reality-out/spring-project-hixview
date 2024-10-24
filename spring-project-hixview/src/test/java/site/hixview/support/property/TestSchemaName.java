package site.hixview.support.property;

import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestPropertySource(properties = {
        "schema.articles.company=test_company_articles",
        "schema.articles.industry=test_industry_articles",
        "schema.articles.economy=test_economy_articles",
        "schema.articles.main=test_article_mains",
        "schema.posts.blog=test_blog_posts",
        "schema.companies=test_companies",
        "schema.members=test_members"
})
public @interface TestSchemaName {
}