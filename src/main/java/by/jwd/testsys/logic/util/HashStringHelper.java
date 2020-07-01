package by.jwd.testsys.logic.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashStringHelper {

    private static final String HASHED_STRING_START = "$2a$";
    private static int workload = 12;

    private HashStringHelper() {
    }


    public static String hashPassword(String password_plaintext) {

        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(password_plaintext, salt);
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {

        if (null == stored_hash || !stored_hash.startsWith(HASHED_STRING_START)) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }

        return BCrypt.checkpw(password_plaintext, stored_hash);
    }

}
