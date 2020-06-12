package server.com.hsbc.resource;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.apache.commons.collections.map.HashedMap;
import server.com.hsbc.entity.Trade;
import server.com.hsbc.manager.TradeManager;
import server.com.hsbc.util.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/trade")
public class TradeResource {
    private final TradeManager tradeManager;
    private final Log log = new Log(TradeResource.class);

    @Inject
    public TradeResource(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber) {
        List<Trade> tradeList = tradeManager.getAll(pageSize, pageNumber);
        long count = tradeManager.countAll();
        Map<String, Object> result = new HashMap<>();
        result.put("rows", tradeList);
        result.put("count", count);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        Trade trade = tradeManager.getById(id);
        return Response.ok(trade).build();
    }

    @POST
    public Response add(Trade trade) {
        if (dataIsValid(trade)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("All fields are required").build();
        }
        tradeManager.add(trade);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(Trade trade, @PathParam("id") long id) {
        if (dataIsValid(trade)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("All fields are required").build();
        }
        trade.setId(id);
        tradeManager.update(trade);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        tradeManager.delete(id);
        return Response.noContent().build();
    }

    private boolean dataIsValid(Trade trade) {
        return Strings.isNullOrEmpty(trade.getCurrencyPair()) || Strings.isNullOrEmpty(trade.getTradeType()) || trade.getTradeDate() == null || trade.getTradeAmount() == null;
    }
}
