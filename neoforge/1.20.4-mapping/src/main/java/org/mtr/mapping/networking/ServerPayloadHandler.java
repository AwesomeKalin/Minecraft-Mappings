package org.mtr.mapping.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.PacketBufferReceiver;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final PacketObject data, final PlayPayloadContext context) {
        final FriendlyByteBuf byteBuf = data.byteBuf;

        PacketBufferReceiver.receive(byteBuf, packetBufferReceiver -> {
            final Function<PacketBufferReceiver, ? extends PacketHandler> getPacketInstance = Registry.packets.get(packetBufferReceiver.readString());
            final PacketHandler packetHandler = getPacketInstance.apply(packetBufferReceiver);
            final net.minecraft.server.MinecraftServer server = context.level().get().getServer();
            final ServerPlayerEntity serverPlayerEntity = new ServerPlayerEntity(Objects.requireNonNull(server.getPlayerList().getPlayer(context.player().get().getUUID())));
            packetHandler.runServer(new MinecraftServer(Objects.requireNonNull(server)), serverPlayerEntity);
        }, (Consumer<Runnable>) context.workHandler());
    }
}
