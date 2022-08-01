package Core;

import HeadLibs.Annotations.IntRange;
import HeadLibs.Helper.HRandomHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Get available port.
 * @author xuxiaocheng
 */
@SuppressWarnings({"MagicNumber", "unused"})
public class PortManager {
    private static final int timeout = 2000;

    private static final Collection<Integer> checkedPortsFlag = new HashSet<>();
    private static boolean checkPortAvailableInFlag(@NotNull String host, @IntRange(minimum = 1, maximum = 65535) int port, boolean isClient) {
        if (checkedPortsFlag.contains(port))
            return false;
        checkedPortsFlag.add(port);
        return isClient ? portIsAvailableForClient(host, port) : portIsAvailableForServer(host, port);
    }

    /**
     * Get next available port in the host with random.
     * @param hostIn the host
     * @param isClient true - check with {@link PortManager#portIsAvailableForClient(String, int)}.
     *                 false - check with {@link PortManager#portIsAvailableForServer(String, int)}.
     * @return 0 - failed. others - found port.
     */
    @IntRange(minimum = 1, maximum = 65535)
    public static synchronized int getNextAvailablePortRandom(@Nullable String hostIn, boolean isClient) {
        if (hostIn == null)
            return 0;
        String host = hostIn;
        if (host.startsWith("https://"))
            host = host.substring(8);
        if (host.startsWith("http://"))
            host = host.substring(7);
        RandomGenerator random = new Random(HRandomHelper.getSeed("Craftworld"));
        synchronized (checkedPortsFlag) {
            checkedPortsFlag.clear();
            int r = HRandomHelper.nextInt(random, 1, 65535);
            while (!checkPortAvailableInFlag(host, r, isClient)) {
                if (checkedPortsFlag.size() > 500) {
                    for (int i = 1; i < 65536; ++i)
                        if (checkPortAvailableInFlag(host, i, isClient))
                            return i;
                    return 0;
                }
                r = HRandomHelper.nextInt(random, 1, 65535);
            }
            return r;
        }
    }

    /**
     * Get next available port in the host with order.
     * @param hostIn the host
     * @param isClient true - check with {@link PortManager#portIsAvailableForClient(String, int)}.
     *                 false - check with {@link PortManager#portIsAvailableForServer(String, int)}.
     * @return 0 - failed. others - found port.
     */
    @IntRange(minimum = 1, maximum = 65535)
    public static synchronized int getNextAvailablePortOrderly(@Nullable String hostIn, boolean isClient) {
        if (hostIn == null)
            return 0;
        String host = hostIn;
        if (host.startsWith("https://"))
            host = host.substring(8);
        if (host.startsWith("http://"))
            host = host.substring(7);
        synchronized (checkedPortsFlag) {
            checkedPortsFlag.clear();
            for (int i = 1; i < 65536; ++i)
                if (checkPortAvailableInFlag(host, i, isClient))
                    return i;
            return 0;
        }
    }

    /**
     * Check port is available with the host for client.
     * @param host the host
     * @param port the port
     * @return true - available. false - unavailable.
     */
    public static boolean portIsAvailableForClient(@Nullable String host, int port) {
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

    /**
     * Check port is available with the host for server.
     * @param host the host
     * @param port the port
     * @return true - available. false - unavailable.
     */
    public static boolean portIsAvailableForServer(@Nullable String host, int port) {
        if (host == null)
            return false;
        if (port < 1 || port > 65535)
            return false;
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        try {
            ServerSocket socket = new ServerSocket();
            socket.bind(socketAddress);
            socket.close();
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }
}
