package transfer.app;

import com.google.inject.Injector;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import transfer.controller.AccountController;
import transfer.controller.TransferController;

import java.io.File;

import static org.glassfish.jersey.server.ServerProperties.*;
import static transfer.config.ApplicationConfig.getConfig;

class ServletContainerSetup {

  private static final String HOST = getConfig().getString("application.host");
  private static final int PORT = getConfig().getInt("application.port");
  private static final String SERVLET_NAME = getConfig().getString("servlet.name");

  static void start(Injector injector) throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    tomcat.setHostname(HOST);
    tomcat.setPort(PORT);
    tomcat.getConnector();

    Context context = tomcat.addWebapp("", new File(".").getAbsolutePath());
    Tomcat.addServlet(context, SERVLET_NAME, servlet(injector));
    context.addServletMappingDecoded("/api/*", SERVLET_NAME);

    tomcat.start();
    tomcat.getServer().await();
  }

  private static ServletContainer servlet(Injector injector) {
    return new ServletContainer(
        new ResourceConfig()
            .property(BV_SEND_ERROR_IN_RESPONSE, true)
            .property(PROVIDER_PACKAGES, "transfer.controller")
            .property(PROVIDER_CLASSNAMES, "org.glassfish.jersey.jackson.JacksonFeature")
            .register(ValidationFeature.class)
            .register(injector.getInstance(AccountController.class))
            .register(injector.getInstance(TransferController.class)));
  }
}
