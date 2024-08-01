package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_MEMBER_ID;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_MEMBER_WITH_THAT_ID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * SELECT Member
     */
    public List<Member> findMembers() {
        return memberRepository.getMembers();
    }

    public List<Member> findMembersByName(String memberName) {
        return memberRepository.getMembersByName(memberName);
    }

    public Optional<Member> findMemberByIdentificationNumber(Long memberIdentificationNumber) {
        return memberRepository.getMemberByIdentifier(memberIdentificationNumber);
    }

    public Optional<Member> findMemberByID(String memberID) {
        return memberRepository.getMemberByID(memberID);
    }

    /**
     * INSERT Member
     */
    @Transactional
    public Member joinMember(Member member) {
        duplicateCheck(member);
        return Member.builder().member(member).identifier(memberRepository.saveMember(member)).build();
    }

    /**
     * REMOVE Member
     */
    @Transactional
    public void removeMember(String memberID) {
        existentCheck(memberID);
        memberRepository.deleteMember(memberID);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCheck(Member member) {
        memberRepository.getMemberByID(member.getId()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_MEMBER_ID);}
        );
    }

    @Transactional
    private void existentCheck(String memberID) {
        memberRepository.getMemberByID(memberID).orElseThrow(
                () -> new IllegalStateException(NO_MEMBER_WITH_THAT_ID)
        );
    }
}