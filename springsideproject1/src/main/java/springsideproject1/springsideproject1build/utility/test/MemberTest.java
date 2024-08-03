package springsideproject1.springsideproject1build.utility.test;

import springsideproject1.springsideproject1build.domain.Member;

import java.time.LocalDate;

public interface MemberTest extends ObjectTest {

    // DB table name
    String memberTable = "testmembers";

    /**
     * Create
     */
    default Member createTestMember() {
        return Member.builder().id("ABcd1234!").password("EFgh1234!").name("박진하")
                .birth(LocalDate.of(2000, 4, 1)).phoneNumber("010-1234-5678").build();
    }

    default Member createTestNewMember() {
        return Member.builder().id("abCD4321!").password("OPqr4321!").name("박하진")
                .birth(LocalDate.of(1999, 9, 1)).phoneNumber("010-2345-6789").build();
    }
}
