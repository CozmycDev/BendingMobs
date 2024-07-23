package net.doodcraft.cozmyc.bendingmobs.ability.earth;


import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.PKMethods;
import net.doodcraft.cozmyc.bendingmobs.StaticMethods;
import net.doodcraft.cozmyc.bendingmobs.object.Element;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EarthBlast {

    private static final double damage = BendingMobs.plugin.getConfig().getDouble("Abilities.Earth.EarthBlast.Damage");
    public static ConcurrentHashMap<Integer, EarthBlast> instances = new ConcurrentHashMap<Integer, EarthBlast>();
    private static int ID = Integer.MIN_VALUE;
    private final LivingEntity entity;
    private Location origin;
    private Location head;
    private Vector dir;
    private int id;

    public EarthBlast(LivingEntity entity, Location target) {
        this.entity = entity;

        origin = entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().getBlock().getRelative(StaticMethods.getCardinalDirection(entity.getLocation().getDirection())).getLocation();

        if (!StaticMethods.isEarth(origin.getBlock())) {
            Block targetOrigin = StaticMethods.getRandomSourceBlock(entity.getLocation(), 6, Element.Earth);
            if (targetOrigin == null) {
                return;
            }

            origin = targetOrigin.getLocation();

            head = entity.getEyeLocation();
            dir = StaticMethods.getDirection(origin.add(0, 1, 0), target).normalize();
            id = ID;

            StaticMethods.playEarthbendingSounds(origin);

            instances.put(id, this);
            if (ID == Integer.MAX_VALUE) {
                ID = Integer.MIN_VALUE;
            }
            ID++;
        }
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
        if (entity.isDead()) {
            return false;
        }
        if (entity.getWorld() != head.getWorld()) {
            return false;
        }

        head.add(dir.multiply(1));

        if (origin.distance(head) > 28) {
            return false;
        }

        if (PKMethods.collidesPKAbility(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

        if (!StaticMethods.isTransparent(head.getBlock())) {
            return false;
        }

        if (PKMethods.isWithinPKShield(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

        displayBlock(head);

        for (Entity entity : StaticMethods.getEntitiesAroundPoint(head, 2)) {
            if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
                if (entity instanceof Creature) {
                    ((Creature) entity).setTarget(this.entity);
                }
                ((LivingEntity) entity).damage(damage, this.entity);
                entity.setLastDamageCause(new EntityDamageByEntityEvent(this.entity, entity, DamageCause.CUSTOM, damage));
                return false;
            }
        }
        return true;
    }

    private void remove() {
        instances.remove(id);
    }

    private void displayBlock(Location location) {
        BlockDisplay blockDisplay = Objects.requireNonNull(location.getWorld()).spawn(location, BlockDisplay.class);
        blockDisplay.setBlock(Material.DEEPSLATE.createBlockData());
        Bukkit.getScheduler().runTaskLater(BendingMobs.plugin, blockDisplay::remove, 2L);
    }

    public static void removeAll() {
        for (int id : instances.keySet()) {
            instances.remove(id);
        }
    }
}
