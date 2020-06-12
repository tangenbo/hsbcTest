package server.com.hsbc.jerseyfilter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * User: oozduygu
 * Date: 7/29/14
 * Time: 5:34 PM
 */
public class HttpResponseFilter implements ContainerResponseFilter {
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        return response;
    }
}
