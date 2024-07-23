package net.doodcraft.cozmyc.bendingmobs.ability.air;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.PKMethods;
import net.doodcraft.cozmyc.bendingmobs.StaticMethods;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class AirBlast {

    private static final double knockBack = BendingMobs.plugin.getConfig().getDouble("Abilities.Air.AirBlast.Knockback");
    public static ConcurrentHashMap<Integer, AirBlast> instances = new ConcurrentHashMap<Integer, AirBlast>();
    private static int ID = Integer.MIN_VALUE;
    private final LivingEntity entity;
    private final Location origin;
    private final int id;
    private Location head;
    private Vector dir;

    public AirBlast(LivingEntity entity, Location target) {
        this.entity = entity;
        origin = entity.getEyeLocation();
        head = entity.getEyeLocation();
        dir = StaticMethods.getDirection(entity.getLocation(), target).normalize();
        id = ID;
        instances.put(id, this);
        if (ID == Integer.MAX_VALUE) {
            ID = Integer.MIN_VALUE;
        }
        ID++;
    }

    public static void progressAll() {
        for (int id : instances.keySet()) {
            if (!instances.get(id).progress()) {
                instances.get(id).remove();
            }
        }
    }

    private boolean progress() {
        if (entity == null || !StaticMethods.isFinite(head) || !StaticMethods.isFinite(dir)) {
            return false;
        }
        if (entity.getWorld() != head.getWorld()) {
            return false;
        }

        head.add(dir.multiply(1));

        if (origin.distance(head) > 28) {
            return false;
        }

        if (!StaticMethods.isTransparent(head.getBlock())) {
            return false;
        }

        if (PKMethods.collidesPKAbility(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

        if (PKMethods.isWithinPKShield(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

        StaticMethods.playAirbendingParticles(head, 5, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
        StaticMethods.playAirbendingSounds(head);

        for (Entity entity : StaticMethods.getEntitiesAroundPoint(head, 2)) {
            if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
                if (entity instanceof Creature) {
                    ((Creature) entity).setTarget(this.entity);
                }
                entity.setVelocity(dir.multiply(knockBack));
                ((LivingEntity) entity).damage(2.0, this.entity);
                entity.setLastDamageCause(new EntityDamageByEntityEvent(this.entity, entity, EntityDamageEvent.DamageCause.CUSTOM, 2.0));
                return false;
            }
        }
        return true;
    }

    private void remove() {
        instances.remove(id);
    }

    public static void removeAll() {
        for (int id : instances.keySet()) {
            instances.remove(id);
        }
    }
}
