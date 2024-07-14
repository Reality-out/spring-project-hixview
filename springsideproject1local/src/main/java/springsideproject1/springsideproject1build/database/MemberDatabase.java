package springsideproject1.springsideproject1build.database;

import springsideproject1.springsideproject1build.domain.Member;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemberDatabase {
    private static Map<AtomicLong, Member> memberHashMap = new ConcurrentHashMap<>();

    private static AtomicLong sequence = new AtomicLong();

    public static void increaseSequence() {
        sequence.set(sequence.get() + 1);
    }

    public static Map<AtomicLong, Member> getCompanyHashMap() {
        return memberHashMap;
    }

    public static AtomicLong getSequence() {
        return sequence;
    }
}