package site.hixview.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_MEMBER_ID;
import static site.hixview.domain.vo.ExceptionMessage.NO_MEMBER_WITH_THAT_ID;

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

    public List<Member> findMembersByBirthday(LocalDate birthday) {
        return memberRepository.getMembersByBirthday(birthday);
    }

    public List<Member> findMembersByNameAndBirthday(String name, LocalDate birthday) {
        return memberRepository.getMembersByNameAndBirthday(name, birthday);
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
    public void removeMemberById(String Id) {
        existentCheck(Id);
        memberRepository.deleteMemberById(Id);
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