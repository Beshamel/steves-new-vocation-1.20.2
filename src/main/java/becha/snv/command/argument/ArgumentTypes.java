package becha.snv.command.argument;

import becha.snv.StevesNewVocation;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class ArgumentTypes {

    public static void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(new Identifier(StevesNewVocation.MOD_ID, "vocation"),
                VocationArgumentType.class, ConstantArgumentSerializer.of(VocationArgumentType::vocation));
    }

}
