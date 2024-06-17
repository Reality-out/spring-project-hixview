package springsideproject1.springsideproject1build.domain;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DatabaseHashMap {
    @Getter
    private static Map<AtomicLong, Member> memberHashMap = new ConcurrentHashMap<>();

    @Getter
    private static AtomicLong sequence = new AtomicLong();

    public static void increaseSequence() {
        sequence.set(sequence.get() + 1);
    }

    public static void clearSettings() {
        memberHashMap.clear();
        sequence.set(0L);
    }

}