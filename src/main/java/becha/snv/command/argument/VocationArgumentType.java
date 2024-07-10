package becha.snv.command.argument;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import becha.snv.StevesNewVocation;
import becha.snv.vocation.Vocation;
import becha.snv.vocation.Vocations;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

public class VocationArgumentType implements ArgumentType<Vocation> {

    public static final DynamicCommandExceptionType INVALID_VOCATION = new DynamicCommandExceptionType(
            o -> Text.literal("Invalid vocation : " + o));

    @Override
    public Vocation parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead()
                && (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '-' || reader.peek() == '_')) {
            reader.skip();
        }
        String vocationString = reader.getString().substring(argBeginning, reader.getCursor());
        Vocation vocation = Vocations.fromName(vocationString);
        if (vocation == null) {
            reader.setCursor(argBeginning);
            throw INVALID_VOCATION.createWithContext(reader, "Invalid vocation id");
        } else {
            return vocation;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(validArguments(), builder);
    }

    public static VocationArgumentType vocation() {
        return new VocationArgumentType();
    }

    private static Stream<String> validArguments() {
        return Vocations.all().stream().map(v -> v.getName());
    }
}
