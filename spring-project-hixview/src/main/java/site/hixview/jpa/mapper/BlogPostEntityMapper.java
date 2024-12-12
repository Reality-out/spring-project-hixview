package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.mapper.support.BlogPostEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class BlogPostEntityMapper extends BlogPostEntityMapperSupport {
    @Mapping(target = POST, ignore = true)
    @Mapping(target = BLOG_POST, ignore = true)
    public abstract BlogPostEntity toBlogPostEntity(BlogPost blogPost);

    @Mapping(target = MAPPED_ARTICLE_NUMBERS, ignore = true)
    public abstract BlogPost toBlogPost(BlogPostEntity blogPostEntity);
}
