package server.com.hsbc.guice.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.VoidHandleCallback;


public class JdbiProvider implements Provider<DBI> {
    private static DBI dbi;

    @Inject
    public JdbiProvider() {
        dbi = new DBIWrapper(DataSourceProvider.getInstance().getDataSource());

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

