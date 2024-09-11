package net.doodcraft.cozmyc.bendingmobs;

import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.airbending.AirShield;
import com.projectkorra.projectkorra.firebending.FireShield;
import org.bukkit.Location;

import java.util.Collection;

public class PKMethods {

    public static boolean isWithinPKShield(Location loc) {
        if (!Compatibility.isHooked("ProjectKorra")) return false;
        if (AirShield.isWithinShield(loc)) {
            return true;
        }
        return FireShield.isWithinShield(loc);
    }

    public static boolean collidesPKAbility(Location loc) {
        if (!Compatibility.isHooked("ProjectKorra")) return false;
        if (loc == null) return false;

        Collection<CoreAbility> abilities = ElementalAbility.getAbilitiesByInstances();

        for (CoreAbility ability : abilities) {
            if (ability == null || ability.getLocation() == null) return false;
            if (loc.distance(ability.getLocation()) <= 2.0) {
                return true;
            }
        }

        return false;
    }
}
