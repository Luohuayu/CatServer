package org.spigotmc;

import net.minecraft.nbt.NbtAccounter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LimitStream extends FilterInputStream {

    private final NbtAccounter limit;

    public LimitStream(InputStream is, NbtAccounter limit) {
        super(is);
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        limit.accountBits(8);
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        limit.accountBits(b.length * 8);
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        limit.accountBits(len * 8);
        return super.read(b, off, len);
    }
}