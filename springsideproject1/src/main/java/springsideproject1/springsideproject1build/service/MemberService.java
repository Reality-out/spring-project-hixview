package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_MEMBER_ID;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_MEMBER_WITH_THAT_ID;
import static springsideproject1.springsideproject1build.utility.MainUtility.encodeUTF8;

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

    public List<Member> findMembersByName(String name) {
        return memberRepository.getMembersByName(name);
    }

    public List<Member> findMembersByBirth(LocalDate birth) {
        return memberRepository.getMembersByBirth(birth);
    }

    public List<String> findMembersByNameAndBirth(String name, LocalDate birth) {
        ArrayList<String> returnMembers = new ArrayList<>();
        for (Member member : findMembersByName(name)) {
            if (member.getBirth().isEqual(birth)) {
                returnMembers.add(member.getId());
            }
        }
        return encodeUTF8(returnMembers);
    }

    public Optional<Member> findMemberByIdentificationNumber(Long identificationNumber) {
        return memberRepository.getMemberByIdentifier(identificationNumber);
    }

    public Optional<Member> findMemberByID(String Id) {
        return memberRepository.getMemberByID(Id);
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
    public void removeMember(String Id) {
        existentCheck(Id);
        memberRepository.deleteMember(Id);
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
    private void existentCheck(String Id) {
        memberRepository.getMemberByID(Id).orElseThrow(
                () -> new IllegalStateException(NO_MEMBER_WITH_THAT_ID)
        );
    }
}