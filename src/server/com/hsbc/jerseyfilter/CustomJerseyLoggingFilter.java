package server.com.hsbc.jerseyfilter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import server.com.hsbc.util.Log;

/**
 * User: psingh
 * Date: 7/11/14
 * Time: 1:51 PM
 */
public class CustomJerseyLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private Log jerseyLog = new Log(CustomJerseyLoggingFilter.class);

    @Override
    public ContainerRequest filter(ContainerRequest req) {
        jerseyLog.debug("REQUEST: %s %s, %s", req.getMethod(), req.getAbsolutePath(), req.getQueryParameters());
        return req;
    }

    @Override
    public ContainerResponse filter(ContainerRequest req, ContainerResponse res) {
        jerseyLog.debug("RESPONSE: %s %s %d %s", req.getMethod(), req.getAbsolutePath(),
                res.getStatus(), res.getStatusType());
        return res;
    }
}
