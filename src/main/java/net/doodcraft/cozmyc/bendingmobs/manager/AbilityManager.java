package net.doodcraft.cozmyc.bendingmobs.manager;

import net.doodcraft.cozmyc.bendingmobs.ability.air.AirAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.earth.EarthAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.fire.FireAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.water.WaterAbility;

public class AbilityManager implements Runnable {

    public void run() {
        EntityManager.progress();
        AirAbility.progress();
        EarthAbility.progress();
        FireAbility.progress();
        WaterAbility.progress();
    }
}