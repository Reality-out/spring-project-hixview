package springsideproject1.springsideproject1production.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1production.domain.Member;
import springsideproject1.springsideproject1production.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * SELECT All Members
     */
    public List<Member> findMembers() {
        return memberRepository.findAllMembers();
    }

    /**
     * SELECT One Member
     */
    public Optional<Member> findOneMemberByIdentificationNumber(Long memberIdentificationNumber) {
        return memberRepository.findMemberByIdentifier(memberIdentificationNumber);
    }

    public Optional<Member> findOneMemberByID(String memberID) {
        return memberRepository.findMemberByID(memberID);
    }

    public List<Member> findOneMemberByName(String memberName) {
        return memberRepository.findMemberByName(memberName);
    }

    /**
     * INSERT One Member
     */
    @Transactional
    public void joinMember(Member member) {
        DuplicateIDCheck(member);
        memberRepository.saveMember(member);
    }

    /**
     * REMOVE One Member
     */
    @Transactional
    public void removeMember(String memberID) {
        memberRepository.findMemberByID(memberID).orElseThrow(
                () -> new IllegalStateException("해당 ID와 일치하는 멤버가 없습니다."));

        memberRepository.removeMemberByID(memberID);
    }

    private void DuplicateIDCheck(Member member) {
        memberRepository.findMemberByID(member.getId()).ifPresent(
                v -> {throw new IllegalStateException("이미 존재하는 ID입니다.");}
        );
    }
}