package becha.snv;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import becha.snv.command.ModCommands;
import becha.snv.command.argument.ArgumentTypes;
import becha.snv.item.ModItemGroups;
import becha.snv.item.ModItems;
import becha.snv.networking.ModMessages;
import becha.snv.vocation.Vocations;

public class StevesNewVocation implements ModInitializer {

	public static final String MOD_NAME = "Steve's New Vocation";
	public static final String MOD_ID = "snv";
	public static final Logger LOGGER = LoggerFactory.getLogger("steves-new-vocation");

	@Override
	public void onInitialize() {

		ModItems.registerItems();
		ModItemGroups.registerItemGroups();
		Vocations.registerVocations();
		ModCommands.registerCommands();
		ModMessages.registerC2SPackets();
		ArgumentTypes.registerArgumentTypes();

		LOGGER.info("Hello Fabric world!");
	}
}