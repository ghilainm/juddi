package org.apache.juddi.utils;

import javax.persistence.EntityManager;
import java.sql.Connection;

public class EntityManagerUtils {

    public static boolean isOracleDatabase(EntityManager entityManager) {
        try {
            String driver = entityManager.unwrap(Connection.class).getMetaData().getDriverName();
            if (driver != null && driver.toLowerCase().contains("oracle")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
