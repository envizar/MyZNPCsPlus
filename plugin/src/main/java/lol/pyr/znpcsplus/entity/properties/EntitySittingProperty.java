package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import lol.pyr.znpcsplus.entity.ArmorStandVehicleEntity;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

import java.util.Map;

public class EntitySittingProperty extends EntityPropertyImpl<Boolean> {
    private final PacketFactory packetFactory;
    private final EntityPropertyRegistryImpl propertyRegistry;

    public EntitySittingProperty(PacketFactory packetFactory, EntityPropertyRegistryImpl propertyRegistry) {
        super("entity_sitting", false, Boolean.class);
        this.packetFactory = packetFactory;
        this.propertyRegistry = propertyRegistry;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        boolean sitting = entity.getProperty(this);
        if (sitting) {
            ArmorStandVehicleEntity vehicleEntity = new ArmorStandVehicleEntity(propertyRegistry);
            PacketEntity vehiclePacketEntity = new PacketEntity(packetFactory, vehicleEntity, EntityTypes.ARMOR_STAND, entity.getLocation().withY(entity.getLocation().getY() - 0.9));
            vehiclePacketEntity.spawn(player);
            entity.setMetadata("ridingVehicle", vehiclePacketEntity);
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerSetPassengers(
                    vehiclePacketEntity.getEntityId(),
                    new int[]{entity.getEntityId()}
            ));
        } else {
            if (entity.hasMetadata("ridingVehicle")) {
                PacketEntity vehicleEntity = (PacketEntity) entity.getMetadata("ridingVehicle");
                PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerSetPassengers(
                        vehicleEntity.getEntityId(),
                        new int[]{}
                ));
                vehicleEntity.despawn(player);
                entity.removeMetadata("ridingVehicle");
                // Send a packet to reset the npc's position
                packetFactory.teleportEntity(player, entity);
            }
        }
    }

}
