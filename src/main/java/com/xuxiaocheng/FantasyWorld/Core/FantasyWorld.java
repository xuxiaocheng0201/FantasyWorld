package com.xuxiaocheng.FantasyWorld.Core;

import com.google.common.collect.ImmutableMap;
import com.xuxiaocheng.EventBus.Subscribe;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Addition;
import com.xuxiaocheng.FantasyWorld.Platform.Events.CoreShutdownEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Events.CoreStartEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionFormatException;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.concurrent.TimeUnit;

@Addition.Additional(id = "FantasyWorld")
public class FantasyWorld implements Addition {
    protected final @NotNull VersionSingle version;
    protected final @NotNull VersionComplex acceptPlatformVersion;
    protected final @NotNull ImmutableMap<String, VersionComplex> cornerstones;
    protected final @NotNull ImmutableMap<String, VersionComplex> modifications;

    protected FantasyWorld() throws VersionFormatException {
        super();
        this.version = VersionSingle.create("0.1.0");
        this.acceptPlatformVersion = VersionComplex.create("[0.1.0,)");
        this.cornerstones = ImmutableMap.of();
        this.modifications = ImmutableMap.of();
    }

    @Override
    public void entrance() {
        //TODO: Register event buses.
        EventBusManager.getInstance(null).register(this);
        Addition.getEventbusById("FantasyWorld").register(this);
    }

    @Override
    public @NotNull VersionSingle getVersion() {
        return this.version;
    }

    @Override
    public @NotNull VersionComplex getAcceptPlatformVersion() {
        return this.acceptPlatformVersion;
    }

    @Override
    public @NotNull @UnmodifiableView ImmutableMap<String, VersionComplex> getCornerstones() {
        return this.cornerstones;
    }

    @Override
    public @NotNull @UnmodifiableView ImmutableMap<String, VersionComplex> getModifications() {
        return this.modifications;
    }

    @Override
    public @NotNull String toString() {
        return "FantasyWorld(" + this.version.getVersion() + ')';
    }

    @Subscribe
    public void onStartEvent(final @NotNull CoreStartEvent event) {
        HLog.DefaultLogger.log("", "null");
    }

    @Subscribe
    public void onShutdownEvent(final @NotNull CoreShutdownEvent event) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        HLog.DefaultLogger.log("", "CoreShutdownEvent");
    }
}
