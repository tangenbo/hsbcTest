package server.com.hsbc.guice.provider;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.tweak.HandleCallback;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;

public class DBIWrapper extends DBI{

    private static final int DEADLOCK_RETRY = 5;
    private static final int SQL_TRANSIENT_CONNECTION_EXCEPTION_RETRY = 5;
    private static final int SLEEP_LOWER_TIME_LIMIT = 5000; // 5sec in ms
    private static final int SLEEP_UPPER_TIME_LIMIT = 60000; // 60sec in ms

    public DBIWrapper(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public <ReturnType> ReturnType withHandle(HandleCallback<ReturnType> callback) throws CallbackFailedException
    {
        int retryCount = 0;
        while (true) {
            final Handle h = this.open();
            try {
                return callback.withHandle(h);
            } catch (Exception e) {
                if(isDeadlocked(e)){
                    if (++retryCount <= DEADLOCK_RETRY) {
                        sleepRandom(); // short delay before retry
                        continue;
                    }
                }
                throw new CallbackFailedException(e);
            } finally {
                h.close();
            }
        }
    }

    /**
     * If we fail to get database connection due to any reason, retry 'x' times.
     */
    @Override
    public Handle open()
    {
        int retryCount = 0;
        while(true) {
            try {
                return super.open();
            } catch (UnableToObtainConnectionException e) {
                if (ExceptionUtils.indexOfType(e, SQLTransientConnectionException.class) != -1) {
                    if(++retryCount <= SQL_TRANSIENT_CONNECTION_EXCEPTION_RETRY){
                        sleepRandom(); // short delay before retry
                        continue;
                    }
                }
                throw e;
            }
        }
    }

    private boolean isDeadlocked(Exception e) {
        if (ExceptionUtils.indexOfType(e, SQLException.class) != -1) {
            if (ExceptionUtils.getRootCause(e) instanceof SQLException) {
                SQLException sqlException = (SQLException) ExceptionUtils.getRootCause(e);
                return sqlException.getErrorCode() == MysqlErrorNumbers.ER_LOCK_DEADLOCK;
            }
        }
        return false;
    }

    private void sleepRandom(){ //wait random 5sec - 60 secs
        try {
            SecureRandom random = new SecureRandom();
            Thread.sleep(random.nextInt(SLEEP_UPPER_TIME_LIMIT - SLEEP_LOWER_TIME_LIMIT) + SLEEP_LOWER_TIME_LIMIT);
        } catch (InterruptedException e) {
            // I will ignore this for now
        }
    }
}
