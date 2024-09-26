package net.doodcraft.cozmyc.bendingmobs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Compatibility {

    private static final Map<String, Plugin> hookedPlugins = new HashMap<>();

    public static boolean isHooked(String name) {
        return hookedPlugins.containsKey(name);
    }

    public static void hookPlugin(String name, String minVersion, String maxVersion) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

        if (plugin == null) {
            return;
        }

        String version = plugin.getDescription().getVersion().split("-")[0];

        if (isVersionSupported(version, minVersion, maxVersion)) {
            hookPlugin(name, plugin);
        } else {
            Bukkit.getLogger().info(String.format("%s v%s is unknown or unsupported. Attempting to hook anyway. There may be errors.", name, version));
            hookPlugin(name, plugin);
        }
    }

    private static boolean isVersionSupported(String version, String minVersion, String maxVersion) {
        try {
            return compareVersions(version, minVersion) >= 0 && compareVersions(version, maxVersion) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static void hookPlugin(String name, Plugin plugin) {
        if (hookedPlugins.containsKey(name)) {
            return;
        }
        hookedPlugins.put(name, plugin);
        Bukkit.getLogger().info(String.format("Hooked into %s successfully!", name));
    }

    private static int compareVersions(String version, String otherVersion) {
        String[] versionParts = version.split("\\.");
        String[] otherVersionParts = otherVersion.split("\\.");

        int length = Math.max(versionParts.length, otherVersionParts.length);
        for (int i = 0; i < length; i++) {
            int vPart = i < versionParts.length ? Integer.parseInt(versionParts[i]) : 0;
            int oPart = i < otherVersionParts.length ? Integer.parseInt(otherVersionParts[i]) : 0;
            if (vPart != oPart) {
                return Integer.compare(vPart, oPart);
            }
        }
        return 0;
    }
}
