package net.doodcraft.cozmyc.bendingmobs.ability.water;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import org.bukkit.entity.LivingEntity;

public class WaterAbility {

    private static final boolean water = BendingMobs.plugin.getConfig().getBoolean("Abilities.Water.Enabled");

    public static void execute(LivingEntity entity, LivingEntity target) {
        if (!water) return;
        if (entity.getWorld() != target.getWorld()) return;
        new WaterBlast(entity, target.getLocation());
    }

    public static void progress() {
        WaterBlast.progressAll();
    }

    public static void remove() {
        WaterBlast.removeAll();
    }
}
