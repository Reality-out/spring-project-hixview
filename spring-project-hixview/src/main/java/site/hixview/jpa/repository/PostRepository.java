package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.method.BasicPostRepositoryFunction;

@Repository
public interface PostRepository extends BasicPostRepositoryFunction<PostEntity>, JpaRepository<PostEntity, Long> {
}
