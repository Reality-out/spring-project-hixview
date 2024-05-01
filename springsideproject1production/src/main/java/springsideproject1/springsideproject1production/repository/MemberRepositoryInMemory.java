package springsideproject1.springsideproject1production.repository;

import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1production.domain.DatabaseHashMap;
import springsideproject1.springsideproject1production.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1production.domain.DatabaseHashMap.*;

@Repository
public class MemberRepositoryInMemory implements MemberRepository {

    /**
     * SELECT Data
     */
    @Override
    public List<Member> findAllMembers() {
        return new ArrayList<>(getMemberHashMap().values());
    }

    @Override
    public Optional<Member> findMemberByIdentifier(Long identifier) {
        return Optional.ofNullable(getMemberHashMap().values().stream()
                .filter(member -> member.getIdentifier().equals(identifier))
                .findAny().get());
    }

    @Override
    public Optional<Member> findMemberByID(String id) {
        return getMemberHashMap().values().stream()
                .filter(member -> member.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Member> findMemberByName(String name) {
        return getMemberHashMap().values().stream()
                .filter(member -> member.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * INSERT Data
     */
    @Override
    public void saveMember(Member member) {
        increaseSequence();
        member.setIdentifier(DatabaseHashMap.getSequence().get());
        getMemberHashMap().put(new AtomicLong(member.getIdentifier()), member);
    }

    /**
     * REMOVE Data
     */
    @Override
    public void removeMemberByID(String id) {
        getMemberHashMap().remove(getMemberHashMap().keySet().stream()
                .filter(member -> getMemberHashMap().get(member).getId().equals(id))
                .findAny().get());
    }
}
