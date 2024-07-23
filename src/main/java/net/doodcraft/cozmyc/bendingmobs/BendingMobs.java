package net.doodcraft.cozmyc.bendingmobs;

import net.doodcraft.cozmyc.bendingmobs.ability.air.AirAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.earth.EarthAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.fire.FireAbility;
import net.doodcraft.cozmyc.bendingmobs.ability.water.WaterAbility;
import net.doodcraft.cozmyc.bendingmobs.config.ConfigManager;
import net.doodcraft.cozmyc.bendingmobs.listener.MobListener;
import net.doodcraft.cozmyc.bendingmobs.manager.AbilityManager;
import net.doodcraft.cozmyc.bendingmobs.manager.EntityManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BendingMobs extends JavaPlugin {

    public static BendingMobs plugin;
    public static Logger log;
    public static DisguiseStrategy disguiseStrategy;

    @Override
    public void onDisable() {
        AirAbility.remove();
        EarthAbility.remove();
        FireAbility.remove();
        WaterAbility.remove();
        EntityManager.remove();
    }

    @Override
    public void onEnable() {
        plugin = this;
        BendingMobs.log = this.getLogger();
        new ConfigManager(this);
        checkHooks();
        registerEntityTypes();
        selectDisguiseStrategy();
        registerListeners();
        scheduleTasks();

        log.info("BendingMobs is now enabled! Using DisguiseStrategy: " + disguiseStrategy.name());
    }

    private void checkHooks() {
        Compatibility.hookPlugin("ProjectKorra", "1.11", "1.13");
    }

    private void registerEntityTypes() {
        MobMethods.entityTypes.clear();
        for (String type : BendingMobs.plugin.getConfig().getStringList("Properties.EntityTypes")) {
            MobMethods.entityTypes.add(type.toUpperCase());
        }
    }

    private void selectDisguiseStrategy() {
        boolean libsEnabled = BendingMobs.plugin.getConfig().getBoolean("LibsDisguises.Enabled");
        // boolean mythicEnabled = BendingMobs.plugin.getConfig().getBoolean("MythicMobs.Enabled");

        switch (libsEnabled ? 1 : 0) {
            case 0:
                disguiseStrategy = DisguiseStrategy.NONE;
                break;
            case 1:
                disguiseStrategy = DisguiseStrategy.LIBSDISGUISES;
                break;
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new MobListener(this), this);
    }

    private void scheduleTasks() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AbilityManager(), 0L, 1L);
        MobMethods.startMobUpdateTask(this);
    }
}
