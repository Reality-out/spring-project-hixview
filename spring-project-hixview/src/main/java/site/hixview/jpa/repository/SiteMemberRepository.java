package site.hixview.jpa.repository;

import site.hixview.aggregate.domain.SiteMember;

import java.util.List;
import java.util.Optional;

public interface SiteMemberRepository {
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
    void deleteByNumber(Long number);
}
