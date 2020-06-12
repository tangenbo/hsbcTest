package server.com.hsbc.jdbc;

import com.mysql.cj.jdbc.MysqlDataSource;
import server.com.hsbc.util.Log;
import server.com.hsbc.util.OSCommand;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;


public class DBServerControl {
    private final Log log = new Log(DBServerControl.class);

    private static final String MYSQLD = "mysqld";
    private static final String MYSQL_BASEDIR = "db/";
    private static final String MYSQL_ADMIN_PROG = "bin/mysqladmin";
    private static final String MYSQL_DATA_DIR_NAME = "data";
    private JDBCProperties jdbc;

    public DBServerControl() {
        jdbc = JDBCProperties.getInstance();
    }

    private String getAdminCommand() {
        return MYSQL_BASEDIR + MYSQL_ADMIN_PROG;
    }

    private String getLanguageDir() {
        return "english";
    }

    private boolean isDBServerRunning() {
        try (Connection conn = getApplicationConnection()) {
            return true;
        } catch (Exception e) {
            // ignore exception and move on
        }
        // Failed to get connection, but this does not mean that
        // the process is not running ...
        String output = runAdminCommand("status");
        return output.contains("Uptime");
    }

    private String[] getMySqldCommand() {
        String serverPropertyFile = jdbc.getDbPropertyFile();
        String socketFile = "--socket=mysql.socket";

        return new String[]{
                MYSQL_BASEDIR + "bin/" + MYSQLD,
                "--defaults-file=" + serverPropertyFile,
                "--basedir=" + MYSQL_BASEDIR,
                "--datadir=" + MYSQL_DATA_DIR_NAME,
                "--port=" + jdbc.getPort(),
                "--bind_address=127.0.0.1",
                "--log-error=" + new File("logs/mysqld.log").getAbsolutePath(),
                socketFile,
                "--lc-messages-dir=" + getLanguageDir(),
                "--lc_messages=en_US"
        };
    }

    public boolean startDbServer() throws Exception {
        log.info("Trying to start MySQL server");

        if (isDBServerRunning()) {
            log.info("MySQL is already running...");
            return true;
        }

        boolean mysqlServerIsUp = startMySqlServer();

        if (mysqlServerIsUp) {
            log.info("MySQL server started");
            return true;
        } else {
            log.error("Failed to communicate with DB server");
            throw new Exception("Unable to communicate with MySQL");
        }
    }

    private boolean startMySqlServer() throws IOException, InterruptedException {
        System.out.println("Starting the MySQL server ...");
        String[] cmd = getMySqldCommand();
        OSCommand.runOSCommand(cmd, true);

        // Wait up to 3 minutes for MySql to be ready.  Have seen cases where
        // it took it more than one minute to start.
        boolean mysqlServerIsUp = false;
        for (int i = 0; i < 30 * 60 * 1000; i = i + 10 * 1000) {
            // Give the server a chance to initialize
            Thread.sleep(10 * 1000);
            if (mysqlServerIsUp = isDBServerRunning()) {
                System.out.println("MySQL server has been started...");
                log.info("MySQL server has been started...");
                break;
            }
        }
        return mysqlServerIsUp;
    }


    public void stopDbServer() {
        try {
            int stopCount = 0;
            if (isDBServerRunning()) {
                log.info("Trying to stop DB server..");
                if (!runAdminCommand("shutdown").isEmpty()) {
                    // we told it to die, now wait for it and give it enough
                    // time to die gracefully. Five minutes may be needed because we use
                    // innodb slow shutdown so that all the logs are properly flushed every time.
                    while (stopCount < 300) {
                        if (!isDBServerRunning()) {
                            break;
                        }

                        log.info("Waiting for DB server to go down..");
                        TimeUnit.SECONDS.sleep(1);
                        stopCount++;
                    }
                    log.info("DB process stopped");
                }
            }
        } catch (InterruptedException ex) {
            log.error("Failed to wait for DB process to stop");
        }
    }

    private String runAdminCommand(String command) {
        log.info("Running mysqladmin command: %s", command);
        String user = jdbc.getUser();
        String password = jdbc.getPassword();

        String host = "--host=" + jdbc.getHost();
        String socket = "--socket=" + getSocketFile();
        String port = "--port=" + jdbc.getPort();

        String[] cmd = new String[7];

        //db/bin/mysqladmin --user=root --password=root --port=3306 --socket=db/data/mysql.socket <cmd>
        cmd[0] = getAdminCommand();
        cmd[1] = port;
        cmd[2] = host;
        cmd[3] = socket;
        cmd[4] = "--user=" + user;
        cmd[5] = "--password=" + password;
        cmd[6] = command;

        try {
            Process p = OSCommand.runOSCommand(cmd, false);
            String ret = OSCommand.gatherStdoutStderr(p);
            log.info("mysqladmin command output: %s", ret.replace("\n", "\\n"));
            return ret;
        } catch (Exception ex) {
            log.error("Failed to run mysqladmin command", ex);
            System.out.println("Error running mysqladmin: " + ex.toString());
            return "";
        }
    }

    private Connection getApplicationConnection() throws SQLException {
        return getDBConnection(jdbc.getDBName());
    }

    private Connection getDBConnection(String database) throws SQLException {
        String host = jdbc.getHost();
        String user = jdbc.getUser();
        String password = jdbc.getPassword();
        int port = jdbc.getPort();
        String dbName = database;

        MysqlDataSource datasrc = new MysqlDataSource();

        try {
            datasrc.setDatabaseName(dbName);
            datasrc.setUser(user);
            datasrc.setServerName(host);
            datasrc.setPassword(password);
            datasrc.setPort(port);
            if (jdbc.getRequireSSL() != null) {
                datasrc.setRequireSSL(jdbc.getRequireSSL());
            }
            if (jdbc.getVerifyServerCertificate() != null) {
                datasrc.setVerifyServerCertificate(jdbc.getVerifyServerCertificate());
            }
            if (jdbc.useCert()) {
                datasrc.setClientCertificateKeyStoreUrl("file://" + jdbc.getKeystoreAbsoluteFilePath());
                datasrc.setClientCertificateKeyStorePassword(jdbc.getKeystorePassword());
            }
            return datasrc.getConnection();
        } catch (SQLException seq) {
            if (seq.getSQLState().equalsIgnoreCase("08S01")) {
                System.out.println("MySQL server @" + host + ':' + port + " is not running");
            }
            log.error("Error getting a JDBC connection - likely MySqld is not running");
            throw seq;
        }
    }

    private String getSocketFile() {
        return MYSQL_BASEDIR + MYSQL_DATA_DIR_NAME + "/mysql.socket";
    }
}
