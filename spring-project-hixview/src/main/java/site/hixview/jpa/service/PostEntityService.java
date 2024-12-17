package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Post;
import site.hixview.aggregate.service.PostService;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.mapper.PostEntityMapper;
import site.hixview.jpa.mapper.PostEntityMapperImpl;
import site.hixview.jpa.repository.PostEntityRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostEntityService implements PostService {

    private final PostEntityRepository postEntityRepository;
    private final PostEntityMapper mapper = new PostEntityMapperImpl();

    @Override
    public List<Post> getAll() {
        return postEntityRepository.findAll().stream().map(mapper::toPost).toList();
    }

    @Override
    public Optional<Post> getByNumber(Long number) {
        Optional<PostEntity> optionalPostEntity = postEntityRepository.findByNumber(number);
        if (optionalPostEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPost(optionalPostEntity.orElseThrow()));
    }
}
