package becha.snv.vocation;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.Maps;

import becha.snv.StevesNewVocation;
import becha.snv.vocation.Vocation.VocationBuilder;

import net.minecraft.util.Identifier;

public class Vocations {

    private static HashMap<Identifier, Vocation> entries = Maps.newHashMap();

    public static final Vocation NONE = register(new Vocation("none", 0, 0, true, Maps.newHashMap()));

    public static final Vocation ARMORER = register(new Vocation("armorer", 0, 0, true, Maps.newHashMap()));
    public static final Vocation BUTCHER = register(new Vocation("butcher", 0, 0, true, Maps.newHashMap()));
    public static final Vocation CARTOGRAPHER = register(new Vocation("cartographer", 0, 0, true, Maps.newHashMap()));
    public static final Vocation CLERIC = register(new Vocation("cleric", 0, 0, true, Maps.newHashMap()));
    public static final Vocation FARMER = register(new Vocation("farmer", 0, 0, true, Maps.newHashMap()));
    public static final Vocation FISHERMAN = register(new Vocation("fisherman", 0, 0, true, Maps.newHashMap()));
    public static final Vocation FLETCHER = register(new Vocation("fletcher", 0, 0, true, Maps.newHashMap()));
    public static final Vocation LEATHERWORKER = register(new Vocation("leatherworker", 0, 0, true, Maps.newHashMap()));
    public static final Vocation LIBRARIAN = register(new Vocation("librarian", 0, 0, true, Maps.newHashMap()));
    public static final Vocation MASON = register(new Vocation("mason", 0, 0, true, Maps.newHashMap()));
    public static final Vocation SHEPHERD = register(new Vocation("shepherd", 0, 0, true, Maps.newHashMap()));
    public static final Vocation TOOLSMITH = register(new Vocation("toolsmith", 0, 0, true, Maps.newHashMap()));
    public static final Vocation WEAPONSMITH = register(new Vocation("weaponsmith", 0, 0, true, Maps.newHashMap()));
    public static final Vocation NITWIT = register(new Vocation("nitwit", 0, 0, true, Maps.newHashMap()));

    private static Vocation register(Vocation vocation) {
        entries.put(vocation.getId(), vocation);
        return vocation;
    }

    public static void registerVocations() {
        StevesNewVocation.LOGGER.info("Loaded {} vocations", entries.size());
    }

    private static Vocation createAndRegisterVocation(String name, Vocation parent) {
        if (parent == null)
            return register(new Vocation(name));

        Vocation vocation = new VocationBuilder(name).build();
        return register(vocation);
    }

    public static Collection<Vocation> all() {
        return entries.values();
    }

    public static Vocation fromName(String name) {
        if (name == null) {
            StevesNewVocation.LOGGER.warn("null Vocation name querry in Vocations.fromName");
            return NONE;
        }
        Identifier id = new Identifier(StevesNewVocation.MOD_ID, name);
        if (entries.containsKey(id))
            return entries.get(id);
        else
            return NONE;
    }
}
