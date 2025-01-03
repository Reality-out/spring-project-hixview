package site.hixview.jpa.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.mapper.support.BlogPostEntityMapperSupport;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface BlogPostEntityMapper extends BlogPostEntityMapperSupport {
    @Mapping(target = POST, ignore = true)
    @Mapping(target = BLOG_POST, ignore = true)
    BlogPostEntity toBlogPostEntity(BlogPost blogPost,
                                    @Context PostEntityRepository postEntityRepository);

    @Mapping(source = POST, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(target = MAPPED_ARTICLE_NUMBERS, ignore = true)
    @Mapping(target = BLOG_POST, ignore = true)
    BlogPost toBlogPost(BlogPostEntity blogPostEntity,
                        @Context BlogPostArticleEntityRepository blogPostArticleRepository);
}
