package net.doodcraft.cozmyc.bendingmobs.ability.fire;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import net.doodcraft.cozmyc.bendingmobs.MobMethods;
import org.bukkit.entity.LivingEntity;

public class FireAbility {

    private static final boolean fire = BendingMobs.plugin.getConfig().getBoolean("Abilities.Fire.Enabled");

    public static void execute(LivingEntity entity, LivingEntity target) {
        if (!fire) return;
        if (entity.getWorld() != target.getWorld()) return;
        if (entity.getLocation().distance(target.getLocation()) > 20) {
            new FireJet(entity, target.getLocation());
            return;
        }

        switch (MobMethods.rand.nextInt(2)) {
            case 0:
                new FireBlast(entity, target.getLocation());
                break;
            case 1:
                new FireJet(entity, target.getLocation());
                break;
        }
    }

    public static void progress() {
        FireBlast.progressAll();
        FireJet.progressAll();
    }

    public static void remove() {
        FireBlast.removeAll();
        FireJet.removeAll();
    }
}
