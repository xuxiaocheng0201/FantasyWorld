package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketInputStream;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;
import com.xuxiaocheng.HeadLibs.Helper.HRandomHelper;
import com.xuxiaocheng.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;
import java.util.function.Supplier;

public class PacketStreamTest {
    protected static final int extraLen = 100;

    @Test
    public void ioSerializable() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Object[] test = new Object[PacketStreamTest.extraLen];
        PacketStreamTest.checkIO(test, 0, () -> {
                    if (HRandomHelper.RANDOM.nextBoolean())
                        return new BigInteger(HRandomHelper.nextInt(0, PacketStreamTest.extraLen), (Random) HRandomHelper.RANDOM);
                    else
                        return BigDecimal.valueOf(HRandomHelper.RANDOM.nextDouble()).multiply(new BigDecimal(HRandomHelper.nextInt(0, PacketStreamTest.extraLen)).pow(HRandomHelper.nextInt(0, PacketStreamTest.extraLen))).sqrt(MathContext.DECIMAL128);
                },
                PacketOutputStream.class.getDeclaredMethod("writeSerializable", Serializable.class),
                PacketInputStream.class.getDeclaredMethod("readSerializable"));
    }

    protected static Short[] shortsTest = new Short[]{0, 1, 2, 127, -1, -128, Short.MIN_VALUE, Short.MIN_VALUE + 1, Short.MAX_VALUE, Short.MAX_VALUE - 1};
    protected static Integer[] intsTest = new Integer[]{0, 1, 2, 127, 65535, -1, -128, -65536, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, Integer.MAX_VALUE - 1};
    protected static Long[] longsTest = new Long[] {0L, 1L, 2L, 65535L, 2147483647L, -1L, -65536L, -2147483648L, Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MAX_VALUE, Long.MAX_VALUE - 1};
    protected static Float[] floatsTest = new Float[] {0.0F, 0.1F, 1.0F, 1.0e-5F, 2147483647.0F, -1.0F, -65536.0F, -2147483648.0F, Float.MIN_VALUE, Float.MIN_VALUE + 1, Float.MAX_VALUE, Float.MAX_VALUE - 1};
    protected static Double[] doublesTest = new Double[] {0.0, 0.1, 1.0, 1.0e-5, 2147483647.0, -1.0, -65536.0, -2147483648.0, Double.MIN_VALUE, Double.MIN_VALUE + 1, Double.MAX_VALUE, Double.MAX_VALUE - 1};
    protected static String[]stringsTest = new String[] {null, "", "Hello", "中文测试"};

    @Test
    public void ioString() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int strings = PacketStreamTest.stringsTest.length;
        final String[] stringsTest = new String[strings + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.stringsTest, 0, stringsTest, 0, strings);
        PacketStreamTest.checkIO(stringsTest, strings, () -> HRandomHelper.nextString(HRandomHelper.nextInt(0, PacketStreamTest.extraLen), 0, 128),
                PacketOutputStream.class.getDeclaredMethod("writeUTF", String.class),
                PacketInputStream.class.getDeclaredMethod("readUTF"));
    }

    @Test
    public void ioDouble() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int doubles = PacketStreamTest.doublesTest.length;
        final Double[] doublesTest = new Double[doubles + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.doublesTest, 0, doublesTest, 0, doubles);
        PacketStreamTest.checkIO(doublesTest, doubles, HRandomHelper.RANDOM::nextDouble,
                PacketOutputStream.class.getDeclaredMethod("writeDouble", double.class),
                PacketInputStream.class.getDeclaredMethod("readDouble"));
    }

    @Test
    public void ioFloat() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int floats = PacketStreamTest.floatsTest.length;
        final Float[] floatsTest = new Float[floats + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.floatsTest, 0, floatsTest, 0, floats);
        PacketStreamTest.checkIO(floatsTest, floats, HRandomHelper.RANDOM::nextFloat,
                PacketOutputStream.class.getDeclaredMethod("writeFloat", float.class),
                PacketInputStream.class.getDeclaredMethod("readFloat"));
    }

    @Test
    public void ioVariableLenLong() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int longs = PacketStreamTest.longsTest.length;
        final Long[] longsTest = new Long[longs + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.longsTest, 0, longsTest, 0, longs);
        PacketStreamTest.checkIO(longsTest, longs, HRandomHelper.RANDOM::nextLong,
                PacketOutputStream.class.getDeclaredMethod("writeVariableLenLong", long.class),
                PacketInputStream.class.getDeclaredMethod("readVariableLenLong"));
    }

    @Test
    public void ioLong() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int longs = PacketStreamTest.longsTest.length;
        final Long[] longsTest = new Long[longs + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.longsTest, 0, longsTest, 0, longs);
        PacketStreamTest.checkIO(longsTest, longs, HRandomHelper.RANDOM::nextLong,
                PacketOutputStream.class.getDeclaredMethod("writeLong", long.class),
                PacketInputStream.class.getDeclaredMethod("readLong"));
    }

    @Test
    public void ioVariableLenInt() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int ints = PacketStreamTest.intsTest.length;
        final Integer[] intsTest = new Integer[ints + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.intsTest, 0, intsTest, 0, ints);
        PacketStreamTest.checkIO(intsTest, ints, HRandomHelper.RANDOM::nextInt,
                PacketOutputStream.class.getDeclaredMethod("writeVariableLenInt", int.class),
                PacketInputStream.class.getDeclaredMethod("readVariableLenInt"));
    }

    @Test
    public void ioInt() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int ints = PacketStreamTest.intsTest.length;
        final Integer[] intsTest = new Integer[ints + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.intsTest, 0, intsTest, 0, ints);
        PacketStreamTest.checkIO(intsTest, ints, HRandomHelper.RANDOM::nextInt,
                PacketOutputStream.class.getDeclaredMethod("writeInt", int.class),
                PacketInputStream.class.getDeclaredMethod("readInt"));
    }

    @Test
    public void ioVariableLenShort() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int shorts = PacketStreamTest.shortsTest.length;
        final Short[] shortsTest = new Short[shorts + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.shortsTest, 0, shortsTest, 0, shorts);
        PacketStreamTest.checkIO(shortsTest, shorts, () -> (short) HRandomHelper.RANDOM.nextInt(),
                PacketOutputStream.class.getDeclaredMethod("writeVariableLenShort", short.class),
                PacketInputStream.class.getDeclaredMethod("readVariableLenShort"));
    }

    @Test
    public void ioShort() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int shorts = PacketStreamTest.shortsTest.length;
        final Short[] shortsTest = new Short[shorts + PacketStreamTest.extraLen];
        System.arraycopy(PacketStreamTest.shortsTest, 0, shortsTest, 0, shorts);
        PacketStreamTest.checkIO(shortsTest, shorts, () -> (short) HRandomHelper.RANDOM.nextInt(),
                PacketOutputStream.class.getDeclaredMethod("writeShort", short.class),
                PacketInputStream.class.getDeclaredMethod("readShort"));
    }

    protected static <T> void checkIO(final T @NotNull [] tests, final int off, final @NotNull Supplier<? extends T> randomer, @NotNull final Method writer, @NotNull final Method reader) throws IOException, InvocationTargetException, IllegalAccessException {
        for (int i = off; i < tests.length; ++i)
            tests[i] = randomer.get();
        final byte[] bytes;
        try (final PacketOutputStream outputStream = new PacketOutputStream()) {
            for (final T i: tests)
                writer.invoke(outputStream, i);
            bytes = outputStream.toBytes();
        }
        try (final PacketInputStream inputStream = new PacketInputStream(bytes)) {
            for (final T i: tests)
                TestUtil.assetsEquals(reader.invoke(inputStream), i);
        }
    }
}
