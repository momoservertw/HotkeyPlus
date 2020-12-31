package tw.momocraft.hotkeyplus;

import org.bukkit.plugin.java.JavaPlugin;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;
import tw.momocraft.hotkeyplus.handlers.RegisterHandler;

public class HotkeyPlus extends JavaPlugin {
    private static HotkeyPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandler.generateData(false);
        RegisterHandler.registerEvents();
        CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPlugin(), "&fhas been Enabled.");
    }

    @Override
    public void onDisable() {
        CorePlusAPI.getLangManager().sendConsoleMsg(ConfigHandler.getPlugin(), "&fhas been Disabled.");
    }

    public static HotkeyPlus getInstance() {
        return instance;
    }
}