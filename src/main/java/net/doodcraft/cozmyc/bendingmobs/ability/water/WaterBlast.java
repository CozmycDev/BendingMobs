package net.doodcraft.cozmyc.bendingmobs.ability.water;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.PKMethods;
import net.doodcraft.cozmyc.bendingmobs.StaticMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WaterBlast {

    private static final double damage = BendingMobs.plugin.getConfig().getDouble("Abilities.Water.WaterBlast.Damage");
    public static final ConcurrentHashMap<Integer, WaterBlast> instances = new ConcurrentHashMap<>();
    private static int ID = Integer.MIN_VALUE;
    private final LivingEntity entity;
    private final Location origin;
    private final int id;
    private final Location head;
    private final Vector dir;

    public WaterBlast(LivingEntity entity, Location target) {
        this.entity = entity;
        origin = entity.getEyeLocation();
        head = entity.getEyeLocation();
        dir = StaticMethods.getDirection(entity.getLocation(), target).normalize();
        Objects.requireNonNull(entity.getEquipment()).setItemInMainHand(new ItemStack(Material.POTION, 1));
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
        if (entity == null || StaticMethods.isFinite(head) || !StaticMethods.isFinite(dir)) {
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

        head.add(dir.multiply(1));

        if (!StaticMethods.isTransparent(head.getBlock())) {
            return false;
        }

        if (PKMethods.isWithinPKShield(head)) {
            StaticMethods.playAbilityCollisionParticles(head, 10, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0);
            return false;
        }

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

        displayBlock(head);
        return true;
    }

    private void remove() {
        instances.remove(id);
    }

    private void displayBlock(Location location) {
        BlockDisplay blockDisplay = Objects.requireNonNull(location.getWorld()).spawn(location, BlockDisplay.class);

        blockDisplay.setBlock(Material.BLUE_ICE.createBlockData());
        blockDisplay.setDisplayHeight(0.5F);
        blockDisplay.setDisplayWidth(0.5F);
        blockDisplay.setPersistent(false);

        Bukkit.getScheduler().runTaskLater(BendingMobs.plugin, blockDisplay::remove, 2L);
    }

    public static void removeAll() {
        for (int id : instances.keySet()) {
            instances.remove(id);
        }
    }
}
