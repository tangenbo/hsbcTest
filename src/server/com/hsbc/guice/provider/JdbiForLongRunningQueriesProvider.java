package server.com.hsbc.guice.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.VoidHandleCallback;

public class JdbiForLongRunningQueriesProvider implements Provider<DBI> {
    private static DBI dbi;

    @Inject
    public JdbiForLongRunningQueriesProvider() {
        dbi = new DBIWrapper(DataSourceProvider.getInstance().getDataSourceForLongRunningQueries());

        // this is a hack to get the connection pool constructed for the very first time
        dbi.withHandle(new VoidHandleCallback() {
            @Override
            protected void execute(Handle handle) throws Exception {
                handle.execute("select 1");
            }
        });
    }

    @Override
    public DBI get() {
        return dbi;
    }
}
