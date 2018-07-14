package org.spigotmc;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.nbt.NBTSizeTracker;

public class LimitStream extends FilterInputStream
{

    private final NBTSizeTracker limit;

    public LimitStream(InputStream is, NBTSizeTracker limit)
    {
        super( is );
        this.limit = limit;
    }

    @Override
    public int read() throws IOException
    {
        limit.read( 8 );
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        limit.read( b.length * 8 );
        return super.read( b );
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        limit.read( len * 8 );
        return super.read( b, off, len );
    }
}
