package transfer.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.catalina.LifecycleException;
import transfer.config.TransferModule;

public class Application {

  public static void main(String[] args) throws LifecycleException {
    Injector injector = Guice.createInjector(new TransferModule());
    DatabaseSetup.start();
    ServletContainerSetup.start(injector);
  }
}
