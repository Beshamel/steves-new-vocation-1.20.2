package becha.snv.item;

import becha.snv.StevesNewVocation;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> SNV_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),
            Identifier.of(StevesNewVocation.MOD_ID, "snv"));

    public static ItemGroup SNV = FabricItemGroup.builder()
            .displayName(Text.literal(StevesNewVocation.MOD_NAME))
            .icon(() -> new ItemStack(ModItems.SHIMMER_CRYSTAL))
            .build();

    public static void registerItemGroups() {
        StevesNewVocation.LOGGER.info("Registering item groups for " + StevesNewVocation.MOD_NAME);

        Registry.register(Registries.ITEM_GROUP, SNV_KEY, SNV);

        ItemGroupEvents.modifyEntriesEvent(SNV_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.SHIMMER_CRYSTAL);
        });
    }

}
