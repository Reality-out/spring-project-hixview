package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.service.supers.ServiceWithNumberIdentifier;

import java.util.List;

public interface BlogPostArticleService extends ServiceWithNumberIdentifier<BlogPostArticle> {
    List<BlogPostArticle> getByBlogPost(BlogPost blogPost);

    List<BlogPostArticle> getByArticle(Article article);
}
