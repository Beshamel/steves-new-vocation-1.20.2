package becha.snv.networking.packet;

import becha.snv.vocation.Vocation;
import becha.snv.vocation.Vocations;
import becha.snv.vocation.Vocation.VocationData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class VocationSyncS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        Vocation vocation = Vocations.fromName(buf.readString());
        VocationData.write(client.player, vocation);
        vocation.onApply(client.player);
    }
}
