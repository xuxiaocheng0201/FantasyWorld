package Core;

import HeadLibs.Helper.HRandomHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.random.RandomGenerator;

/**
 * Get available port.
 * @author xuxiaocheng
 */
@SuppressWarnings({"MagicNumber", "unused"})
public class PortManager {
    private static final int timeout = 2000;

    private static final Collection<Integer> checkedPortsFlag = new HashSet<>();
    private static boolean checkPortAvailableInFlag(@NotNull String host, @Range(from = 1, to = 65535) int port) {
        if (checkedPortsFlag.contains(port))
            return false;
        checkedPortsFlag.add(port);
        return portIsAvailable(host, port);
    }

    /**
     * Get next available port in the host with random.
     * @param hostIn the host
     * @return 0 - failed. others - found port.
     */
    @Range(from = 0, to = 65535)
    public static int getNextAvailablePortRandom(@Nullable String hostIn) {
        if (hostIn == null)
            return 0;
        String host = hostIn;
        if (host.startsWith("https://"))
            host = host.substring(8);
        if (host.startsWith("http://"))
            host = host.substring(7);
        checkedPortsFlag.clear();
        long t1 = System.currentTimeMillis();
        RandomGenerator random = new SecureRandom("Craftworld".getBytes());
        int r = HRandomHelper.nextInt(1, 65535);
        while (!checkPortAvailableInFlag(host, r)) {
            if (System.currentTimeMillis() - t1 > 3000) {
                for (int i = 1; i < 65536; ++i)
                    if (checkPortAvailableInFlag(host, i))
                        return i;
                return 0;
            }
            r = HRandomHelper.nextInt(1, 65535);
        }
        return r;
    }

    /**
     * Get next available port in the host with order.
     * @param hostIn the host
     * @return 0 - failed. others - found port.
     */
    @Range(from = 0, to = 65535)
    public static int getNextAvailablePortQuickly(@Nullable String hostIn) {
        if (hostIn == null)
            return 0;
        String host = hostIn;
        if (host.startsWith("https://"))
            host = host.substring(8);
        if (host.startsWith("http://"))
            host = host.substring(7);
        checkedPortsFlag.clear();
        for (int i = 1; i < 65536; ++i)
            if (checkPortAvailableInFlag(host, i))
                return i;
        return 0;
    }

    /**
     * Check port is available with the host.
     * @param host the host
     * @param port the port
     * @return true - available. false - unavailable.
     */
    public static boolean portIsAvailable(@Nullable String host, int port) {
        if (host == null)
            return false;
        if (port < 1 || port > 65535)
            return false;
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        Socket socket = new Socket();
        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }
}
