package server.com.hsbc.jerseyfilter;

import com.amazonaws.services.mq.model.BadRequestException;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * To prevent user from submitting JSON with HTML tags in it.
 */
public class HtmlFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        if (MediaType.APPLICATION_JSON_TYPE.equals(containerRequest.getMediaType())) { // consume the stream
            InputStream stream = containerRequest.getEntityInputStream();
            String origContent;
            try {
                origContent = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
            } catch (IOException e) { // could happen if not UTF8-compliant
                throw new BadRequestException("Input is not valid.");
            }
            if (Jsoup.isValid(origContent, Whitelist.none())) {  // ensure it does not contain any HTML tags
                // have to put it back into the request since the stream is consumed above
                containerRequest.setEntityInputStream(new ByteArrayInputStream(origContent.getBytes(StandardCharsets.UTF_8)));
                return containerRequest;
            } else {
                throw new BadRequestException("HTML input is not allowed!");
            }
        } else { // else avoid consuming anything
            return containerRequest;
        }
    }
}
