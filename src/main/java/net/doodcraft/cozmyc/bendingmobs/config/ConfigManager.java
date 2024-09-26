package net.doodcraft.cozmyc.bendingmobs.config;

import net.doodcraft.cozmyc.bendingmobs.BendingMobs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class ConfigManager {

    final BendingMobs plugin;

    public ConfigManager(BendingMobs plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        FileConfiguration config;
        config = plugin.getConfig();

        config.addDefault("Properties.EntityTypes", new String[]{
                EntityType.ZOMBIE.name().toUpperCase()
        });

        config.addDefault("Properties.BendFrequency", 75);
        config.addDefault("Properties.MovementSpeed", 0.3f);
        config.addDefault("Properties.DoMobsSwim", true);
		config.addDefault("Properties.DenyOtherMobSpawns", false);
        config.addDefault("Properties.Air.NoFallDamage", true);
        config.addDefault("Properties.Avatar.Enabled", false);
        config.addDefault("Properties.Avatar.Frequency", 150);
        config.addDefault("Properties.Entity.Villager.FightBack", true);
		config.addDefault("Properties.DisplayName.Avatar", "Avatar");
		config.addDefault("Properties.DisplayName.Air", "AirBender");
		config.addDefault("Properties.DisplayName.Earth", "EarthBender");
		config.addDefault("Properties.DisplayName.Fire", "FireBender");
		config.addDefault("Properties.DisplayName.Water", "WaterBender");

        config.addDefault("LibsDisguises.Enabled", false);
        config.addDefault("LibsDisguises.SkinName.Avatar", "Avatar");
        config.addDefault("LibsDisguises.SkinName.Air", "AirBender");
        config.addDefault("LibsDisguises.SkinName.Earth", "EarthBender");
        config.addDefault("LibsDisguises.SkinName.Fire", "FireBender");
        config.addDefault("LibsDisguises.SkinName.Water", "WaterBender");

        config.addDefault("Abilities.Air.Enabled", true);
        config.addDefault("Abilities.Air.AirBlast.Knockback", 2.0);
        config.addDefault("Abilities.Air.AirScooter.Duration", 1000);
        config.addDefault("Abilities.Air.AirScooter.Speed", 0.5);

        config.addDefault("Abilities.Earth.Enabled", true);
        config.addDefault("Abilities.Earth.EarthBlast.Damage", 6.0);
        config.addDefault("Abilities.Earth.LavaBlast.Damage", 6.0);

        config.addDefault("Abilities.Fire.Enabled", true);
        config.addDefault("Abilities.Fire.FireBlast.Damage", 5.0);
        config.addDefault("Abilities.Fire.FireBlast.FireTick", 2000);
        config.addDefault("Abilities.Fire.FireJet.Duration", 1000);
        config.addDefault("Abilities.Fire.FireJet.Speed", 0.5);
        config.addDefault("Abilities.Fire.Combustion.Damage", 6.0);
        config.addDefault("Abilities.Fire.Combustion.FireTick", 2000);
        config.addDefault("Abilities.Fire.Lightning.Damage", 7.0);

        config.addDefault("Abilities.Water.Enabled", true);
        config.addDefault("Abilities.Water.WaterBlast.Damage", 4.0);
        config.addDefault("Abilities.Water.IceBlast.Damage", 6.0);

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
}
