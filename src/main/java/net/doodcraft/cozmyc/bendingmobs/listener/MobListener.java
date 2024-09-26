package net.doodcraft.cozmyc.bendingmobs.listener;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.MobMethods;
import net.doodcraft.cozmyc.bendingmobs.manager.EntityManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Objects;

public class MobListener implements Listener {

    public static final boolean airFallDamage = BendingMobs.plugin.getConfig().getBoolean("Properties.Air.NoFallDamage");
    public static final boolean villagerFightBack = BendingMobs.plugin.getConfig().getBoolean("Properties.Entity.Villager.FightBack");
    public static final boolean preventMobCombustion = BendingMobs.plugin.getConfig().getBoolean("Properties.PreventMobCombustion");
    public static final boolean denyOtherSpawns = BendingMobs.plugin.getConfig().getBoolean("Properties.DenyOtherMobSpawns");

    final BendingMobs plugin;

    public MobListener(BendingMobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFireAttack(EntityTargetEvent event) {
        if (event.getTarget() == null) {
            return;
        }
        if (event.getEntity() instanceof LivingEntity && event.getTarget() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();  // Manual cast
            LivingEntity target = (LivingEntity) event.getTarget();  // Manual cast

            if (MobMethods.canEntityBend(entity.getType().name())) {
                EntityManager.addEntity(entity, target);
                if (target.getType().equals(EntityType.VILLAGER) && villagerFightBack) {
                    EntityManager.addEntity(target, entity);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobCombust(EntityCombustEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (!(MobMethods.hasElement((LivingEntity) event.getEntity()))) return;
        if (preventMobCombustion) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkLoadEvent event) {
        Entity[] entities = event.getChunk().getEntities();
        for (Entity entity : entities) {
            if (MobMethods.entityTypes.contains(entity.getType().name())) {
                MobMethods.assignElement(entity);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if (MobMethods.isDisabledWorld(entity.getWorld())) return;
        if (denyOtherSpawns && !MobMethods.canEntityBend(entity.getType().name())) {
            event.setCancelled(true);
            return;
        }
        if (MobMethods.canEntityBend(entity.getType().name())) MobMethods.assignElement(entity);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();  // Manual cast to LivingEntity

            if (MobMethods.canEntityBend(entity.getType().name())) {
                if ((MobMethods.getElement(entity) != null && Objects.requireNonNull(MobMethods.getElement(entity)).isAirbender())
                        || MobMethods.isAvatar(entity)) {

                    if (airFallDamage && event.getCause() == DamageCause.FALL) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
