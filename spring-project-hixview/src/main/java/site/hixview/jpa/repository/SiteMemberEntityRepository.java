package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.SiteMemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteMemberEntityRepository extends JpaRepository<SiteMemberEntity, Long> {
    /**
     * SELECT SiteMember
     */
    List<SiteMemberEntity> findByName(String name);

    Optional<SiteMemberEntity> findByNumber(Long number);

    Optional<SiteMemberEntity> findById(String id);

    Optional<SiteMemberEntity> findByIdAndPw(String id, String pw);

    Optional<SiteMemberEntity> findByEmail(String email);

    /**
     * REMOVE SiteMember
     */
    void deleteByNumber(Long number);

    /**
     * CHECK SiteMember
     */
    boolean existsByNumber(Long number);
}
