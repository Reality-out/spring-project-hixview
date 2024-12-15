package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

import java.util.List;
import java.util.Optional;

public interface SiteMemberService extends CrudAllowedServiceWithNumberId<SiteMember> {
    List<SiteMember> getByName(String name);

    Optional<SiteMember> getById(String id);

    Optional<SiteMember> getByIdAndPw(String id, String pw);

    Optional<SiteMember> getByEmail(String email);
}
