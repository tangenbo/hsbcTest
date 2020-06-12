package server.com.hsbc.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.skife.jdbi.v2.DBI;
import server.com.hsbc.dao.trade.TradeDao;
import server.com.hsbc.entity.Trade;

import java.util.List;

/**
 * Created by etang on 2020/6/11
 */
@Singleton
public class TradeManager {
    private final TradeDao tradeDao;

    @Inject
    TradeManager(DBI dbi) {
        this.tradeDao = dbi.onDemand(TradeDao.class);
    }

    public long countAll() {
        return tradeDao.countAll();
    }

    public List<Trade> getAll(Integer pageSize, Integer pageNumber) {
        return tradeDao.getAll(pageSize, pageNumber - 1);
    }

    public Trade getById(long id) {
        return tradeDao.getById(id);
    }

    public int add(Trade trade) {
        return tradeDao.add(trade);
    }

    public int update(Trade trade) {
        return tradeDao.update(trade);
    }

    public int delete(long id) {
        return tradeDao.delete(id);
    }
}
