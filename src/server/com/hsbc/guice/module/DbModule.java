package server.com.hsbc.guice.module;


import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.skife.jdbi.v2.DBI;
import server.com.hsbc.guice.provider.JdbiForLongRunningQueriesProvider;
import server.com.hsbc.guice.provider.JdbiProvider;
import server.com.hsbc.util.Log;

public class DbModule extends AbstractModule {
    private Log log = new Log(DbModule.class);
    public DbModule () {}
    @Override
    protected void configure() {
        try {
            install(new DBConnectModule());
            // create connection pool
            bind(DBI.class).toProvider(JdbiProvider.class).in(Scopes.SINGLETON);
            bind(DBI.class).annotatedWith(Names.named("LONG_RUNNING_QUERY_POOL")).toProvider(JdbiForLongRunningQueriesProvider.class).in(Scopes.SINGLETON);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    //The module for DB connection only
    class DBConnectModule extends AbstractModule {

        @Override
        protected void configure() {
        }
    }
}
