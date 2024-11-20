package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteMemberRepository extends JpaRepository<SiteMemberEntity, Long> {
    /**
     * SELECT SiteMember
     */
    List<SiteMember> findByName(String name);

    Optional<SiteMember> findByNumber(Long number);

    Optional<SiteMember> findById(String id);

    Optional<SiteMember> findByIdAndPw(String id, String pw);

    Optional<SiteMember> findByEmail(String email);

    /**
     * REMOVE SiteMember
     */
    @Transactional
    void deleteByNumber(Long number);
}
