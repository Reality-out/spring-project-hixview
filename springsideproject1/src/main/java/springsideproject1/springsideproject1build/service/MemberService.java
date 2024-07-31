package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.Utility.duplicateCheck;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_MEMBER_WITH_THAT_ID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * SELECT Members
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
    public Member joinMember(Member member) {
        duplicateCheck(memberRepository, member);
        Long memberIdentifier = memberRepository.saveMember(member);
        return new Member.MemberBuilder()
                .member(member)
                .identifier(memberIdentifier)
                .build();
    }

    /**
     * REMOVE One Member
     */
    @Transactional
    public void removeMember(String memberID) {
        memberRepository.findMemberByID(memberID).orElseThrow(
                () -> new IllegalStateException(NO_MEMBER_WITH_THAT_ID)
        );

        memberRepository.removeMemberByID(memberID);
    }
}