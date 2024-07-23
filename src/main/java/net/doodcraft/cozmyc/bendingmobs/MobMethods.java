package net.doodcraft.cozmyc.bendingmobs;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.doodcraft.cozmyc.bendingmobs.object.Element;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobMethods {

    private static final boolean avatar = BendingMobs.plugin.getConfig().getBoolean("Properties.Avatar.Enabled");
    private static final int avatarFrequency = BendingMobs.plugin.getConfig().getInt("Properties.Avatar.Frequency");

    private static final double movementSpeed = BendingMobs.plugin.getConfig().getDouble("Properties.MovementSpeed");
    private static final boolean mobsSwim = BendingMobs.plugin.getConfig().getBoolean("Properties.DoMobsSwim");

    public static List<String> disabledWorlds = new ArrayList<String>();
    public static List<String> entityTypes = new ArrayList<String>();

    public static Random rand = new Random();

    public static void assignElement(Entity entity) {
        if (!entity.hasMetadata("element")) {
            int elementIndex = getRandomElementIndex();
            entity.setMetadata("element", new FixedMetadataValue(BendingMobs.plugin, elementIndex));
            setMovementSpeed(entity);
        }
    }

    private static int getRandomElementIndex() {
        if (avatar && rand.nextInt(avatarFrequency) == 0) {
            return Element.Avatar.ordinal();
        }
        return rand.nextInt(Element.values().length - 1);
    }

    public static void setMovementSpeed(Entity entity) {
        LivingEntity livingEntity = (LivingEntity) entity;
        if (livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
            livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(movementSpeed);
        }
    }

    public static void assignElement(Entity entity, Element element) {
        if (entity == null) return;
        if (element != null) {
            entity.setMetadata("element", new FixedMetadataValue(BendingMobs.plugin, element.ordinal()));
        }
    }

    public static boolean isAvatar(LivingEntity entity) {
        if (entity.hasMetadata("element") && !entity.getMetadata("element").isEmpty() && (entity.getMetadata("element").get(0).asInt() == 4)) {
            return true;
        }
        return false;
    }

    public static boolean isDisabledWorld(World world) {
        return disabledWorlds.contains(world.getName());
    }

    // todo refactor elsewhere
    public static void startMobUpdateTask(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Entity> entitiesToRemove = new ArrayList<>();

                for (Entity entity : Bukkit.getWorlds().stream().flatMap(world -> world.getEntities().stream()).toList()) {
                    if (!(entity instanceof LivingEntity)) continue;
                    if (!canEntityBend(entity.getType().name())) continue;

                    if (!(hasElement((LivingEntity) entity))) {
                        entitiesToRemove.add(entity);
                        continue;
                    }

                    Element element = getElement((LivingEntity) entity);
                    disguiseEntity(entity, element);

                    if (mobsSwim) {
                        if (!(entity instanceof Monster)) continue;
                        if (entity.isInWater()) {
                            Vector velocity = new Vector(0, 0.64, 0);
                            LivingEntity target = ((Monster) entity).getTarget();
                            if (target != null) {
                                Vector direction = target.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                                direction.multiply(0.18);
                                velocity.add(direction);
                            }
                            if (StaticMethods.isFinite(velocity)) entity.setVelocity(velocity);
                        }
                    }
                }

                for (Entity entity : entitiesToRemove) {
                    entity.remove();
                }
            }
        }.runTaskTimer(plugin, 100L, 10L);
    }

    public static boolean canEntityBend(String type) {
        return entityTypes.contains(type.toUpperCase());
    }

    public static boolean hasElement(LivingEntity entity) {
        return entity.hasMetadata("element") && !entity.getMetadata("element").isEmpty();
    }

    public static Element getElement(LivingEntity entity) {
        if ((entity.hasMetadata("element") && !entity.getMetadata("element").isEmpty())) {
            return Element.getType(entity.getMetadata("element").get(0).asInt());
        }
        return null;
    }

    public static void disguiseEntity(Entity entity, Element element) {
        String skinName = getNameFor(element, "LibsDisguises.SkinName");
        String displayName = getNameFor(element, "Properties.DisplayName");

        // String mythicMobName = getDisguiseName(element, "MythicMobs.Mob");

        if (BendingMobs.disguiseStrategy.equals(DisguiseStrategy.LIBSDISGUISES)) {
            if (DisguiseAPI.isDisguised(entity)) return;
            PlayerDisguise playerDisguise = new PlayerDisguise(skinName);
            playerDisguise.setEntity(entity);
            playerDisguise.startDisguise();
        }

        entity.setCustomName(displayName);
        entity.setCustomNameVisible(true);
    }

    private static String getNameFor(Element element, String configPath) {
        return switch (element) {
            case Air -> BendingMobs.plugin.getConfig().getString(configPath + ".Air");
            case Earth -> BendingMobs.plugin.getConfig().getString(configPath + ".Earth");
            case Fire -> BendingMobs.plugin.getConfig().getString(configPath + ".Fire");
            case Water -> BendingMobs.plugin.getConfig().getString(configPath + ".Water");
            case Avatar -> BendingMobs.plugin.getConfig().getString(configPath + ".Avatar");
            default -> "Herobrine";
        };
    }
}
