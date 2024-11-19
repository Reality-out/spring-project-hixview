package site.hixview.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogPostDto {
    private Long number;
    private String name;
    private String link;
    private String date;
    private String classification;
    private String relatedArticleNumbers;
}
