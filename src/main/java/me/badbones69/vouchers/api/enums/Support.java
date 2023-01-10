package me.badbones69.vouchers.api.enums;

import me.badbones69.vouchers.Vouchers;

public enum Support {
    
    PLACEHOLDERAPI("PlaceholderAPI"),
    MVDWPLACEHOLDERAPI("MVdWPlaceholderAPI");
    
    private final String name;

    private final Vouchers plugin = Vouchers.getPlugin();
    
    Support(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isPluginLoaded() {
        if (plugin.getServer().getPluginManager().getPlugin(name) != null) return plugin.isEnabled();

        return false;
    }
}