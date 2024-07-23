package net.doodcraft.cozmyc.bendingmobs;

import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import net.doodcraft.cozmyc.bendingmobs.object.Element;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class StaticMethods {

    public static final Set<Material> TRANSPARENT = new HashSet<>();
    public static Set<String> EARTH_BLOCKS = new HashSet<>();
    public static Set<String> ICE_BLOCKS = new HashSet<>();
    public static Set<String> LAVA_BLOCKS = new HashSet<>();

    static {
        for (final Material mat : Material.values()) {
            if (isTransparent(mat)) {
                TRANSPARENT.add(mat);
            }
        }

        List<String> earthList = new ArrayList<>();
        earthList.add("#base_stone_overworld");
        earthList.add("#base_stone_nether");
        earthList.add("#dirt");

        List<String> iceList = new ArrayList<>();
        earthList.add("#ice");

        List<String> lavaList = new ArrayList<>();
        lavaList.add("LAVA");

        addMaterialTags(EARTH_BLOCKS, earthList);
        addMaterialTags(ICE_BLOCKS, iceList);
        addMaterialTags(LAVA_BLOCKS, lavaList);
    }

    public static boolean isFinite(Location loc) {
        return Double.isFinite(loc.getX()) && Double.isFinite(loc.getY()) && Double.isFinite(loc.getZ());
    }

    public static boolean isFinite(Vector vec) {
        return Double.isFinite(vec.getX()) && Double.isFinite(vec.getY()) && Double.isFinite(vec.getZ());
    }

    public static void addMaterialTags(Set<String> outputSet, List<String> configList) {
        ListIterator<String> iterator = new ArrayList<>(configList).listIterator();
        iterator.forEachRemaining(next -> {
            if (next.startsWith("#")) {
                NamespacedKey key = NamespacedKey.minecraft(next.replaceFirst("#", ""));
                for (Material material : Objects.requireNonNull(Bukkit.getTag(Tag.REGISTRY_BLOCKS, key, Material.class)).getValues()) {
                    outputSet.add(material.toString());
                }
            } else {
                outputSet.add(next.toUpperCase());
            }
        });
    }

    public static void playCombustionPathParticles(Location loc) {
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 4, 0.5F, 0.5F, 1.0F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.SMOKE_LARGE, loc, 4, 0.5F, 0.5F, 1.0F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FIREWORKS_SPARK, loc, 8, 0.5F, 0.5F, 1.0F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0.5F, 0.5F, 0.5F, 0.0, null, true);
    }

    public static void playCombustionHitParticles(Location loc) {
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 12, 0.5F, 0.5F, 1.0F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.SMOKE_LARGE, loc, 12, 0.5F, 0.5F, 1.0F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FIREWORKS_SPARK, loc, 12, 0.5F, 0.5F, 1.0F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.EXPLOSION_HUGE, loc, 4, 0.5F, 0.5F, 0.5F, 0.0, null, true);
        Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 0.75F);
    }

    public static List<Entity> getEntitiesAroundPoint(Location location, double radius) {
        if (location == null || location.getWorld() == null) return new ArrayList<>();

        List<Entity> nearbyEntities = new ArrayList<>(location.getWorld().getNearbyEntities(location, radius, radius, radius));
        List<Entity> filteredEntities = new ArrayList<>();

        for (Entity entity : nearbyEntities) {
            if (isAcceptableEntity(entity)) {
                filteredEntities.add(entity);
            }
        }

        return filteredEntities;
    }

    private static boolean isAcceptableEntity(Entity entity) {
        return !(entity.isDead() ||
                (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR) ||
                (entity instanceof ArmorStand && ((ArmorStand) entity).isMarker()));
    }

    public static boolean isLava(Block block) {
        return LAVA_BLOCKS.contains(block.getType().name().toUpperCase());
    }

    public static boolean isIce(Block block) {
        return ICE_BLOCKS.contains(block.getType().name().toUpperCase());
    }

    public static void playAirbendingSounds(Location loc) {
        if (Compatibility.isHooked("ProjectKorra")) {
            AirAbility.playAirbendingSound(loc);
        } else {
            Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.ENTITY_HORSE_BREATHE, 0.8f, 0.75f);
        }
    }

    public static void playLightningbendingSounds(Location loc) {
        if (Compatibility.isHooked("ProjectKorra")) {
            FireAbility.playLightningbendingSound(loc);
        } else {
            Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.5f, 1.65f);
        }
    }

    public static void playFirebendingSounds(Location loc) {
        if (Compatibility.isHooked("ProjectKorra")) {
            FireAbility.playFirebendingSound(loc);
        } else {
            Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 2.0f, 1.35f);
        }

    }

    public static void playAirbendingParticles(Location loc, int amount, double offsetX, double offsetY, double offsetZ, double extra) {
        if (Compatibility.isHooked("ProjectKorra")) {
            AirAbility.playAirbendingParticles(loc, amount);
        } else {
            Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.CLOUD, loc, amount, offsetX, offsetY, offsetZ, extra, null, true);
        }
    }

    public static void playFirebendingParticles(Location loc, int amount, double offsetX, double offsetY, double offsetZ, double extra) {
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, amount, offsetX, offsetY, offsetZ, extra, null, true);
    }

    public static void playAbilityCollisionParticles(Location loc, int amount, double offsetX, double offsetY, double offsetZ, double extra) {
        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.CRIT, loc, amount, offsetX, offsetY, offsetZ, extra, null, true);
    }

    public static void playEarthbendingSounds(Location loc) {
        Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.ENTITY_BOAT_PADDLE_LAND, 2.0f, 0.75f);
    }

    public static Vector getDirection(Location location, Location destination) {
        return destination.toVector().subtract(location.toVector());
    }

    public static BlockFace getCardinalDirection(Vector vector) {
        BlockFace[] faces = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
        Vector[] directions = {
                new Vector(0, 0, -1),
                new Vector(0.707, 0, -0.707),
                new Vector(1, 0, 0),
                new Vector(0.707, 0, 0.707),
                new Vector(0, 0, 1),
                new Vector(-0.707, 0, 0.707),
                new Vector(-1, 0, 0),
                new Vector(-0.707, 0, -0.707)
        };

        int bestIndex = 0;
        double bestDot = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < directions.length; i++) {
            double dot = vector.dot(directions[i]);
            if (dot > bestDot) {
                bestDot = dot;
                bestIndex = i;
            }
        }

        return faces[bestIndex];
    }

    public static Block getRandomSourceBlock(Location location, int radius, Element element) {
        List<Integer> checked = new ArrayList<Integer>();
        List<Block> blocks = getBlocksAroundPoint(location, radius);

        for (int i = 0; i < blocks.size(); i++) {
            int index = MobMethods.rand.nextInt(blocks.size());

            while (checked.contains(index)) {
                index = MobMethods.rand.nextInt(blocks.size());
            }

            checked.add(index);
            Block block = blocks.get(index);
            if (block == null || block.getLocation().distance(location) < 2) {
                continue;
            }

            switch (element) {
                case Earth:
                    if (isEarth(block) && StaticMethods.isTransparent(block.getRelative(BlockFace.UP))) {
                        return block;
                    }
                case Water:
                    if (isWater(block) && StaticMethods.isTransparent(block.getRelative(BlockFace.UP))) {
                        return block;
                    }
                default:
                    break;
            }
        }
        return null;
    }

    public static List<Block> getBlocksAroundPoint(Location location, double radius) {
        List<Block> blocks = new ArrayList<>();
        int xOrg = location.getBlockX();
        int yOrg = location.getBlockY();
        int zOrg = location.getBlockZ();
        int radiusInt = (int) Math.ceil(radius);

        int minX = xOrg - radiusInt;
        int maxX = xOrg + radiusInt;
        int minY = yOrg - radiusInt;
        int maxY = yOrg + radiusInt;
        int minZ = zOrg - radiusInt;
        int maxZ = zOrg + radiusInt;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z);
                    double distanceSquared = (x - xOrg) * (x - xOrg) + (y - yOrg) * (y - yOrg) + (z - zOrg) * (z - zOrg);
                    if (distanceSquared <= radius * radius) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static boolean isEarth(Block block) {
        return EARTH_BLOCKS.contains(block.getType().name().toUpperCase());
    }

    public static boolean isTransparent(final Block block) {
        return isTransparent(block.getType());
    }

    public static boolean isWater(Block block) {
        if (block == null) return false;

        BlockState state = block.getState();
        if (state instanceof Container) return false;

        BlockData data = block.getBlockData();
        if (data instanceof Waterlogged) return ((Waterlogged) data).isWaterlogged();

        Material material = data.getMaterial();
        return material == Material.WATER ||
                material == Material.SEAGRASS ||
                material == Material.TALL_SEAGRASS ||
                material == Material.KELP_PLANT ||
                material == Material.KELP ||
                material == Material.BUBBLE_COLUMN;
    }

    public static boolean isTransparent(final Material material) {
        return !material.isOccluding() && !material.isSolid();
    }
}
