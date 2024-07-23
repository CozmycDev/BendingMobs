package net.doodcraft.cozmyc.bendingmobs.ability;

import net.doodcraft.cozmyc.bendingmobs.MobMethods;
import net.doodcraft.cozmyc.bendingmobs.ability.air.AirAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.earth.EarthAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.fire.FireAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.water.WaterAbility;
import org.bukkit.entity.LivingEntity;

public class AvatarAbility {

    public static void execute(LivingEntity entity, LivingEntity target) {
        switch (MobMethods.rand.nextInt(4)) {
            case 0:
                AirAbility.execute(entity, target);
                break;
            case 1:
                EarthAbility.execute(entity, target);
                break;
            case 2:
                FireAbility.execute(entity, target);
                break;
            case 3:
                WaterAbility.execute(entity, target);
                break;
        }
    }
}
