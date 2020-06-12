package server.com.hsbc.guice.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.Provider;

public class ObjectMapperProvider implements Provider<ObjectMapper> {
    @Override
    public ObjectMapper get() {
        final ObjectMapper mapper = new ObjectMapper();

        // To keep the contract between the json and POJO lenient. This allows additional keys in json that are not in POJO
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.registerModule(new GuavaModule());
        return mapper;
    }
}
