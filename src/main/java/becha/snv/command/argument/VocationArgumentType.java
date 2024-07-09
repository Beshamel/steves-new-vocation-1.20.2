package becha.snv.command.argument;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import becha.snv.vocation.Vocation;
import becha.snv.vocation.Vocations;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class VocationArgumentType implements ArgumentType<Vocation> {

    @Override
    public Vocation parse(StringReader reader) throws CommandSyntaxException {
        return Vocations.fromName(reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof ServerCommandSource) {
            if (((ServerCommandSource) context.getSource()).isExecutedByPlayer()) {
                ServerPlayerEntity player = ((ServerCommandSource) context.getSource()).getPlayer();
                return CommandSource.suggestMatching(validArguments(player), builder);
            }
        }
        if (context.getSource() instanceof ClientCommandSource) {
            FabricClientCommandSource source = (FabricClientCommandSource) context.getSource();
            MinecraftClient client = source.getClient();
            return CommandSource.suggestMatching(validArguments(client.player), builder);
        }
        if (context.getSource() instanceof CommandSource) {
            return CommandSource.suggestMatching(Vocations.all().stream().map(vocation -> vocation.getName()), builder);
        }

        return Suggestions.empty();
    }

    public static VocationArgumentType vocation() {
        return new VocationArgumentType();
    }

    private static Stream<String> validArguments(PlayerEntity player) {
        return Vocations.all().stream().map(v -> v.getName());
    }
}
