package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.BufferedByteArrayOutputStream;
import com.xuxiaocheng.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BufferedByteArrayOutputStreamTest {
    @Test
    public void writeBytes() {
        final byte[] list = new byte[10000];
        for (int i = 0; i < 10000; ++i)
            list[i] = ((byte) i);
        final BufferedByteArrayOutputStream stream = new BufferedByteArrayOutputStream();
        stream.write(list, 0, list.length);
        TestUtil.assetsEquals(stream.toByteArray(10, 3), new byte[] {10, 11, 12});
        TestUtil.assetsEquals(stream.toByteArray(1000, 3), new byte[] {(byte) 1000, (byte) 1001, (byte) 1002});
    }

    @Test
    public void write() {
        final BufferedByteArrayOutputStream stream = new BufferedByteArrayOutputStream();
        stream.write(12);
        TestUtil.assetsEquals(stream.toByteArray(), new byte[] {12});
        final Collection<Byte> list = new ArrayList<>(List.of((byte) 12));
        for (int i = 1; i < 100000; ++i) {
            list.add((byte) i);
            stream.write(i);
        }
        TestUtil.assetsEquals(stream.toByteArray(), list.toArray());
        TestUtil.assetsEquals(stream.get(0), (byte) 12);
        TestUtil.assetsEquals(stream.get(2), (byte) 2);
        TestUtil.assetsEquals(stream.toByteArray(10, 3), new byte[] {10, 11, 12});
        TestUtil.assetsEquals(stream.toByteArray(10000, 3), new byte[] {(byte) 10000, (byte) 10001, (byte) 10002});
    }
}