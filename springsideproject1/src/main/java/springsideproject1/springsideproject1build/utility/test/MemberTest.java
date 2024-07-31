package springsideproject1.springsideproject1build.utility.test;

import springsideproject1.springsideproject1build.domain.Member;

public interface MemberTest extends ObjectTest {

    String memberTable = "testmembers";

    default Member createTestMember() {
        return new Member.MemberBuilder()
                .id("ABcd1234!")
                .password("EFgh1234!")
                .name("박진하")
                .build();
    }

    default Member createTestNewMember() {
        return new Member.MemberBuilder()
                .id("abCD4321!")
                .password("OPqr4321!")
                .name("박하진")
                .build();
    }
}
