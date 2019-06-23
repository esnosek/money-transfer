package transfer.config;

import javax.ws.rs.core.Application;
import java.util.Map;

public class ApplicationConfig extends Application {

    @Override
    public Map<String, Object> getProperties() {
        return Map.of("jersey.config.server.provider.packages", "transfer.controller",
                "jersey.config.server.provider.classnames", "org.glassfish.jersey.jackson.JacksonFeature");
    }


}
