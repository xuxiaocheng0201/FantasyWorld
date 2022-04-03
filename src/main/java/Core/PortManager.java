package Core;

import org.jetbrains.annotations.Range;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.random.RandomGenerator;

@SuppressWarnings("MagicNumber")
public class PortManager {
    private static final Collection<Integer> checkedPort = new HashSet<>();
    private static boolean checkPortAvailable(@Range(from = 1, to = 65535) int port) {
        if (checkedPort.contains(port))
            return false;
        checkedPort.add(port);
        try {
            Closeable server = new ServerSocket(port);
            server.close();
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    @Range(from = 0, to = 65535)
    public static int getNextAvailablePort() {
        checkedPort.clear();
        long t1 = System.currentTimeMillis();
        RandomGenerator random = new SecureRandom("Craftworld".getBytes());
        int r = random.nextInt();
        while (r < 1 || r > 65535 || !checkPortAvailable(r)) {
            if (System.currentTimeMillis() - t1 > 3000) {
                for (int i = 1; i < 65536; ++i)
                    if (checkPortAvailable(i))
                        return i;
                return 0;
            }
            r = random.nextInt();
        }
        return r;
    }

    @Range(from = 0, to = 65535)
    public static int getNextAvailablePortQuickly() {
        checkedPort.clear();
        for (int i = 1; i < 65536; ++i)
            if (checkPortAvailable(i))
                return i;
        return 0;
    }

    public static boolean checkPortUnavailable(int port) {
        if (port < 1 || port > 65535)
            return false;
        try {
            Closeable server = new ServerSocket(port);
            server.close();
            return false;
        } catch (IOException exception) {
            return true;
        }
    }
}
