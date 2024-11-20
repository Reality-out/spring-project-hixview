package site.hixview.support.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;

public interface ObjectTestUtils {
    // Test Entity Manager Factory
    EntityManagerFactory testEMF = Persistence.createEntityManagerFactory("testEntityManager", new HashMap<String, String>(){{
        put("schema.article", "test_article");
        put("schema.article.company", "test_company_article");
        put("schema.article.economy", "test_economy_article");
        put("schema.article.industry", "test_industry_article");
        put("schema.category.first", "test_first_category");
        put("schema.category.industry", "test_industry_category");
        put("schema.category.second", "test_second_category");
        put("schema.company", "test_company");
        put("schema.content.economy", "test_economy_content");
        put("schema.post", "test_post");
        put("schema.post.blog", "test_blog_post");
        put("schema.press", "test_press");
        put("schema.mapper.blog.post.article", "test_blog_post_arti_mapper");
        put("schema.mapper.company.article.company", "test_comp_arti_comp_mapper");
        put("schema.mapper.economy.article.content", "test_econ_arti_cont_mapper");
        put("schema.mapper.industry.article.second.category", "test_indu_arti_sec_cate_mapper");
        put("schema.member.site", "test_site_member");
    }});
}
