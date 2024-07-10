package becha.snv.vocation;

import java.util.Map;

import com.google.common.collect.Maps;

import becha.snv.StevesNewVocation;
import becha.snv.networking.ModMessages;
import becha.snv.state.StateSaverAndLoader;
import becha.snv.util.Util;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Vocation {

    private static final String ICON_PATH_PREFIX = "textures/vocation/";

    private final boolean visible;

    private final String name;
    private final Identifier id;
    private final Map<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    private Identifier displayIcon;
    private Text displayName;

    public Vocation(String name) {
        this(name, 0, 0, false, Maps.newHashMap());
    }

    public Vocation(String name, int x, int y, boolean visible,
            Map<EntityAttribute, EntityAttributeModifier> attributeModifiers) {
        this.visible = visible;
        this.name = name;
        Identifier iconId = new Identifier(StevesNewVocation.MOD_ID, ICON_PATH_PREFIX + name + ".png");
        this.id = new Identifier(StevesNewVocation.MOD_ID, name);
        String translationKey = createTranslationKey(name);
        this.attributeModifiers = attributeModifiers;
        this.displayIcon = iconId;
        this.displayName = Text.translatable(translationKey);
    }

    public void onApply(PlayerEntity player) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : this.attributeModifiers.entrySet()) {
            Util.applyModifier(player, entry.getKey(), entry.getValue(), this.getName());
        }
    }

    public void onStart(PlayerEntity player, Vocation oldVocation) {
        if (this != Vocations.NONE)
            player.sendMessage(Text.translatable("msg.snv.vocation_start").append(displayName));

        onApply(player);
    }

    public void onStop(PlayerEntity player, Vocation newVocation) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : this.attributeModifiers.entrySet()) {
            Util.removeModifier(player, entry.getKey(), entry.getValue());
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean revealed(PlayerEntity player) {
        return isVisible();
    }

    public boolean unlocked(PlayerEntity player) {
        return revealed(player);
    }

    public String getName() {
        return name;
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getIcon() {
        return displayIcon;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public Map<EntityAttribute, EntityAttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    protected static String createTranslationKey(String name) {
        return Util.createTranslationKey("vocation", new Identifier(StevesNewVocation.MOD_ID, name));
    }

    public int getTier() {
        return 1;
    }

    public static void set(PlayerEntity player, Vocation vocation) {
        VocationData.set(player, vocation);
        vocation.onApply(player);
    }

    public static void setIfValid(PlayerEntity player, Vocation vocation) {
        if (vocation.unlocked(player))
            set(player, vocation);
    }

    public static void onChange(PlayerEntity player, Vocation lastVocation, Vocation newVocation) {
        if (lastVocation != newVocation) {
            lastVocation.onStop(player, newVocation);
            newVocation.onStart(player, lastVocation);
        }
    }

    public static Vocation get(PlayerEntity player) {
        return VocationData.get(player);
    }

    public static void reset(PlayerEntity player) {
        if (Vocation.get(player) == Vocations.NONE) {
            player.sendMessage(Text.translatable("msg.snv.reset_vocation_fail"));
        } else {
            Vocation.set(player, Vocations.NONE);
            player.sendMessage(Text.translatable("msg.snv.reset_vocation_success"));
        }
    }

    public static void sendInfo(PlayerEntity player, Vocation vocation) {

        if (vocation == Vocations.NONE)
            player.sendMessage(Text.translatable("msg.snv.no_vocation_yet"));
        else
            player.sendMessage(Text.translatable("msg.snv.vocation_info_name").append(" : ")
                    .append(vocation.getDisplayName()));
    }

    public class VocationData {

        public static String KEY = "vocation";

        public static void write(PlayerEntity player, Vocation vocation) {
            if (player == null)
                return;
            StateSaverAndLoader.getPlayerState(player).vocation = vocation;
        }

        public static Vocation read(PlayerEntity player) {
            if (player == null)
                return Vocations.NONE;
            Vocation vocation = StateSaverAndLoader.getPlayerState(player).vocation;
            return vocation == null ? Vocations.NONE : vocation;
        }

        public static void set(PlayerEntity player, Vocation vocation) {
            if (player instanceof ServerPlayerEntity) {
                Vocation lastVocation = read(player);
                write(player, vocation);
                onChange(player, lastVocation, vocation);
                sync((ServerPlayerEntity) player);
                return;
            }
            if (player instanceof ClientPlayerEntity) {
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeString(vocation.getName());
                ClientPlayNetworking.send(ModMessages.VOCATION_SET_C2S, buffer);
                return;
            }
            StevesNewVocation.LOGGER.warn(
                    "Could not set vocation for " + player.getName() + ", of type " + player.getClass().getName());
        }

        public static void seek() {
            ClientPlayNetworking.send(ModMessages.VOCATION_SEEK_C2S, PacketByteBufs.create());
        }

        public static Vocation get(PlayerEntity player) {
            return read(player);
        }

        public static void sync(ServerPlayerEntity player) {
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeString(read(player).getName());
            read(player).onApply(player);
            ServerPlayNetworking.send(player, ModMessages.VOCATION_SYNC_S2C, buffer);
        }
    }

    public static class VocationBuilder {

        String name;

        boolean visible = false;
        Map<EntityAttribute, EntityAttributeModifier> attributeModifiers = Maps.newHashMap();
        int x = 0;
        int y = 0;

        public Vocation build() {

            return new Vocation(name, x, y, visible,
                    attributeModifiers);
        }

        public VocationBuilder(String name) {
            this.name = name;
        }

        public VocationBuilder visible(boolean b) {
            this.visible = b;
            return this;
        }

        public VocationBuilder addAttributeModifier(EntityAttribute attribute, String name, double amount,
                EntityAttributeModifier.Operation operation) {
            EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(name, amount, operation);
            this.addAttributeModifier(attribute, entityAttributeModifier);
            return this;
        }

        public VocationBuilder displayAt(int x, int y) {
            visible = true;
            this.x = x;
            this.y = y;
            return this;
        }

        private void addAttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier) {
            this.attributeModifiers.put(attribute, modifier);
        }
    }
}