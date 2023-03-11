package com.xuxiaocheng.FantasyWorld.Platform;

import com.xuxiaocheng.FantasyWorld.Core.FantasyWorld;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Addition;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.AdditionsLoader;
import com.xuxiaocheng.FantasyWorld.Platform.Events.CoreShutdownEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Events.CoreStartEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionFormatException;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FantasyWorldPlatform {
    private FantasyWorldPlatform() {
        super();
    }

    public static final boolean DebugMode = !new File(FantasyWorldPlatform.class.getProtectionDomain().getCodeSource().getLocation().getPath()).isFile();

    private static final @NotNull HLog logger = HLog.createInstance("DefaultLogger",
            FantasyWorldPlatform.DebugMode ? Integer.MIN_VALUE : HLogLevel.DEBUG.getPriority() + 1,
            true, new LoggerOutputStream(true, !FantasyWorldPlatform.DebugMode));
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

    public static final @NotNull ExecutorService DefaultThreadPool = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() << 1, Runtime.getRuntime().availableProcessors() << 3,
            30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(128),
            new DefaultThreadFactory(FantasyWorldPlatform.class), ((r, executor) -> {
                FantasyWorldPlatform.logger.log(HLogLevel.ERROR, "The main Thread Pool is full. Runnable class: ", r.getClass());
                r.run();
            }));

    @SuppressWarnings("unchecked")
    public static void main(final @NotNull String @NotNull [] args) {
//        HLog.setDebugMode(false);
        Thread.currentThread().setName("FantasyWorldPlatform/main");
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> FantasyWorldPlatform.logger.log(HLogLevel.FAULT, "An uncaught exception has been thrown in thread '" + thread + "'.", error));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Welcome to play again!");
            FantasyWorldPlatform.logger.getPrinter().flush();
        }, "FantasyWorldPlatform/shutdown"));
        FantasyWorldPlatform.logger.log(HLogLevel.FINE, "Hello FantasyWorld platform! version: ", FantasyWorldPlatform.CurrentVersion.getVersion());
        FantasyWorldPlatform.logger.log(HLogLevel.DEBUG, "Working path: ", System.getProperty("user.dir"));
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
        // Fix when Run in idea.
        if (FantasyWorldPlatform.DebugMode) {
            try {
                final Field field = AdditionsLoader.class.getDeclaredField("Modifications");
                final Constructor<?> constructor = FantasyWorld.class.getDeclaredConstructor();
                field.setAccessible(true);
                constructor.setAccessible(true);
                final Object o = field.get(null);
                if (o instanceof Map<?, ?>) {
                    final Map<String, Addition> modifications = (Map<String, Addition>) o;
                    final Addition addition = (Addition) constructor.newInstance();
                    addition.entrance();
                    modifications.put("FantasyWorld", addition);
                }
            } catch (@SuppressWarnings("OverlyBroadCatchBlock") final Exception ignored) {
            }
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
        final Map<Future<?>, String> futures = new HashMap<>();
        for (final JarFile jarFile: additionFiles)
            futures.put(FantasyWorldPlatform.DefaultThreadPool.submit(() -> {
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
        AdditionsLoader.initializeAdditions();
        EventBusManager.getInstance(null).post(new CoreStartEvent());
        EventBusManager.getInstance(null).post(new CoreShutdownEvent());
        ((ThreadPoolExecutor) FantasyWorldPlatform.DefaultThreadPool).allowCoreThreadTimeOut(true);
        ((ThreadPoolExecutor) FantasyWorldPlatform.DefaultThreadPool).setKeepAliveTime(1L, TimeUnit.SECONDS);
    }
}
