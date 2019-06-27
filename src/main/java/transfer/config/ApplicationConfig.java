package transfer.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class ApplicationConfig {

  public static Configuration getConfig() {
    Configuration config = null;
    try {
      config = new Configurations().properties(new File("application.properties"));
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
    return config;
  }
}
