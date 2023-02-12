package com.xuxiaocheng;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Objects;

public final class TestUtil {
    private TestUtil() {
        super();
    }

    @Test
    public void test() {
    }

    public static void assetsEquals(final @Nullable Object real, final @Nullable Object expected) {
        if (expected != null && expected.getClass().isArray()) {
            assert real != null : "real: null";
            assert real.getClass().isArray() : "Not array!";
            final int len = Array.getLength(real);
            assert Array.getLength(expected) == len : "real array len: " + len;
            for (int i = 0; i < len; ++i)
                assert Objects.equals(Array.get(real, i), Array.get(expected, i)) : "i: " + i + ", real: " + Array.get(real, i);
            return;
        }
        assert Objects.equals(real, expected) : "real: " + real + ", excepted: " + expected;
    }

    public static void assetsAt(final @Nullable Object real, final @Nullable Object expected) {
        assert real == expected : "real: " + real + ", excepted: " + expected;
    }

    @FunctionalInterface
    public interface ThrowableFunction {
        void run() throws Throwable;
    }

    public static void assertThrow(final @NotNull ThrowableFunction function, final @NotNull Class<? extends Throwable> exception) {
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
    public static <T, C> T getField(final @NotNull Class<C> clazz, final @NotNull String fieldName, final @Nullable C invoker) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(invoker);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
