package server.com.hsbc.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.guice.JerseyServletModule;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new GuiceServletModule());
        install(new JerseyServletModule());
        install(new DbModule());
        bind(GuiceFilter.class);
    }
}
