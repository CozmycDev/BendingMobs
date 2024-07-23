package net.doodcraft.cozmyc.bendingmobs.ability.air;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.MobMethods;
import org.bukkit.entity.LivingEntity;

public class AirAbility {

    private static final boolean air = BendingMobs.plugin.getConfig().getBoolean("Abilities.Air.Enabled");

    public static void execute(LivingEntity entity, LivingEntity target) {
        if (!air) return;
        if (entity.getWorld() != target.getWorld()) return;

        if (entity.getLocation().distance(target.getLocation()) > 20) {
            new AirScooter(entity, target.getLocation());
            return;
        }

        switch (MobMethods.rand.nextInt(2)) {
            case 0:
                new AirBlast(entity, target.getLocation());
                break;
            case 1:
                new AirScooter(entity, target.getLocation());
                break;
        }
    }

    public static void progress() {
        AirBlast.progressAll();
        AirScooter.progressAll();
    }

    public static void remove() {
        AirBlast.removeAll();
        AirScooter.removeAll();
    }
}
