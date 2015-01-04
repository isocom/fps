package name.prokop.bart.fps.server;

import org.springframework.stereotype.Service;

@Service
public class LicensingService {

    private static int counter = 0;

    public void increaseCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

}
