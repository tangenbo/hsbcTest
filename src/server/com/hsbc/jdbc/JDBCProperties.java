package server.com.hsbc.jdbc;

import com.google.common.base.Strings;

public final class JDBCProperties {
    private MysqlConnectionProperties mysqlConnectionProperties;

    private String dbPropertyFile;
    private static JDBCProperties instance;

    public static synchronized JDBCProperties getInstance() {
        if (instance == null) {
            instance = new JDBCProperties();
        }
        return instance;
    }

    private JDBCProperties() {
        mysqlConnectionProperties = JDBCPropertiesUtil.getConnectionProperties();
        dbPropertyFile = getPropertyFile();
    }

    private String getPropertyFile(){
        return "properties/mysql.properties";
    }

    public boolean useCert() {
        return !Strings.isNullOrEmpty(mysqlConnectionProperties.keystoreAbsoluteFilePath)
                && !Strings.isNullOrEmpty(mysqlConnectionProperties.keystorePassword);
    }

    public Boolean getRequireSSL() {
        return mysqlConnectionProperties.requireSSL;
    }

    public Boolean getVerifyServerCertificate() {
        return mysqlConnectionProperties.verifyServerCertificate;
    }

    public String getKeystoreAbsoluteFilePath() {
        return mysqlConnectionProperties.keystoreAbsoluteFilePath;
    }

    public String getKeystorePassword() {
        return mysqlConnectionProperties.keystorePassword;
    }

    public String getHost() {
        return mysqlConnectionProperties.host;
    }

    public String getUser() {
        return mysqlConnectionProperties.user;
    }

    public String getPassword() {
        return mysqlConnectionProperties.password;
    }

    public String getDBName() {
        return mysqlConnectionProperties.database;
    }

    public int getPort() {
        return mysqlConnectionProperties.port;
    }

    public String getDbPropertyFile() {
        return dbPropertyFile;
    }

    public String getUrlOptions(){
        return JDBCPropertiesUtil.getUrlOptions(mysqlConnectionProperties);
    }
}
