package com.xuxiaocheng.FantasyWorld.Platform;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionFormatException;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FantasyWorldPlatform {
    private FantasyWorldPlatform() {
        super();
    }

    private static @NotNull VersionSingle CurrentVersion = VersionSingle.EmptyVersion;
    static {
        try {
            FantasyWorldPlatform.CurrentVersion = VersionSingle.create("0.1.0");
        } catch (final VersionFormatException ignore) {
        }
    }

    @SuppressWarnings("ClassExplicitlyExtendsThread")
    private static class ShutdownHookThread extends Thread {
        private ShutdownHookThread(@NotNull final String name) {
            super(name);
        }

        @Override
        public void run() {
            FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Welcome to play again!");
        }
    }

    private static @NotNull HLog logger = HLog.DefaultLogger;
    static {
        if (!HLog.isDebugMode()) {
            final File dir = new File("logs");
            final File path = new File(dir, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss")) + ".log");
            try {
                if (!dir.exists()) {
                    if (!dir.mkdirs())
                        throw new IOException("Failed to create log directory.");
                } else if (!dir.isDirectory())
                    throw new IOException("Not a log directory.");
                if (path.exists() && !path.isFile())
                    throw new IOException("Not a log file.");
                if (!path.createNewFile())
                    throw new IOException("Failed to create log file.");
                try {
                    FantasyWorldPlatform.logger = HLog.createInstance("DefaultFileLogger", false,
                            new BufferedOutputStream(new FileOutputStream(path, true)));
                } catch (final FileNotFoundException exception) {
                    throw new IOException("Failed to get log file.", exception);
                }
            } catch (final IOException exception) {
                HLog.DefaultLogger.log(HLogLevel.FAULT, "Fail to create log file.");
                HLog.DefaultLogger.log(HLogLevel.FAULT, exception);
            }
        }
    }

    private static boolean runOnClient = false;
    private static boolean showJWindow = false;
    public static boolean isRunOnClient() {
        return FantasyWorldPlatform.runOnClient;
    }
    public static boolean isShowJWindow() {
        return FantasyWorldPlatform.showJWindow;
    }

    public static void main(@NotNull final String @NotNull [] args) {
        Thread.currentThread().setName("FantasyWorldPlatform/main");
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> FantasyWorldPlatform.logger.log(HLogLevel.FAULT, "An uncaught exception has been thrown in thread '" + thread + "'.", error));
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(Thread.currentThread().getName()));
        FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Hello FantasyWorld platform! ", FantasyWorldPlatform.CurrentVersion);
        for (final String arg: args) {
            switch (arg.hashCode()) {
                case -826834514 -> FantasyWorldPlatform.runOnClient = false; //"runServer"
                case -1278720458 -> FantasyWorldPlatform.runOnClient = true; //"runClient"
                case 455289810 -> FantasyWorldPlatform.showJWindow = false; //"hideWindow"
                case 887337293  -> FantasyWorldPlatform.showJWindow = true; //"showWindow"
                default -> FantasyWorldPlatform.logger.log(HLogLevel.WARN, "Unrecognized arg: ", arg);
            }
        }
    }
}
