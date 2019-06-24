package transfer.config;

import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig extends Application {

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        properties.put("jersey.config.server.provider.packages", "transfer.controller");
        properties.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.jackson.JacksonFeature");
        return properties;
    }
}
