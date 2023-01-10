package me.badbones69.vouchers;

import me.badbones69.vouchers.api.enums.ServerProtocol;
import me.badbones69.vouchers.api.enums.Messages;
import me.badbones69.vouchers.controllers.GUI;
import me.badbones69.vouchers.api.FileManager;
import me.badbones69.vouchers.api.FileManager.Files;
import me.badbones69.vouchers.api.CrazyManager;
import me.badbones69.vouchers.commands.VoucherCommands;
import me.badbones69.vouchers.commands.VoucherTab;
import me.badbones69.vouchers.controllers.FireworkDamageAPI;
import me.badbones69.vouchers.controllers.VoucherClick;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Vouchers extends JavaPlugin implements Listener {

    private static Vouchers plugin;
    private FileManager fileManager;
    private CrazyManager crazyManager;

    @Override
    public void onEnable() {
        plugin = this;

        if (ServerProtocol.isNewer(ServerProtocol.v1_16_R3)) {
            getLogger().warning("This jar only works on 1.16.X & below.");
            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        fileManager = new FileManager();
        crazyManager = new CrazyManager();

        fileManager.logInfo(true).setup();

        if (!Files.DATA.getFile().contains("Players")) {
            Files.DATA.getFile().set("Players.Clear", null);
            Files.DATA.saveFile();
        }

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new VoucherClick(), this);
        pluginManager.registerEvents(new GUI(), this);

        getCommand("vouchers").setExecutor(new VoucherCommands());
        getCommand("vouchers").setTabCompleter(new VoucherTab());

        Messages.addMissingMessages();

        try {
            if (ServerProtocol.isNewer(ServerProtocol.v1_10_R1)) pluginManager.registerEvents(new FireworkDamageAPI(this), this);
        } catch (Exception ignored) {}

        FileConfiguration config = Files.CONFIG.getFile();

        boolean metricsEnabled = config.getBoolean("Settings.Toggle-Metrics");
        String metricsPath = config.getString("Settings.Toggle-Metrics");

        if (metricsPath == null) {
            config.set("Settings.Toggle-Metrics", true);

            Files.CONFIG.saveFile();
        }

        if (metricsEnabled) new Metrics(this, 4536);

        crazyManager.load();
    }

    public static Vouchers getPlugin() {
        return plugin;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public CrazyManager getCrazyManager() {
        return crazyManager;
    }
}