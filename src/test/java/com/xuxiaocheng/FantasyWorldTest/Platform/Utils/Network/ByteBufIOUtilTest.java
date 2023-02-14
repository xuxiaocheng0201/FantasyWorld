package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Network.PacketManager;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.ByteBufIOUtil;
import com.xuxiaocheng.HeadLibs.Helper.HRandomHelper;
import com.xuxiaocheng.TestUtil;
import io.netty.buffer.ByteBuf;
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

public class ByteBufIOUtilTest {
    protected static final int extraLen = 100;

    protected static final Short[] shortsTest = new Short[]{0, 1, 2, 127, -1, -128, Short.MIN_VALUE, Short.MIN_VALUE + 1, Short.MAX_VALUE, Short.MAX_VALUE - 1};
    protected static final Integer[] intsTest = new Integer[]{0, 1, 2, 127, 65535, -1, -128, -65536, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, Integer.MAX_VALUE - 1};
    protected static final Long[] longsTest = new Long[] {0L, 1L, 2L, 65535L, 2147483647L, -1L, -65536L, -2147483648L, Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MAX_VALUE, Long.MAX_VALUE - 1};
    protected static final String[] stringsTest = new String[] {/*null, *(Unsupported)*/"", "Hello", "中文测试"};

    @Test
    public void ioSerializable() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Object[] test = new Object[ByteBufIOUtilTest.extraLen];
        ByteBufIOUtilTest.checkIO(test, 0, () -> {
                    if (HRandomHelper.RANDOM.nextBoolean())
                        return new BigInteger(HRandomHelper.nextInt(0, ByteBufIOUtilTest.extraLen), (Random) HRandomHelper.RANDOM);
                    else
                        return BigDecimal.valueOf(HRandomHelper.RANDOM.nextDouble()).multiply(new BigDecimal(HRandomHelper.nextInt(0, ByteBufIOUtilTest.extraLen)).pow(HRandomHelper.nextInt(0, ByteBufIOUtilTest.extraLen))).sqrt(MathContext.DECIMAL128);
                },
                ByteBufIOUtil.class.getDeclaredMethod("writeSerializable", ByteBuf.class, Serializable.class),
                ByteBufIOUtil.class.getDeclaredMethod("readSerializable", ByteBuf.class));
    }

    @Test
    public void ioString() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int strings = ByteBufIOUtilTest.stringsTest.length;
        final String[] stringsTest = new String[strings + ByteBufIOUtilTest.extraLen];
        System.arraycopy(ByteBufIOUtilTest.stringsTest, 0, stringsTest, 0, strings);
        ByteBufIOUtilTest.checkIO(stringsTest, strings, () -> HRandomHelper.nextString(HRandomHelper.nextInt(0, ByteBufIOUtilTest.extraLen), 0, 128),
                ByteBufIOUtil.class.getDeclaredMethod("writeUTF", ByteBuf.class, String.class),
                ByteBufIOUtil.class.getDeclaredMethod("readUTF", ByteBuf.class));
    }
    
    @Test
    public void ioVariableLenLong() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int longs = ByteBufIOUtilTest.longsTest.length;
        final Long[] longsTest = new Long[longs + ByteBufIOUtilTest.extraLen];
        System.arraycopy(ByteBufIOUtilTest.longsTest, 0, longsTest, 0, longs);
        ByteBufIOUtilTest.checkIO(longsTest, longs, HRandomHelper.RANDOM::nextLong,
                ByteBufIOUtil.class.getDeclaredMethod("writeVariableLenLong", ByteBuf.class, long.class),
                ByteBufIOUtil.class.getDeclaredMethod("readVariableLenLong", ByteBuf.class));
    }

    @Test
    public void ioVariableLenInt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int ints = ByteBufIOUtilTest.intsTest.length;
        final Integer[] intsTest = new Integer[ints + ByteBufIOUtilTest.extraLen];
        System.arraycopy(ByteBufIOUtilTest.intsTest, 0, intsTest, 0, ints);
        ByteBufIOUtilTest.checkIO(intsTest, ints, HRandomHelper.RANDOM::nextInt,
                ByteBufIOUtil.class.getDeclaredMethod("writeVariableLenInt", ByteBuf.class, int.class),
                ByteBufIOUtil.class.getDeclaredMethod("readVariableLenInt", ByteBuf.class));
    }

    @Test
    public void ioVariableLenShort() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int shorts = ByteBufIOUtilTest.shortsTest.length;
        final Short[] shortsTest = new Short[shorts + ByteBufIOUtilTest.extraLen];
        System.arraycopy(ByteBufIOUtilTest.shortsTest, 0, shortsTest, 0, shorts);
        ByteBufIOUtilTest.checkIO(shortsTest, shorts, () -> (short) HRandomHelper.RANDOM.nextInt(),
                ByteBufIOUtil.class.getDeclaredMethod("writeVariableLenShort", ByteBuf.class, short.class),
                ByteBufIOUtil.class.getDeclaredMethod("readVariableLenShort", ByteBuf.class));
    }


    protected static <T> void checkIO(final T @NotNull [] tests, final int off, final @NotNull Supplier<? extends T> randomer,
                                      final @NotNull Method writer, final @NotNull Method reader) throws InvocationTargetException, IllegalAccessException {
        for (int i = off; i < tests.length; ++i)
            tests[i] = randomer.get();
        final ByteBuf buffer = PacketManager.allocateWriteBuffer();
        for (final T i: tests)
            writer.invoke(null, buffer, i);
        for (final T i: tests)
            TestUtil.assetsEquals(reader.invoke(null, buffer), i);
    }
}
