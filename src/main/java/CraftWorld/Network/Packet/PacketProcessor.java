package CraftWorld.Network.Packet;

import Core.Craftworld;
import CraftWorld.ConstantStorage;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Network.Packet.PacketProcessor.PacketDataOutputStream.ByteArrayLimiterOutputStream;
import HeadLibs.Annotations.ByteRange;
import HeadLibs.Annotations.IntRange;
import HeadLibs.Helper.HRandomHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class PacketProcessor {
    protected static final @NotNull PacketDataOutputStream packetDataOutputStream;
    static {
        try {
            packetDataOutputStream = new PacketDataOutputStream(ConstantStorage.NETWORK_BYTEBUFFER_SIZE);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected static boolean reopened = true;
    public static byte @Nullable [] getSendBytes() throws IOException {
        byte[] bytes = packetDataOutputStream.getByteArray();
        //noinspection MagicNumber
        if (ByteArrayLimiterOutputStream.getCount(bytes) == 25) {
            if (reopened)
                packetDataOutputStream.reopen();
            return null;
        }
        reopened = false;
        return bytes;
    }

    public static void sendInformationToServer(@Nullable IDSTBase data) throws IOException {
        if (data == null)
            return;
        data.write(packetDataOutputStream);
    }

    @SuppressWarnings({"MagicNumber", "NumericCastThatLosesPrecision"})
    public static byte[] getVersionCheckBytes() {
        int len = Craftworld.CURRENT_VERSION_STRING.getBytes().length;
        byte[] bytes = new byte[len + 19];
        //Unique flag
        bytes[0] = 0;
        bytes[1] = 0;
        bytes[2] = 0;
        bytes[3] = 0;
        bytes[4] = 0;
        bytes[5] = 0;
        bytes[6] = 0;
        bytes[7] = 0;
        //flag: version check
        bytes[8] = 0;
        bytes[9] = 0;
        bytes[10] = 0;
        bytes[11] = 0;
        bytes[12] = 0;
        bytes[13] = 0;
        bytes[14] = 0;
        bytes[15] = 0;
        //set version check
        bytes[16] = (byte) (len >>> 24);
        bytes[17] = (byte) (len >>> 16);
        bytes[18] = (byte) (len >>> 8);
        bytes[19] = (byte) (len);
        System.arraycopy(bytes, 20, Craftworld.CURRENT_VERSION_STRING.getBytes(), 0, len);
        return bytes;
    }

    public static class PacketDataOutputStream implements DataOutput {
        protected final @NotNull ByteArrayLimiterOutputStream byteArrayLimiter;
        protected final @NotNull DataOutputStream dataOutputStream;
        protected final int bufferSize;

        public PacketDataOutputStream(int bufferSize) throws IOException {
            super();
            this.byteArrayLimiter = new ByteArrayLimiterOutputStream(bufferSize);
            this.dataOutputStream = new DataOutputStream(new GZIPOutputStream(this.byteArrayLimiter));
            //TODO 信号良好时取消GZIPOutputStream装饰
            this.bufferSize = bufferSize;
        }

        public byte @NotNull [] getByteArray() throws IOException {
            this.dataOutputStream.flush();
            return Objects.requireNonNullElse(this.byteArrayLimiter.savedBytes.poll(), this.byteArrayLimiter.bytes);
        }

        public void reopen() {
            this.byteArrayLimiter.reopen();
        }

        @Override
        public void write(int b) throws IOException {
            this.dataOutputStream.write(b);
        }

        @Override
        public void write(byte @NotNull [] b) throws IOException {
            this.dataOutputStream.write(b);
        }

        @Override
        public void write(byte @NotNull [] b, int off, int len) throws IOException {
            this.dataOutputStream.write(b, off, len);
        }

        @Override
        public void writeBoolean(boolean v) throws IOException {
            this.dataOutputStream.writeBoolean(v);
        }

        @Override
        public void writeByte(int v) throws IOException {
            this.dataOutputStream.writeByte(v);
        }

        @Override
        public void writeShort(int v) throws IOException {
            this.dataOutputStream.writeShort(v);
        }

        @Override
        public void writeChar(int v) throws IOException {
            this.dataOutputStream.writeChar(v);
        }

        @Override
        public void writeInt(int v) throws IOException {
            this.dataOutputStream.writeInt(v);
        }

        @Override
        public void writeLong(long v) throws IOException {
            this.dataOutputStream.writeLong(v);
        }

        @Override
        public void writeFloat(float v) throws IOException {
            this.dataOutputStream.writeFloat(v);
        }

        @Override
        public void writeDouble(double v) throws IOException {
            this.dataOutputStream.writeDouble(v);
        }

        @Override
        public void writeBytes(@NotNull String s) throws IOException {
            this.dataOutputStream.writeBytes(s);
        }

        @Override
        public void writeChars(@NotNull String s) throws IOException {
            this.dataOutputStream.writeChars(s);
        }

        @Override
        public void writeUTF(@NotNull String s) throws IOException {
            this.dataOutputStream.writeUTF(s);
        }

        @SuppressWarnings({"NumericCastThatLosesPrecision", "MagicNumber", "PointlessBitwiseExpression", "NotNullFieldNotInitialized"})
        public static class ByteArrayLimiterOutputStream extends OutputStream {
            protected final @NotNull Queue<byte[]> savedBytes = new ArrayDeque<>();
            protected byte @NotNull [] bytes;
            protected int size;

            protected @NotNull UUID uuid;
            protected int index;

            public ByteArrayLimiterOutputStream(@IntRange(minimum = 1) int size) {
                super();
                this.size = size;
                this.reopen();
            }

            public void reopen() {
                this.uuid = HRandomHelper.getRandomUUID();
                while (this.uuid.getMostSignificantBits() == 0) //unique flag.
                    this.uuid = HRandomHelper.getRandomUUID();
                this.index = 0;
                this.savedBytes.clear();
                this.bytes = new byte[this.size];
                this.prepareHead();
            }

            @Override
            public void write(@ByteRange int b) throws IOException {
                int count = this.getCount();
                if (count >= this.size) {
                    this.savedBytes.add(this.bytes);
                    this.bytes = new byte[this.size];
                    this.prepareHead();
                }
                this.bytes[count] = (byte) b;
                this.setCount(count + 1);
            }

            protected void prepareHead() {
                long v = this.uuid.getMostSignificantBits();
                this.bytes[0] = (byte) (v >>> 56);
                this.bytes[1] = (byte) (v >>> 48);
                this.bytes[2] = (byte) (v >>> 40);
                this.bytes[3] = (byte) (v >>> 32);
                this.bytes[4] = (byte) (v >>> 24);
                this.bytes[5] = (byte) (v >>> 16);
                this.bytes[6] = (byte) (v >>> 8);
                this.bytes[7] = (byte) (v >>> 0);
                v = this.uuid.getLeastSignificantBits();
                this.bytes[8] = (byte) (v >>> 56);
                this.bytes[9] = (byte) (v >>> 48);
                this.bytes[10] = (byte) (v >>> 40);
                this.bytes[11] = (byte) (v >>> 32);
                this.bytes[12] = (byte) (v >>> 24);
                this.bytes[13] = (byte) (v >>> 16);
                this.bytes[14] = (byte) (v >>> 8);
                this.bytes[15] = (byte) (v >>> 0);
                v = this.index++;
                this.bytes[16] = (byte) (v >>> 24);
                this.bytes[17] = (byte) (v >>> 16);
                this.bytes[18] = (byte) (v >>> 8);
                this.bytes[19] = (byte) (v >>> 0);
                this.setCount(25);
            }

            public int getCount() {
                return getCount(this.bytes);
            }

            @Contract(pure = true)
            public static int getCount(byte @NotNull [] bytes) {
                return ((bytes[20] << 24) +
                        (bytes[21] << 16) +
                        (bytes[22] << 8) +
                        (bytes[23] << 0));
            }

            public void setCount(int count) {
                this.bytes[20] = (byte) (count >>> 24);
                this.bytes[21] = (byte) (count >>> 16);
                this.bytes[22] = (byte) (count >>> 8);
                this.bytes[23] = (byte) (count >>> 0);
            }
        }
    }
}
