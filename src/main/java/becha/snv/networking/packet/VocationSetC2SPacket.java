package becha.snv.networking.packet;

import becha.snv.vocation.Vocation;
import becha.snv.vocation.Vocations;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class VocationSetC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        Vocation vocation = Vocations.fromName(buf.readString());
        Vocation.setIfValid(player, vocation);
    }
}