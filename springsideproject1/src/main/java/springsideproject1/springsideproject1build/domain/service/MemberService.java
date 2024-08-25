package springsideproject1.springsideproject1build.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_MEMBER_ID;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_MEMBER_WITH_THAT_ID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public List<Member> findMembersByNameAndBirth(String name, LocalDate birth) {
        return memberRepository.getMembersByNameAndBirth(name, birth);
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
    public Member registerMember(Member member) {
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
    private void duplicateCheck(Member member) {
        memberRepository.getMemberByID(member.getId()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_MEMBER_ID);}
        );
    }

    private void existentCheck(String Id) {
        memberRepository.getMemberByID(Id).orElseThrow(
                () -> new IllegalStateException(NO_MEMBER_WITH_THAT_ID)
        );
    }
}