package world.soapboxrace.mp.server.netty.messages;

import io.netty.buffer.ByteBuf;
import world.soapboxrace.mp.server.netty.UdpMessage;

import java.nio.ByteBuffer;

public class ServerKeepAlive implements UdpMessage
{
    public int counter;
    public int time;
    public int helloTime;
    public short unknownCounter;

    @Override
    public void read(ByteBuf buf)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(ByteBuffer buffer)
    {
        byte[] counterBytes = ByteBuffer.allocate(2).putShort((short) counter).array();
        byte[] timeBytes = ByteBuffer.allocate(2).putShort((short) time).array();
        byte[] helloTimeBytes = ByteBuffer.allocate(2).putShort((short) helloTime).array();
        byte[] unknownCounterBytes = ByteBuffer.allocate(2).putShort(unknownCounter).array();

        buffer.put((byte) 0x00);
        buffer.put(counterBytes);
        buffer.put((byte) 0x02);
        buffer.put(timeBytes);
        buffer.put(helloTimeBytes);
        buffer.put(unknownCounterBytes);

        int x = Integer.reverse(0xFFFF);

        if ((int) unknownCounter != 0xFFFF)
        {
            if (unknownCounter <= 16)
            {
                x &= ~(1 << (31 - (unknownCounter - 1)));
            }
        }

        byte[] hsBytes = ByteBuffer.allocate(4).putInt(x).array();

        buffer.put(hsBytes[0]);
        buffer.put(hsBytes[1]);

        buffer.put((byte) 0xff); // packet end

        buffer.put(new byte[]{0x01, 0x01, 0x01, 0x01});
    }
}
