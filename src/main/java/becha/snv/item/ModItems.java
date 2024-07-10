package becha.snv.item;

import java.util.ArrayList;
import java.util.List;

import becha.snv.StevesNewVocation;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

        public static final Item SHIMMER_CRYSTAL = registerItem("shimmer_crystal", new Item(new FabricItemSettings()));

        private static Item registerItem(String name, Item item) {
                return registerItem(name, item, new ArrayList<ItemGroup>());
        }

        private static Item registerItem(String name, Item item, List<ItemGroup> groups) {
                return Registry.register(Registries.ITEM, new Identifier(StevesNewVocation.MOD_ID, name), item);
        }

        public static void registerItems() {
                StevesNewVocation.LOGGER.info("Registering items for " + StevesNewVocation.MOD_NAME);
        }

}
