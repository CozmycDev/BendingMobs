package net.doodcraft.cozmyc.bendingmobs.ability.fire;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.PKMethods;
import net.doodcraft.cozmyc.bendingmobs.StaticMethods;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class FireBlast {

    private static final double damage = BendingMobs.plugin.getConfig().getDouble("Abilities.Fire.FireBlast.Damage");
    private static final long fireTick = BendingMobs.plugin.getConfig().getLong("Abilities.Fire.FireBlast.FireTick");
    public static ConcurrentHashMap<Integer, FireBlast> instances = new ConcurrentHashMap<Integer, FireBlast>();
    private static int ID = Integer.MIN_VALUE;
    private final LivingEntity entity;
    private final Location origin;
    private final Location head;
    private final Vector dir;
    private final int id;

    public FireBlast(LivingEntity entity, Location target) {
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

        if (PKMethods.isWithinPKShield(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

        if (PKMethods.collidesPKAbility(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

        if (!StaticMethods.isTransparent(head.getBlock()) || StaticMethods.isWater(head.getBlock())) {
            return false;
        }

        StaticMethods.playFirebendingSounds(head);
        StaticMethods.playFirebendingParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);

        for (Entity entity : StaticMethods.getEntitiesAroundPoint(head, 2)) {
            if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
                if (entity instanceof Creature) {
                    ((Creature) entity).setTarget(this.entity);
                }
                ((LivingEntity) entity).damage(damage, this.entity);
                entity.setLastDamageCause(new EntityDamageByEntityEvent(this.entity, entity, DamageCause.CUSTOM, damage));
                entity.setFireTicks((int) (fireTick / 50));
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
