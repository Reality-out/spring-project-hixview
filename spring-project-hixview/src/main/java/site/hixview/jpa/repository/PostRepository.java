package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.method.BasicPostRepositoryFunction;

public interface PostRepository extends BasicPostRepositoryFunction<PostEntity>, JpaRepository<PostEntity, Long> {
}
