package com.xuxiaocheng.FantasyWorld.Platform;

import com.xuxiaocheng.FantasyWorld.Platform.Additions.AdditionsLoader;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FantasyWorldPlatform {
    private FantasyWorldPlatform() {
        super();
    }

    public static final boolean DebugMode = HLog.isDebugMode();

    private static @NotNull HLog logger = HLog.DefaultLogger; static {
        if (HLog.isDebugMode())
            FantasyWorldPlatform.logger = HLog.createInstance("DefaultLogger",
                Integer.MIN_VALUE, false, HLog.DefaultLogger.getWriter());
        else {
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
                    FantasyWorldPlatform.logger = HLog.createInstance("DefaultLogger",
                            HLogLevel.FINE.getPriority(), false,
                            new BufferedOutputStream(new FileOutputStream(path, true)));
                } catch (final FileNotFoundException exception) {
                    throw new IOException("Failed to get log file.", exception);
                }
            } catch (final IOException exception) {
                HLog.DefaultLogger.log(HLogLevel.FAULT, "Fail to create log file.", exception);
            }
        }
    }
    public static @NotNull HLog getLogger() {
        return FantasyWorldPlatform.logger;
    }

    private static @NotNull VersionSingle CurrentVersion = VersionSingle.EmptyVersion; static {
        try {
            FantasyWorldPlatform.CurrentVersion = VersionSingle.create("0.1.0");
        } catch (final VersionFormatException ignore) {
        }
    }
    private static boolean runOnClient = false;
    private static boolean showJWindow = false;
    private static final @NotNull Collection<@NotNull File> AdditionSearchPaths = new HashSet<>(); static {
        FantasyWorldPlatform.AdditionSearchPaths.add(new File("."));
        FantasyWorldPlatform.AdditionSearchPaths.add(new File("FantasyWorld\\mods"));
        FantasyWorldPlatform.AdditionSearchPaths.add(new File("FantasyWorld\\0.1.0\\additions"));
    }
    public static @NotNull VersionSingle getCurrentVersion() {
        return FantasyWorldPlatform.CurrentVersion;
    }
    public static boolean isRunOnClient() {
        return FantasyWorldPlatform.runOnClient;
    }
    public static boolean isShowJWindow() {
        return FantasyWorldPlatform.showJWindow;
    }

    public static void main(@NotNull final String @NotNull [] args) {
        HLog.setDebugMode(false);
        Thread.currentThread().setName("FantasyWorldPlatform/main");
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> FantasyWorldPlatform.logger.log(HLogLevel.FAULT, "An uncaught exception has been thrown in thread '" + thread + "'.", error));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Welcome to play again!");
        }, "FantasyWorldPlatform/shutdown"));
        FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Hello FantasyWorld platform! version: ", FantasyWorldPlatform.CurrentVersion.getVersion());
        for (final String arg: args) {
            switch (arg.hashCode()) {
                case -826834514 -> FantasyWorldPlatform.runOnClient = false; //"runServer"
                case -1278720458 -> FantasyWorldPlatform.runOnClient = true; //"runClient"
                case 455289810 -> FantasyWorldPlatform.showJWindow = false; //"hideWindow"
                case 887337293  -> FantasyWorldPlatform.showJWindow = true; //"showWindow"
                default -> FantasyWorldPlatform.logger.log(HLogLevel.WARN, "Unrecognized arg: ", arg);
            }
        }
        FantasyWorldPlatform.logger.log(HLogLevel.DEBUG, "runOnClient: ", FantasyWorldPlatform.runOnClient, ", showJWindow: ", FantasyWorldPlatform.showJWindow);
        if (FantasyWorldPlatform.showJWindow) {
            FantasyWorldPlatform.logger.log(HLogLevel.VERBOSE, "Showing window.");
            // TODO: Show window
        }
        final Collection<JarFile> additionFiles = new HashSet<>();
        FantasyWorldPlatform.logger.log(HLogLevel.INFO, "Finding additions in: ", FantasyWorldPlatform.AdditionSearchPaths);
        for (final File path: FantasyWorldPlatform.AdditionSearchPaths) {
            if (!path.isDirectory())
                continue;
            FantasyWorldPlatform.logger.log(HLogLevel.VERBOSE, "Find in path: ", path.getAbsolutePath());
            final String[] jarFiles = path.list((dir, name) -> name.endsWith(".jar"));
            if (jarFiles == null)
                continue;
            additionFiles.addAll(Stream.of(jarFiles).map((s) -> {
                FantasyWorldPlatform.logger.log(HLogLevel.VERBOSE, "Found addition file: ", s);
                try {
                    return new JarFile(new File(path, s));
                } catch (final IOException exception) {
                    FantasyWorldPlatform.logger.log(HLogLevel.ERROR, "Failed to record addition file: ", s, exception);
                }
                //noinspection ReturnOfNull
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Found addition files count: ", additionFiles.size());
        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final Map<Future<?>, String> futures = new HashMap<>();
        for (final JarFile jarFile: additionFiles)
            futures.put(executor.submit(() -> {
                final String name = Thread.currentThread().getName();
                Thread.currentThread().setName("FantasyWorldPlatform/AdditionLoader" + name.substring(name.lastIndexOf('-')));
                FantasyWorldPlatform.logger.log(HLogLevel.VERBOSE, "Loading addition. file: ", jarFile.getName());
                AdditionsLoader.addAddition(jarFile);
                FantasyWorldPlatform.logger.log(HLogLevel.VERBOSE, "Loaded addition. file: ", jarFile.getName());
            }), jarFile.getName());
        for (final Map.Entry<Future<?>, String> entry: futures.entrySet())
            try {
                entry.getKey().get();
            } catch (final InterruptedException | ExecutionException exception) {
                FantasyWorldPlatform.logger.log(HLogLevel.ERROR, "Failed to load addition. file: ", entry.getValue(), exception);
            }
        futures.clear();
//        EventBusManager.get("global");
        AdditionsLoader.initializeAdditions();
        // TODO: Post Server/Client start events

    }
}
