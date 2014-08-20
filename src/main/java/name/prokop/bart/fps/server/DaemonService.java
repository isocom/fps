package name.prokop.bart.fps.server;

import java.io.File;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class DaemonService {

    private final File terminator = new File("stop");
    private boolean pleaseTerminate = false;

    @PostConstruct
    private void start() {
        System.out.println("Daemon Started");
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!pleaseTerminate) {
                    try {
                        Thread.sleep(10);
                        pleaseTerminate = terminator.exists();
                        if (pleaseTerminate) {
                            terminator.delete();
                            terminator.deleteOnExit();
                        }
                    } catch (InterruptedException ioe) {
                    }
                }
            }
        }).start();
    }

    @PreDestroy
    private void destroy() {
        // in case the application context would be closed externally
        pleaseTerminate = true;
    }

}
