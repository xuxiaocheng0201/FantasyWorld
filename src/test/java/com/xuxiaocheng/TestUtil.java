package com.xuxiaocheng;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

public final class TestUtil {
    private TestUtil() {
        super();
    }

    public static void assetsEquals(@Nullable final Object real, @Nullable final Object expected) {
        assert Objects.equals(real, expected) : real;
    }

    public static void assetsAt(@Nullable final Object real, @Nullable final Object expected) {
        assert real == expected : real;
    }

    @FunctionalInterface
    public interface ThrowableFunction {
        void run() throws Throwable;
    }

    public static void assertThrow(@NotNull final ThrowableFunction function, @NotNull final Class<? extends Throwable> exception) {
        try {
            function.run();
            assert false : "Not throw.";
        } catch (final AssertionError assertion) {
            throw assertion;
        } catch (final Throwable throwable) {
            assert throwable.getClass().equals(exception) : throwable;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(@NotNull final Class<?> clazz, @NotNull final String fieldName, @Nullable final Object invoker) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(invoker);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
