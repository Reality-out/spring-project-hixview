package springsideproject1.springsideproject1build.repository;

import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.database.MemberDatabase;
import springsideproject1.springsideproject1build.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.database.MemberDatabase.*;

@Repository
public class MemberRepositoryInMemory implements MemberRepository {

    /**
     * SELECT Data
     */
    @Override
    public List<Member> findAllMembers() {
        return new ArrayList<>(getCompanyHashMap().values());
    }

    @Override
    public Optional<Member> findMemberByIdentifier(Long identifier) {
        return Optional.ofNullable(getCompanyHashMap().values().stream()
                .filter(member -> member.getIdentifier().equals(identifier))
                .findAny().get());
    }

    @Override
    public Optional<Member> findMemberByID(String id) {
        return getCompanyHashMap().values().stream()
                .filter(member -> member.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Member> findMemberByName(String name) {
        return getCompanyHashMap().values().stream()
                .filter(member -> member.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * INSERT Data
     */
    @Override
    public void saveMember(Member member) {
        increaseSequence();
        member.setIdentifier(MemberDatabase.getSequence().get());
        getCompanyHashMap().put(new AtomicLong(member.getIdentifier()), member);
    }

    /**
     * REMOVE Data
     */
    @Override
    public void removeMemberByID(String id) {
        getCompanyHashMap().remove(getCompanyHashMap().keySet().stream()
                .filter(memberIdentifier -> getCompanyHashMap().get(memberIdentifier).getId().equals(id))
                .findAny().get());
    }
}
