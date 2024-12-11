package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPostArticle {
    private final Long number;
    private final Long postNumber;
    private final Long articleNumber;
}
