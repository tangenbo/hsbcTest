package server.com.hsbc.dao.trade;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import server.com.hsbc.entity.Trade;

import java.util.List;

/**
 * Created by etang on 2020/6/11
 */

@RegisterMapper({TradeMapper.class})
@UseStringTemplate3StatementLocator
public interface TradeDao {
    @SqlQuery("select count(id) from trade")
    long countAll();

    @SqlQuery("select id,trade_date,trade_amount,trade_type,currency_pair from trade limit :pageSize offset :pageNumber")
    List<Trade> getAll(@Bind("pageSize") Integer pageSize, @Bind("pageNumber") Integer pageNumber);

    @SqlQuery("select id,trade_date,trade_amount,trade_type,currency_pair from trade where id=:id")
    Trade getById(@Bind("id") long id);

    @SqlUpdate("insert into trade (trade_date,trade_amount,trade_type,currency_pair) values (:tradeDate,:tradeAmount,:tradeType,:currencyPair)")
    int add(@BindBean Trade trade);

    @SqlUpdate("update trade set trade_date=:tradeDate,trade_amount=:tradeAmount,trade_type=:tradeType,currency_pair=:currencyPair where id=:id")
    int update(@BindBean Trade trade);

    @SqlUpdate("delete from trade where id=:id")
    int delete(@Bind("id") long id);
}
