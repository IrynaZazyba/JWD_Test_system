package by.jwd.testsys.logic.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashStringHelper {

    private static final String HASHED_STRING_START = "$2a$";
    private static final int workload = 12;

    private HashStringHelper() {
    }


    public static String hashString(String text) {

        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(text, salt);
    }

    public static boolean checkString(String text, String stored_hash) {

        if (null == stored_hash || !stored_hash.startsWith(HASHED_STRING_START)) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }

        return BCrypt.checkpw(text, stored_hash);
    }

}
