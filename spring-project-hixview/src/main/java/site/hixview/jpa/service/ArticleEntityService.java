package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.service.ArticleService;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.mapper.ArticleEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleEntityService implements ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final ArticleEntityMapperImpl mapper = new ArticleEntityMapperImpl();

    @Override
    public List<Article> getAll() {
        return articleEntityRepository.findAll().stream().map(mapper::toArticle).toList();
    }

    @Override
    public Optional<Article> getByNumber(Long number) {
        Optional<ArticleEntity> optionalArticleEntity = articleEntityRepository.findByNumber(number);
        if (optionalArticleEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toArticle(optionalArticleEntity.orElseThrow()));
    }
}
