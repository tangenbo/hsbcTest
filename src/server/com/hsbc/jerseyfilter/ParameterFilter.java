package server.com.hsbc.jerseyfilter;

import com.amazonaws.services.mq.model.BadRequestException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.jsoup.Jsoup;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public class ParameterFilter implements ContainerRequestFilter {
    @Override
    public ContainerRequest filter(ContainerRequest request) {
        MultivaluedMap<String, String> parameters = request.getQueryParameters();
        for (List<String> values : parameters.values()){
            for(String value : values){
                if(value.length() > 2000){
                    throw new BadRequestException("Invalid length of parameter value");
                }
                if(isHtml(value)){
                    throw new BadRequestException("HTML is not allow in parameter value");
                }
            }
        }
        return request;
    }

    /**
     *
     * Basically, every string is a HTML even if it is poorly formatted or invalid,
     * what we do here is to see if there is any html element inside the input.
     * If parsing succeed, method text() on the returned Document object should give us a string which is different from the origin one.
     *  1. contains a valid html: "<a>hello</a>", "<a></a>"
     *  2. missing opening tag is considered valid: "</a>"
     *  3. missing closing tag is considered valid: "<a>"
     *  4. incomplete tag is considered valid: "<a"
     *  5. input ">" or "<" are considered invalid
     */
    public static boolean isHtml(String htmlString){
        if(htmlString == null || htmlString.isEmpty()){
            return false;
        }
        return !Jsoup.parse(htmlString).text().equals(htmlString);
    }
}
