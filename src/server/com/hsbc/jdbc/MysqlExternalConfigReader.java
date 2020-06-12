package server.com.hsbc.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MysqlExternalConfigReader {
    public static MysqlConnectionProperties readFromFile(String mysqlConnectionPropertiesFilePath) throws IOException {
        File file = new File(mysqlConnectionPropertiesFilePath);
        if (!file.exists()) {
            return null;
        }

        return readMysqlConnectionProperties(file);
    }

    private static MysqlConnectionProperties readMysqlConnectionProperties(File file) throws IOException {
        MysqlConnectionProperties mysqlConnectionProperties = new MysqlConnectionProperties();
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        mysqlConnectionProperties.user = properties.getProperty("user");
        mysqlConnectionProperties.password = properties.getProperty("password");
        mysqlConnectionProperties.host = properties.getProperty("host");
        mysqlConnectionProperties.database = properties.getProperty("database");
        mysqlConnectionProperties.port = properties.getProperty("port") != null
                ? Integer.parseInt(properties.getProperty("port"))
                : null;
        mysqlConnectionProperties.requireSSL = properties.getProperty("requireSSL") != null
                ? Boolean.parseBoolean(properties.getProperty("requireSSL"))
                : null;
        mysqlConnectionProperties.verifyServerCertificate = properties.getProperty("verifyServerCertificate") != null
                ? Boolean.parseBoolean(properties.getProperty("verifyServerCertificate", "false"))
                : null;
        mysqlConnectionProperties.keystoreAbsoluteFilePath = properties.getProperty("keystoreAbsoluteFilePath");
        mysqlConnectionProperties.keystorePassword = properties.getProperty("keystorePassword");

        return mysqlConnectionProperties;
    }
}
