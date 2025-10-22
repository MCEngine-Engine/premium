package io.github.mcengine.spigotmc.premium.engine;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.Metrics;
import io.github.mcengine.common.premium.MCEnginePremiumCommon;
import io.github.mcengine.common.premium.command.MCEnginePremiumCommand;
import io.github.mcengine.common.premium.tabcompleter.MCEnginePremiumTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main SpigotMC plugin class for MCEnginePremium.
 */
public class MCEnginePremiumSpigotMC extends JavaPlugin {

    /** Premium common API singleton. */
    private MCEnginePremiumCommon api;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        new Metrics(this, 26359); // bStats Metrics ID for Premium
        saveDefaultConfig(); // Save config.yml if it doesn't exist

        boolean enabled = getConfig().getBoolean("enable", false);
        if (!enabled) {
            getLogger().warning("Plugin is disabled in config.yml (enable: false). Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        String license = getConfig().getString("licenses.license", "free"); 
        if (!license.equalsIgnoreCase("free")) { 
            getLogger().warning("Plugin is disabled in config.yml.");
            getLogger().warning("Invalid license.");
            getLogger().warning("Check license or use \"free\".");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        api = new MCEnginePremiumCommon(this);

        // Register command executor and tab completer
        if (getCommand("premium") != null) {
            getCommand("premium").setExecutor(new MCEnginePremiumCommand());
            getCommand("premium").setTabCompleter(new MCEnginePremiumTabCompleter());
        } else {
            getLogger().warning("Command 'premium' not found in plugin.yml");
        }

        // Load extensions for Premium module
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.premium.extension.library.IMCEnginePremiumLibrary",
            "libraries",
            "Library"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.premium.extension.api.IMCEnginePremiumAPI",
            "apis",
            "API"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.premium.extension.agent.IMCEnginePremiumAgent",
            "agents",
            "Agent"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.premium.extension.addon.IMCEnginePremiumAddOn",
            "addons",
            "AddOn"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.premium.extension.dlc.IMCEnginePremiumDLC",
            "dlcs",
            "DLC"
        );

        // Check for plugin updates
        MCEngineCoreApi.checkUpdate(
            this,
            getLogger(),
            "github",
            "MCEngine-Engine",
            "premium",
            getConfig().getString("github.token", "null")
        );
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic (if any) can go here
    }
}
