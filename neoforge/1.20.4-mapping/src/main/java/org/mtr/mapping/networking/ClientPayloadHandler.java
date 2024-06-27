package org.mtr.mapping.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.PacketBufferReceiver;

import java.util.function.Consumer;
import java.util.function.Function;

public class ClientPayloadHandler {
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final PacketObject data, final PlayPayloadContext context) {
        final FriendlyByteBuf byteBuf = data.byteBuf;

        PacketBufferReceiver.receive(byteBuf, packetBufferReceiver -> {
            final Function<PacketBufferReceiver, ? extends PacketHandler> getPacketInstance = Registry.packets.get(packetBufferReceiver.readString());
            final PacketHandler packetHandler = getPacketInstance.apply(packetBufferReceiver);
            packetHandler.runClient();
        }, (Consumer<Runnable>) context.workHandler());
    }
}
