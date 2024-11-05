package dev.jsinco.brewery.util;

import dev.jsinco.brewery.TheBrewingProject;
import org.bukkit.Bukkit;

public final class Logging {

    public static void info(String m) {
        Bukkit.getConsoleSender().sendMessage(m);
    }

    public static void debug(String m) {
        if (TheBrewingProject.getInstance().getConfigManager().getConfig().verboseLogging) {
            Bukkit.getConsoleSender().sendMessage(m);
        }
    }

    public static void error(String m, Throwable throwable) {
        Bukkit.getConsoleSender().sendMessage(m);
    }
}
