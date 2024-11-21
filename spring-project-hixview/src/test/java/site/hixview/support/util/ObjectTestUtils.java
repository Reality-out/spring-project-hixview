package site.hixview.support.util;

import java.util.Properties;

public interface ObjectTestUtils {
    static Properties getTestSchemaProper() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.setProperty("hibernate.show_sql", String.valueOf(true));
        properties.setProperty("hibernate.format_sql", String.valueOf(true));
        properties.setProperty("schema.article", "test_article");
        properties.setProperty("schema.article.company", "test_company_article");
        properties.setProperty("schema.article.economy", "test_economy_article");
        properties.setProperty("schema.article.industry", "test_industry_article");
        properties.setProperty("schema.category.first", "test_first_category");
        properties.setProperty("schema.category.industry", "test_industry_category");
        properties.setProperty("schema.category.second", "test_second_category");
        properties.setProperty("schema.company", "test_company");
        properties.setProperty("schema.content.economy", "test_economy_content");
        properties.setProperty("schema.post", "test_post");
        properties.setProperty("schema.post.blog", "test_blog_post");
        properties.setProperty("schema.press", "test_press");
        properties.setProperty("schema.mapper.blog.post.article", "test_blog_post_arti_mapper");
        properties.setProperty("schema.mapper.company.article.company", "test_comp_arti_comp_mapper");
        properties.setProperty("schema.mapper.economy.article.content", "test_econ_arti_cont_mapper");
        properties.setProperty("schema.mapper.industry.article.second.category", "test_indu_arti_sec_cate_mapper");
        properties.setProperty("schema.member.site", "test_site_member");
        return properties;
    }
}
