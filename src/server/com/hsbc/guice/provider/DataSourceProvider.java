package server.com.hsbc.guice.provider;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import server.com.hsbc.jdbc.JDBCProperties;
import server.com.hsbc.util.Log;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {
    private HikariDataSource ds;
    private HikariDataSource dsForLongRunningQueries;
    private static DataSourceProvider instance;
    private final Log log = new Log(DataSourceProvider.class);

    public synchronized static DataSourceProvider getInstance() {
        if (instance == null) {
            instance = new DataSourceProvider(MetricsProvider.getInstance().getMetricRegistry());
        }
        return instance;
    }

    private DataSourceProvider(MetricRegistry metric) {
        buildDataSourceProvider(metric, false);
        buildDataSourceProvider(metric, true);
    }

    private void buildDataSourceProvider(MetricRegistry metric, boolean isForLongRunningQueries) {
        HikariDataSource ds = new HikariDataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                try {
                    return super.getConnection();
                } catch (SQLException e) {
                    log.error("Failed to get db connection. dumping hikari cp pool stats");
                    throw e;
                }
            }
        };

        JDBCProperties jdbcProp = JDBCProperties.getInstance();
        String url = new StringBuilder("jdbc:mysql://").
                append(jdbcProp.getHost()).
                append(":").
                append(jdbcProp.getPort()).
                append("/").
                append(jdbcProp.getDBName()).
                append(jdbcProp.getUrlOptions()).toString();

        ds.setJdbcUrl(url);
        ds.setUsername(jdbcProp.getUser());
        ds.setPassword(jdbcProp.getPassword());

        ds.setRegisterMbeans(true);
        ds.setMetricRegistry(metric);

        if (isForLongRunningQueries) {
            ds.setPoolName("DB_LONG_RUNNING_QUERY_POOL");
            this.dsForLongRunningQueries = ds;
        } else {
            ds.setPoolName("DB_POOL");
            this.ds = ds;
        }
    }

    public DataSource getDataSource() {
        return ds;
    }

    public DataSource getDataSourceForLongRunningQueries() {
        return dsForLongRunningQueries;
    }

    public void closeDataSource() {
        log.info("DataSourceProvider closing the datasource");
        ds.close();
        dsForLongRunningQueries.close();
    }
}
