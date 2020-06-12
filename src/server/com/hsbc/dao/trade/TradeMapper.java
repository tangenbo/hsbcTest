package server.com.hsbc.dao.trade;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import server.com.hsbc.entity.Trade;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by etang on 2020/6/11
 */
public class TradeMapper implements ResultSetMapper<Trade> {
    @Override
    public Trade map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        Trade trade = new Trade();
        trade.setId(rs.getLong("id"));
        trade.setTradeDate(rs.getLong("trade_date"));
        trade.setTradeAmount(rs.getInt("trade_amount"));
        trade.setTradeType(rs.getString("trade_type"));
        trade.setCurrencyPair(rs.getString("currency_pair"));
        return trade;
    }
}
