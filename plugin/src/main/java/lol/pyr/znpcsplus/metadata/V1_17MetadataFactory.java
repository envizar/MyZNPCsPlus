package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import lol.pyr.znpcsplus.util.CatVariant;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.ParrotVariant;
import org.bukkit.DyeColor;

@Deprecated
public class V1_17MetadataFactory extends V1_16MetadataFactory {

    @Override
    public EntityData usingItem(boolean usingItem, boolean offHand, boolean riptide) {
        return newEntityData(8, EntityDataTypes.BYTE, (byte) ((usingItem ? 0x01 : 0) | (offHand ? 0x02 : 0) | (riptide ? 0x04 : 0)));
    }

    @Override
    public EntityData shoulderEntityLeft(ParrotVariant variant) {
        return createShoulderEntityLeft(19, variant);
    }

    @Override
    public EntityData shoulderEntityRight(ParrotVariant variant) {
        return createShoulderEntityRight(20, variant);
    }

    @Override
    public EntityData axolotlVariant(int variant) {
        return newEntityData(17, EntityDataTypes.INT, variant);
    }

    @Override
    public EntityData playingDead(boolean playingDead) {
        return newEntityData(18, EntityDataTypes.BOOLEAN, playingDead);
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (onFire ? 0x01 : 0));
    }

    @Override
    public EntityData catVariant(CatVariant variant) {
        return newEntityData(19, EntityDataTypes.CAT_VARIANT, variant.getId());
    }

    @Override
    public EntityData catLying(boolean lying) {
        return newEntityData(20, EntityDataTypes.BOOLEAN, lying);
    }

    @Override
    public EntityData catTamed(boolean tamed) {
        return newEntityData(17, EntityDataTypes.BYTE, (byte) (tamed ? 0x04 : 0));
    }

    @Override
    public EntityData catCollarColor(DyeColor collarColor) {
        return newEntityData(22, EntityDataTypes.INT, collarColor.ordinal());
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(16, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(17, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData endermanHeldBlock(int carriedBlock) {
        return newEntityData(16, EntityDataTypes.INT, carriedBlock);
    }

    @Override
    public EntityData endermanScreaming(boolean screaming) {
        return newEntityData(17, EntityDataTypes.BOOLEAN, screaming);
    }

    @Override
    public EntityData endermanStaring(boolean staring) {
        return newEntityData(18, EntityDataTypes.BOOLEAN, staring);
    }

    @Override
    public EntityData evokerSpell(int spell) {
        return newEntityData(17, EntityDataTypes.BYTE, (byte) spell);
    }

    @Override
    public EntityData hoglinImmuneToZombification(boolean immuneToZombification) {
        return newEntityData(17, EntityDataTypes.BOOLEAN, immuneToZombification);
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(18, EntityDataTypes.VILLAGER_DATA, new VillagerData(type, profession, level));
    }
}
