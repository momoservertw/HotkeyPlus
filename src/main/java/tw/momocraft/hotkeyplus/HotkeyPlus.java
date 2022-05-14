package tw.momocraft.hotkeyplus;

import org.bukkit.plugin.java.JavaPlugin;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;

public class HotkeyPlus extends JavaPlugin {
    private static HotkeyPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandler.generateData(false);
        CorePlusAPI.getMsg().sendConsoleMsg(ConfigHandler.getPluginPrefix(), "&fhas been Enabled.");
    }

    @Override
    public void onDisable() {
        CorePlusAPI.getMsg().sendConsoleMsg(ConfigHandler.getPluginPrefix(), "&fhas been Disabled.");
    }

    public static HotkeyPlus getInstance() {
        return instance;
    }
}