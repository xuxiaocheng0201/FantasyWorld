package com.xuxiaocheng.FantasyWorld.Platform.Additions;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.AberrantAdditionConstructorException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.AdditionVersionNotSupportException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.ConflictingAdditionIdException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.IllegalAdditionException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.MismatchedAdditionAnnouncementException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.MissingAdditionException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.PlatformVersionNotSupportException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.SortAdditionsException;
import com.xuxiaocheng.FantasyWorld.Platform.Events.AdditionInitializationEvent;
import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.DirectedGraph;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.JarClassLoader;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AdditionsLoader {
    private AdditionsLoader() {
        super();
    }

    private static final @NotNull HLog logger = HLog.getInstance("DefaultLogger");
    private static final @NotNull Map<String, Addition> Modifications = new ConcurrentHashMap<>();
    private static final @NotNull Map<String, Addition> preInstallModifications = new HashMap<>();

    private static final @NotNull Map<String, Addition> unmodifiableModifications = Collections.unmodifiableMap(AdditionsLoader.Modifications);
    @Contract(pure = true)
    public static @NotNull @UnmodifiableView Map<String, Addition> getUnmodifiableModifications() {
        return AdditionsLoader.unmodifiableModifications;
    }

    public static boolean deleteAddition(@Nullable final String id) {
        if (id == null)
            return false;
        AdditionsLoader.logger.log(HLogLevel.WARN, "Delete addition: ", id, new Throwable());
        return AdditionsLoader.Modifications.remove(id) != null;
    }

    public static final @NotNull EventBus AdditionsLoaderExceptionEventBus = EventBusManager.get("AdditionsLoader/Exceptions");
    static {
        AdditionsLoader.AdditionsLoaderExceptionEventBus.register(new Object() {
            @Subscribe
            public void handle(@NotNull final IllegalAdditionException exception) {
                AdditionsLoader.logger.log(exception.getLevel(), exception);
            }
        });
    }

    public static void addAddition(@Nullable final JarFile jarFile) {
        if (jarFile == null) {
            AdditionsLoader.logger.log(HLogLevel.MISTAKE, "Add additions in a null jar file.");
            return;
        }
        final Map<String, Addition> additions = new HashMap<>();
        // Find additions class and get instances from jar file.
        {
            AdditionsLoader.logger.log(HLogLevel.INFO, "Finding additions in ", jarFile.getName());
            final JarClassLoader jarClassLoader = new JarClassLoader(jarFile);
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();
                if (!name.endsWith(".class"))
                    continue;
                final Class<?> c;
                try {
                    c = jarClassLoader.loadClass(name.substring(0, name.length() - 6).replace('/', '.'));
                } catch (final ClassNotFoundException ignore) {
                    continue;
                }
                if (Modifier.isAbstract(c.getModifiers()))
                    continue;
                final Addition.Additional additional = c.getAnnotation(Addition.Additional.class);
                if (additional == null ^ !Addition.class.isAssignableFrom(c)) {
                    AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new MismatchedAdditionAnnouncementException(null, c));
                    continue;
                }
                if (additional == null)
                    continue;
                final Addition addition;
                try {
                    final Constructor<?> constructor = c.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    addition = (Addition) constructor.newInstance();
                } catch (final NoSuchMethodException exception) {
                    AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new AberrantAdditionConstructorException("Mismatched the parameterless constructor.", c, exception));
                    continue;
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                    AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new AberrantAdditionConstructorException("Addition constructor throws exception.", c, exception));
                    continue;
                }
                final String id = additional.id();
                if (additions.containsKey(id)) {
                    final Addition existed = additions.get(id);
                    AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new ConflictingAdditionIdException(null, id, existed.getClass(), c));
                    if (VersionSingle.compareVersion(existed.getVersion(), addition.getVersion()) < 0)
                        additions.put(id, addition);
                    continue;
                }
                additions.put(id, addition);
            }
            AdditionsLoader.logger.log(HLogLevel.FINE, "Found additions count: ", additions.size());
        }
        // Invoke additions.
        {
            final Iterator<Map.Entry<String, Addition>> it = additions.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry<String, Addition> entry = it.next();
                final String id = entry.getKey();
                AdditionsLoader.logger.log(HLogLevel.VERBOSE, "Invoking addition. id: ", id);
                try {
                    entry.getValue().entrance();
                    AdditionsLoader.logger.log(HLogLevel.VERBOSE, "Invoked addition. id: ", id);
                } catch (final Exception exception) {
                    it.remove();
                    AdditionsLoader.logger.log(HLogLevel.ERROR, "Fail to invoke addition. id: ", id);
                    AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new AberrantAdditionConstructorException("The entrance method throws exception. id: " + id, exception));
                }
            }
        }
        // Check requirements.
        {
            final Iterator<Map.Entry<String, Addition>> it = additions.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry<String, Addition> entry = it.next();
                final Addition addition = entry.getValue();
                if (!VersionComplex.versionInComplex(FantasyWorldPlatform.getCurrentVersion(), addition.getAcceptPlatformVersion())) {
                    it.remove();
                    AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new PlatformVersionNotSupportException(null, entry.getKey(), addition.getAcceptPlatformVersion()));
                    continue;
                }
                for (final Map.Entry<String, VersionComplex> requirement: Sets.union(addition.getCornerstones().entrySet(), addition.getModifications().entrySet())) {
                    Addition dependency = additions.get(requirement.getKey());
                    if (dependency == null)
                        dependency = AdditionsLoader.Modifications.get(requirement.getKey());
                    if (dependency == null) {
                        it.remove();
                        AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new MissingAdditionException(null, entry.getKey(), requirement.getKey(), requirement.getValue()));
                        break;
                    }
                    if (!VersionComplex.versionInComplex(dependency.getVersion(), requirement.getValue())) {
                        it.remove();
                        AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new AdditionVersionNotSupportException(null, entry.getKey(), requirement.getKey(), requirement.getValue(), dependency.getVersion()));
                        break;
                    }
                }
            }
        }
        AdditionsLoader.Modifications.putAll(additions);
        synchronized (AdditionsLoader.preInstallModifications) {
            AdditionsLoader.preInstallModifications.putAll(additions);
        }
    }

    private static final @NotNull DirectedGraph<String> directedGraph = new DirectedGraph<>();
    public static void initializeAdditions() {
        if (AdditionsLoader.preInstallModifications.isEmpty())
            return;
        AdditionsLoader.logger.log(HLogLevel.INFO, "Sorting additions.");
        final Map<String, Addition> processingMap;
        synchronized (AdditionsLoader.preInstallModifications) {
            processingMap = new HashMap<>(AdditionsLoader.preInstallModifications.size());
            processingMap.putAll(AdditionsLoader.preInstallModifications);
            AdditionsLoader.preInstallModifications.clear();
        }
        final List<String> sortedList;
        final DirectedGraph<String> graph = new DirectedGraph<>(AdditionsLoader.directedGraph);
        for (final Map.Entry<String, Addition> entry: processingMap.entrySet()) {
            final String id = entry.getKey();
            graph.addNode(id);
            for (final String cornerstone: entry.getValue().getCornerstones().keySet())
                graph.addEdge(cornerstone, id);
            for (final String modification: entry.getValue().getModifications().keySet())
                graph.addEdge(id, modification);
        }
        sortedList = graph.sort();
        if (sortedList.isEmpty()) {
            AdditionsLoader.AdditionsLoaderExceptionEventBus.post(new SortAdditionsException(null, AdditionsLoader.directedGraph));
            return;
        }
        AdditionsLoader.logger.log(HLogLevel.FINE, "Sorted additions list: ", sortedList);
        // synchronize with global
        synchronized (AdditionsLoader.directedGraph) {
            for (final Map.Entry<String, Addition> entry: processingMap.entrySet()) {
                final String id = entry.getKey();
                AdditionsLoader.directedGraph.addNode(id);
                for (final String cornerstone : entry.getValue().getCornerstones().keySet())
                    AdditionsLoader.directedGraph.addEdge(cornerstone, id);
                for (final String modification : entry.getValue().getModifications().keySet())
                    AdditionsLoader.directedGraph.addEdge(id, modification);
            }
        }
        // Post events.
        final EventBus pre = EventBusManager.get("AdditionInitialization-0");
        final EventBus post = EventBusManager.get("AdditionInitialization-1");
        for (final String id: sortedList) {
            AdditionsLoader.logger.log(HLogLevel.VERBOSE, "Initializing addition: ", id);
            final AdditionInitializationEvent event = new AdditionInitializationEvent(id);
            pre.post(event);
            final EventBus eventBus = EventBusManager.getExist(id);
            if (eventBus == null) {
                post.post(event);
                continue;
            }
            eventBus.post(event);
            post.post(event);
        }
    }
}
