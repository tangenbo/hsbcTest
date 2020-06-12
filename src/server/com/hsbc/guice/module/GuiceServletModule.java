package server.com.hsbc.guice.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.base.Joiner;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import server.com.hsbc.guice.provider.JacksonJsonProviderProvider;
import server.com.hsbc.guice.provider.ObjectMapperProvider;
import server.com.hsbc.jerseyfilter.*;

import java.util.HashMap;
import java.util.Map;

public class GuiceServletModule extends ServletModule {
    @Override
    protected void configureServlets() {

        // hook Jersey into Guice Servlet
        bind(GuiceContainer.class);

        // create configured singleton object mapper
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Scopes.SINGLETON);

        // hook Jackson into Jersey as the POJO <-> JSON mapper
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderProvider.class).in(Scopes.SINGLETON);

        Map<String, String> jerseyConfig = new HashMap<String, String>();
        jerseyConfig.put(ResourceConfig.FEATURE_DISABLE_WADL, "true");
        // IMPORTANT: need to specify the package where resources are located in order for Jersey to pick them up
        jerseyConfig.put(PackagesResourceConfig.PROPERTY_PACKAGES, "server.com.hsbc.resource");

        Joiner joiner = Joiner.on(",");
        String requestFilters = joiner.join(CustomJerseyLoggingFilter.class.getCanonicalName(),
                HttpRequestFilter.class.getCanonicalName(),
                HtmlFilter.class.getCanonicalName(),
                ParameterFilter.class.getCanonicalName(),
                CustomGzipFilter.class.getCanonicalName());
        jerseyConfig.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, requestFilters);

        String responseFilters = joiner.join(HttpResponseFilter.class.getCanonicalName(),  CustomJerseyLoggingFilter.class.getCanonicalName(),
                CustomGzipFilter.class.getCanonicalName());
        jerseyConfig.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS,
                responseFilters);

        serve("/rest/*").with(GuiceContainer.class, jerseyConfig);
    }
}

