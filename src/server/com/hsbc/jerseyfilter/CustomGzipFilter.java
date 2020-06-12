package server.com.hsbc.jerseyfilter;

import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;


/**
 * User: oozduygu
 * Date: 7/29/14
 */
public class CustomGzipFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static GZIPContentEncodingFilter gzipContentEncodingFilter = new GZIPContentEncodingFilter();

    public ContainerRequest filter(ContainerRequest request) {
        return gzipContentEncodingFilter.filter(request);
    }

    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        return gzipContentEncodingFilter.filter(request, response);
    }
}
