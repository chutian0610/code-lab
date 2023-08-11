package info.victorchu.tool.jvm.charlene.clazz.deserializer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassFileReader {
    private final DataInputStream in;

    public ClassFileReader(InputStream in) {
        this.in = new DataInputStream(new BufferedInputStream(in));
    }

    public int skip(int length) throws IOException {
        return in.skipBytes(length);
    }

    public void readFully(byte[] b) throws IOException {
        in.readFully(b);
    }

    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    public int readUnsignedShort() throws IOException {
        return in.readUnsignedShort();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public long readLong() throws IOException {
        return in.readLong();
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public double readDouble() throws IOException {
        return in.readDouble();
    }

    public String readUTF() throws IOException {
        return in.readUTF();
    }
}
