package server.com.hsbc.jdbc;

public class MysqlConnectionProperties {
    public String user;
    public String password;
    public String host;
    public Integer port;
    public String database;

    public Boolean requireSSL;
    public Boolean verifyServerCertificate;
    public String keystoreAbsoluteFilePath;
    public String keystorePassword;

    public void overwriteProperties(MysqlConnectionProperties newProperty) {
        if (newProperty == null) return;

        if (newProperty.user != null) this.user = newProperty.user;
        if (newProperty.password != null) this.password = newProperty.password;
        if (newProperty.host != null) this.host = newProperty.host;
        if (newProperty.port != null) this.port = newProperty.port;
        if (newProperty.database != null) this.database = newProperty.database;

        if (newProperty.requireSSL != null) this.requireSSL = newProperty.requireSSL;
        if (newProperty.verifyServerCertificate != null)
            this.verifyServerCertificate = newProperty.verifyServerCertificate;
        if (newProperty.keystoreAbsoluteFilePath != null)
            this.keystoreAbsoluteFilePath = newProperty.keystoreAbsoluteFilePath;
        if (newProperty.keystorePassword != null) this.keystorePassword = newProperty.keystorePassword;
    }
}
