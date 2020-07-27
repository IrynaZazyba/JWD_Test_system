package by.jwd.testsys.dao.dbconn;

import java.util.ResourceBundle;

public class DBResourceManagerTest {
    private final static DBResourceManagerTest instance = new DBResourceManagerTest();

    private ResourceBundle bundle =
            ResourceBundle.getBundle("testdb");

    public static DBResourceManagerTest getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return bundle.getString(key);
    }
}
