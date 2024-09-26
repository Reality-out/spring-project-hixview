package site.hixview.support.property;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestPropertySources({
        @TestPropertySource(properties = {
                "schema.article.companies=test_company_articles",
                "schema.article.industries=test_industry_articles",
                "schema.article.mains=test_article_mains",
                "schema.companies=test_companies",
                "schema.members=test_members"
        })
})
public @interface TestSchemaName {
}