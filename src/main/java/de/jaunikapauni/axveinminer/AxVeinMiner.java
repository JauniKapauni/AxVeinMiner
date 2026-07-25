package de.jaunikapauni.axveinminer;

import de.jaunikapauni.axveinminer.listener.BlockBreakListener;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class AxVeinMiner extends JavaPlugin {
    public int MAX_RADIUS;
    public int MAX_BLOCKS;
    public Set<Material> MATERIALS;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        MAX_RADIUS = config.getInt("max-radius");
        MAX_BLOCKS = config.getInt("max-blocks");
        MATERIALS = new HashSet<>();
        for(String mat : config.getStringList("materials")){
            Material m = Material.matchMaterial(mat);
            if(m != null) MATERIALS.add(m);
        }
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
