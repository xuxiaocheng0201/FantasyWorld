package com.xuxiaocheng.FantasyWorld.Platform;

import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LoggerOutputStream extends OutputStream {
    protected final boolean console;
    protected final @NotNull OutputStream ConsoleOutputStream = HLog.DefaultLogger.getWriter(); // System.out
    protected final boolean file;
    protected OutputStream FileOutputStream;

    public LoggerOutputStream(final boolean console, final boolean file) {
        super();
        this.console = console;
        this.file = file;
        if (this.file) {
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
                    this.FileOutputStream = new BufferedOutputStream(new FileOutputStream(path, true));
                } catch (final FileNotFoundException exception) {
                    throw new IOException("Failed to get log file.", exception);
                }
            } catch (final IOException exception) {
                HLog.DefaultLogger.log(HLogLevel.FAULT, "Fail to create log file.", exception);
            }
        }
    }

    @Override
    public void write(final int b) throws IOException {
        if (this.console)
            this.ConsoleOutputStream.write(b);
        if (this.FileOutputStream != null)
            this.FileOutputStream.write(b);
    }

    @Override
    public void write(final byte @NotNull [] b, final int off, final int len) throws IOException {
        if (this.console)
            this.ConsoleOutputStream.write(b, off, len);
        if (this.FileOutputStream != null)
            this.FileOutputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (this.console)
            this.ConsoleOutputStream.flush();
        if (this.FileOutputStream != null)
            this.FileOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        if (this.console)
            this.ConsoleOutputStream.close();
        if (this.FileOutputStream != null)
            this.FileOutputStream.close();
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final LoggerOutputStream that)) return false;
        return this.console == that.console && this.file == that.file && Objects.equals(this.FileOutputStream, that.FileOutputStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.console, this.file);
    }

    @Override
    public @NotNull String toString() {
        return "LoggerOutputStream{" +
                "console=" + this.console +
                ", file=" + this.file +
                '}';
    }
}
