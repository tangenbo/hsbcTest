package server.com.hsbc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import server.com.hsbc.guice.module.MainModule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.bridge.SLF4JBridgeHandler;
import server.com.hsbc.jdbc.DBServerControl;
import server.com.hsbc.jettyFilter.CustomSecurityHeadersFilter;
import server.com.hsbc.util.Log;

import javax.servlet.DispatcherType;
import java.util.*;

public class ServerEntrance {
    private static Injector injector;
    private Log log;

    public static void main(String[] args) {
        Log vpLog = new Log(ServerEntrance.class);
        try {
            ServerEntrance instance = new ServerEntrance();
            instance.startServer();
        } catch (Throwable e) {
            vpLog.fatal("Server initialization failed", e);
            System.exit(1);
        }
    }

    private void startServer() throws Exception {
        log = new Log(ServerEntrance.class);

        log.info("===== Server start");

        // start database server next
        log.info("===== Start database server");
        try {
            new DBServerControl().startDbServer();
        } catch (Exception e) {
            log.error("Failed to start database ", e);
        }
        initServer();
        log.info("===== Start jetty");
        startJetty(log);
        log.info("===== Server is ready");
    }

    private static void createInjector() {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install(new MainModule());
            }
        });
    }

    private static void startJetty(Log log) throws Exception {
        List<ContextHandler.AliasCheck> removeAliasCheck = new ArrayList<>();
        removeAliasCheck.add(new ContextHandler.ApproveAliases());

        SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once
        // during the initialization phase of your application
        SLF4JBridgeHandler.install();

        QueuedThreadPool qtp = new QueuedThreadPool();
        qtp.setIdleTimeout(60000);
        qtp.setMinThreads(10);
        qtp.setMaxThreads(128);
        qtp.setName("jetty");
        Server server = new Server(qtp);

        // create HttpConnector so that we can redirect HTTP connections to HTTPS
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSecurePort(443);
        httpConfig.setSecureScheme("https");

        ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        httpConnector.setName("unsecured");
        httpConnector.setPort(80);
        httpConnector.setReuseAddress(true);
        httpConnector.setIdleTimeout(60000);
        server.addConnector(httpConnector);

        // Setup the basic appliation "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler defaultContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        // add custom security headers
        defaultContextHandler.addFilter(CustomSecurityHeadersFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        // add a servlet for REST APIs
        ServletHolder holderRestAPIs = new ServletHolder("restApis", new DefaultServlet());
        holderRestAPIs.setName("Rest APIs");
        defaultContextHandler.addServlet(holderRestAPIs, "/rest/*");
        FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
        defaultContextHandler.addFilter(guiceFilter, "/rest/*", EnumSet.allOf(DispatcherType.class));

        String currentDir = System.getProperty("user.dir");

        // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
        // It is important that this is last.
        ServletHolder holderPwd = new ServletHolder("staticContent", new DefaultServlet());
        holderPwd.setInitParameter("resourceBase", currentDir + "\\web");

        // enable gzip support for static content
        FilterHolder gzip = new FilterHolder(new GzipFilter());
        gzip.setAsyncSupported(true);
        gzip.setInitParameter("mimeTypes", "text/html,text/plain,text/xml,text/javascript,text/css,application/javascript,image/svg+xml");
        gzip.setInitParameter("minGzipSize", "256");
        EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
                DispatcherType.INCLUDE, DispatcherType.REQUEST);
        defaultContextHandler.addFilter(gzip, "/*", all);

        holderPwd.setInitParameter("dirAllowed", "false");
        holderPwd.setInitParameter("gzip", "true");
        holderPwd.setInitParameter("aliases", "true");
        defaultContextHandler.addServlet(holderPwd, "/");

        RewriteHandler urlRewriteHandler = new RewriteHandler();
        urlRewriteHandler.setHandler(defaultContextHandler);

        defaultContextHandler.setAliasChecks(removeAliasCheck);

        HandlerList hlist = new HandlerList();
        hlist.addHandler(urlRewriteHandler);
        hlist.addHandler(defaultContextHandler);

        //No handlers in HandlerCollection could block following handlers by marking request handled
        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[]{hlist});

        server.setHandler(handlerCollection);

        server.start();
        log.info("===== Jetty server started successfully");
    }

    private void initServer() {
        Thread.currentThread().setName("Main");

        log = new Log(ServerEntrance.class);

        log.info("===== Create Guice injector");
        createInjector();
    }
}
