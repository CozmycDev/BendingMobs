package net.doodcraft.cozmyc.bendingmobs.manager;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.DisguiseStrategy;
import net.doodcraft.cozmyc.bendingmobs.MobMethods;
import net.doodcraft.cozmyc.bendingmobs.ability.AvatarAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.air.AirAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.earth.EarthAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.fire.FireAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.water.WaterAbility;
import net.doodcraft.cozmyc.bendingmobs.object.BendingEntity;
import net.doodcraft.cozmyc.bendingmobs.object.Element;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager {

    private static final int chance = BendingMobs.plugin.getConfig().getInt("Properties.BendFrequency");
    public static final ConcurrentHashMap<UUID, BendingEntity> entityArray = new ConcurrentHashMap<>();

    public EntityManager() {

    }

    public static void addEntity(LivingEntity entity, LivingEntity target) {
        if (!(entity instanceof Creature)) {
            return;
        }
        if (MobMethods.isDisabledWorld(entity.getWorld())) {
            return;
        }

        if (entityArray.containsKey(entity.getUniqueId())) {
            entityArray.get(entity.getUniqueId()).setTarget(target);
            return;
        }

        entityArray.put(entity.getUniqueId(), new BendingEntity(entity, target));
        MobMethods.assignElement(entity);
    }

    public static void progress() {
        for (UUID uuid : entityArray.keySet()) {
            BendingEntity pkentity = entityArray.get(uuid);
            LivingEntity entity = pkentity.getEntity();
            LivingEntity target = pkentity.getTarget();
            Creature e = (Creature) entity;

            if (entity == null || entity.isDead()) {
                removeEntity(uuid);
                return;
            }

            if (!entity.getMetadata("element").isEmpty()) {
                if (MobMethods.rand.nextInt(chance) == 0) {
                    if (!entity.hasLineOfSight(target)) {
                        return;
                    }
                    switch (Objects.requireNonNull(Element.getType(entity.getMetadata("element").get(0).asInt()))) {
                        case Air:
                            AirAbility.execute(entity, target);
                            break;
                        case Earth:
                            EarthAbility.execute(entity, target);
                            break;
                        case Fire:
                            FireAbility.execute(entity, target);
                            break;
                        case Water:
                            WaterAbility.execute(entity, target);
                            break;
                        case Avatar:
                            AvatarAbility.execute(entity, target);
                            break;
                    }
                }
            }
        }
    }

    public static void removeEntity(UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity != null) {
            removeEntity((LivingEntity) entity);
        }
    }

    public static void removeEntity(LivingEntity entity) {
        entityArray.remove(entity.getUniqueId());

        if (BendingMobs.disguiseStrategy.equals(DisguiseStrategy.LIBSDISGUISES)) {
            PlayerDisguise disguise = (PlayerDisguise) DisguiseAPI.getDisguise(entity);
            if (disguise != null) {
                disguise.stopDisguise();
            }
        }

        // TODO exit for mythicmobs

        entity.remove();
    }

    public static void remove() {
        entityArray.clear();
    }
}
