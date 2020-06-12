package server.com.hsbc.guice.provider;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MetricsProvider {
    private final MetricRegistry metricRegistry;
    private static MetricsProvider instance;

    public synchronized static MetricsProvider getInstance() {
        if (instance == null) {
            instance = new MetricsProvider();
        }
        return instance;
    }

    private MetricsProvider() {
        metricRegistry = new MetricRegistry();
        Slf4jReporter.forRegistry(metricRegistry).
                outputTo(LoggerFactory.getLogger("metrics")).
                convertRatesTo(TimeUnit.SECONDS).
                convertDurationsTo(TimeUnit.MILLISECONDS).
                build().
                start(30, TimeUnit.SECONDS);

        JmxReporter.forRegistry(metricRegistry).build().start();

    }

    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }
}
