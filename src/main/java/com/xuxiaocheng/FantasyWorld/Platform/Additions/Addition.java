package com.xuxiaocheng.FantasyWorld.Platform.Additions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * <p>FantasyWorldPlatform will ensure that the constructor is called only once immediately after the class was loaded.
 * The inherited class must have a parameterless constructor.</p>
 * <p>Click {@link com.xuxiaocheng.FantasyWorld.Core.FantasyWorld} for example and details.</p>
 */
public interface Addition {
    /**
     * <p>This method will be called immediately after creating all {@code Addition} instances.
     * After that, the rest methods will be called to determine the loading order and post events.</p>
     * <p>You should register event buses here instead of in the constructor.
     * You can change Cornerstones or Modifications based on {@code Addition} list
     *   by calling {@link AdditionalLoader#getUnmodifiableModifications()} as well.</p>
     */
    void entrance();
    @NotNull VersionSingle getVersion();
    @NotNull VersionComplex getAcceptPlatformVersion();
    @NotNull @UnmodifiableView Map<String, VersionComplex> getCornerstones();
    @NotNull @UnmodifiableView Map<String, VersionComplex> getModifications();

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Additional {
        @NotNull String id();
    }
}
