package server.com.hsbc.jerseyfilter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Filter method is called before each request
 */
public class HttpRequestFilter implements ContainerRequestFilter {
    @Context
    private HttpServletRequest hsr;

    public ContainerRequest filter(ContainerRequest request) {
        return request;
    }
}
