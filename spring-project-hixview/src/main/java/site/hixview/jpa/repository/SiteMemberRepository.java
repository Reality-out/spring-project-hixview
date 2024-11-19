package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;

import java.util.List;
import java.util.Optional;

public interface SiteMemberRepository extends JpaRepository<SiteMemberEntity, Long> {
    /**
     * SELECT SiteMember
     */
    List<SiteMember> findByName(String name);

    Optional<SiteMember> findByNumber(Long number);

    Optional<SiteMember> findByID(String id);

    Optional<SiteMember> findByIDAndPassword(String id, String password);

    Optional<SiteMember> findByEmail(String email);

    /**
     * REMOVE SiteMember
     */
    @Transactional
    void deleteByNumber(Long number);
}
