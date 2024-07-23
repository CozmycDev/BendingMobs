package net.doodcraft.cozmyc.bendingmobs.ability.earth;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import org.bukkit.entity.LivingEntity;

public class EarthAbility {

    private static final boolean earth = BendingMobs.plugin.getConfig().getBoolean("Abilities.Earth.Enabled");

    public static void execute(LivingEntity entity, LivingEntity target) {
        if (!earth) return;
        if (entity.getWorld() != target.getWorld()) return;
        new EarthBlast(entity, target.getLocation());
    }

    public static void progress() {
        EarthBlast.progressAll();
    }

    public static void remove() {
        EarthBlast.removeAll();
    }
}
