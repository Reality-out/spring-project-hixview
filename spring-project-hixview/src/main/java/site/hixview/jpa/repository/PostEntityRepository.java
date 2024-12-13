package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.PostEntity;

import java.util.Optional;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
    /**
     * SELECT Post
     */
    Optional<PostEntity> findByNumber(Long number);

    /**
     * DELETE Post
     */
    void deleteByNumber(Long number);

    /**
     * CHECK Post
     */
    boolean existsByNumber(Long number);
}
