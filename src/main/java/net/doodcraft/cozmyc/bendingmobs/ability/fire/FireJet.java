package net.doodcraft.cozmyc.bendingmobs.ability.fire;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.StaticMethods;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class FireJet {

    private static final long duration = BendingMobs.plugin.getConfig().getLong("Abilities.Fire.FireJet.Duration");
    private static final double speed = BendingMobs.plugin.getConfig().getDouble("Abilities.Fire.FireJet.Speed");
    public static final ConcurrentHashMap<Integer, FireJet> instances = new ConcurrentHashMap<>();
    private static int ID = Integer.MIN_VALUE;
    private final LivingEntity entity;
    private final Location target;
    private final long time;
    private final int id;

    public FireJet(LivingEntity entity, Location target) {
        this.entity = entity;
        id = ID;
        time = System.currentTimeMillis() + duration;
        this.target = target;
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
        if (entity == null) {
            return false;
        }
        if (System.currentTimeMillis() > time) {
            return false;
        }
        if (entity.getLocation().distance(target) < 6) {
            return false;
        }

        if (StaticMethods.isWater(entity.getLocation().getBlock())) {
            return false;
        }

        StaticMethods.playFirebendingSounds(entity.getLocation());

        Vector direction = target.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(speed);

        entity.setVelocity(direction);

        StaticMethods.playFirebendingParticles(entity.getLocation(), 16, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
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
