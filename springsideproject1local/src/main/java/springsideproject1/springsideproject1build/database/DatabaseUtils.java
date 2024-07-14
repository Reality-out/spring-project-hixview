package springsideproject1.springsideproject1build.database;

import static springsideproject1.springsideproject1build.database.CompanyDatabase.getCompanyHashMap;
import static springsideproject1.springsideproject1build.database.MemberDatabase.getSequence;

public class DatabaseUtils {

    public static void clearMemberDatabase() {
        MemberDatabase.getCompanyHashMap().clear();
        getSequence().set(0L);
    }

    public static void clearCompanyDatabase() {
        getCompanyHashMap().clear();
    }
}
