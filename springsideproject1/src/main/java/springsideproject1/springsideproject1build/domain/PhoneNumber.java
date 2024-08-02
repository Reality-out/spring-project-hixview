package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumber implements Comparable<PhoneNumber> {
    private final String countryCode;
    private final String areaCode;
    private final String subscriberNumber;

    @Override
    public int compareTo(PhoneNumber number) {
        int countryCompare = countryCode.compareTo(number.getCountryCode());
        if (countryCompare != 0) return countryCompare;

        int areaCompare = areaCode.compareTo(number.getAreaCode());
        if (areaCompare != 0) return areaCompare;

        return subscriberNumber.compareTo(number.getSubscriberNumber());
    }

    @Override
    public String toString() {
        return countryCode + "-" + areaCode + "-" + subscriberNumber;
    }

    public static class PhoneNumberBuilder {
        public PhoneNumberBuilder() {}

        public PhoneNumberBuilder phoneNumber(PhoneNumber phoneNumber) {
            countryCode = phoneNumber.getCountryCode();
            areaCode = phoneNumber.getAreaCode();
            subscriberNumber = phoneNumber.getSubscriberNumber();
            return this;
        }
        
        public PhoneNumberBuilder phoneNumber(String phoneNumber) {
            String[] splitPhoneNumber = phoneNumber.split("-");
            countryCode = splitPhoneNumber[0];
            areaCode = splitPhoneNumber[1];
            subscriberNumber = splitPhoneNumber[2];
            return this;
        }
    }
}