package org.mtr.mapping.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static org.mtr.mapping.registry.MainEventBus.packetIdentifier;

public class PacketObject implements CustomPacketPayload {
    final FriendlyByteBuf byteBuf;

    public PacketObject(FriendlyByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation(packetIdentifier, "packet");
    }
}
