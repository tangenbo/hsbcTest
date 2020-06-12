package server.com.hsbc.jdbc;

import com.google.common.base.Strings;
import server.com.hsbc.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class JDBCPropertiesUtil {
    private final static Log log = new Log(JDBCPropertiesUtil.class);

    private static String urlOptions = "?useCursorFetch=true&defaultFetchSize=50000&logger=com.mysql.cj.log.NullLogger&allowLoadLocalInfile=true";

    public static MysqlConnectionProperties getConnectionProperties() {
        MysqlConnectionProperties properties = new MysqlConnectionProperties();
        properties.overwriteProperties(fetchFromExternalFile());
        try {
            validateProperties(properties);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return properties;
    }

    private static String getConfigFileName() {
        return "properties/mysql.client.properties";
    }

    private static MysqlConnectionProperties fetchFromExternalFile() {
        String configFile = getConfigFileName();
        try {
            return MysqlExternalConfigReader.readFromFile(configFile);
        } catch (IOException e) {
            log.error("Error reading config file");
            return null;
        }
    }

    private static String getKeyStoreFile(MysqlConnectionProperties mysqlConnectionProperties) {
        return mysqlConnectionProperties.keystoreAbsoluteFilePath;
    }

    private static void validateProperties(MysqlConnectionProperties mysqlConnectionProperties) throws FileNotFoundException, AccessDeniedException {
        // Password is optional in mysql
        if (Strings.isNullOrEmpty(mysqlConnectionProperties.user)
                || Strings.isNullOrEmpty(mysqlConnectionProperties.host)
                || mysqlConnectionProperties.port == null) {
            throw new IllegalArgumentException(String.format("Mysql connection properties incomplete: %s", mysqlConnectionProperties.toString()));
        }

        String keyStoreFile = getKeyStoreFile(mysqlConnectionProperties);
        if (!Strings.isNullOrEmpty(keyStoreFile)) {
            validateFile(new File(keyStoreFile));
            if (Strings.isNullOrEmpty(mysqlConnectionProperties.keystorePassword)) {
                throw new IllegalArgumentException("Mysql connection properties keystorePassword is missing");
            }
        }
    }

    private static void validateFile(File file) throws FileNotFoundException, AccessDeniedException {
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File: %s not found", file));
        }
        if (!file.canRead()) {
            throw new AccessDeniedException(String.format("File: %s has no read permission", file));
        }
    }

    public static String getUrlOptions(MysqlConnectionProperties mysqlConnectionProperties) {
        StringBuilder stringBuilder = new StringBuilder(urlOptions);
        stringBuilder.append("&zeroDateTimeBehavior=convertToNull").
                append("&jdbcCompliantTruncation=false").
                append("&cachePrepStmts=true"). // do caching over the driver instead of in the DS pool
                append("&prepStmtCacheSize=250").
                append("&prepStmtCacheSqlLimit=2048");

        if (mysqlConnectionProperties.requireSSL != null) {
            stringBuilder.append("&requireSSL=" + (mysqlConnectionProperties.requireSSL ? "true" : "false"));
        }
        if (mysqlConnectionProperties.verifyServerCertificate != null) {
            stringBuilder.append("&verifyServerCertificate=" + (mysqlConnectionProperties.verifyServerCertificate ? "true" : "false"));
        }
        if (mysqlConnectionProperties.keystoreAbsoluteFilePath != null) {
            stringBuilder.append("&clientCertificateKeyStoreUrl=file://" + mysqlConnectionProperties.keystoreAbsoluteFilePath);
        }
        if (mysqlConnectionProperties.keystorePassword != null) {
            stringBuilder.append("&clientCertificateKeyStorePassword=" + mysqlConnectionProperties.keystorePassword);
        }
        return stringBuilder.toString();
    }
}
