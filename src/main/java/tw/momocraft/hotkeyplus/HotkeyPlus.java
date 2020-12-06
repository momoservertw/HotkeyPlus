package tw.momocraft.hotkeyplus;

import org.bukkit.plugin.java.JavaPlugin;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;
import tw.momocraft.hotkeyplus.handlers.RegisterHandler;
import tw.momocraft.hotkeyplus.handlers.ServerHandler;

public class HotkeyPlus extends JavaPlugin {
    private static HotkeyPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandler.generateData(false);
        RegisterHandler.registerEvents();
        ServerHandler.sendConsoleMessage("&fhas been Enabled.");
    }

    @Override
    public void onDisable() {
        ServerHandler.sendConsoleMessage("&fhas been Disabled.");
    }

    public static HotkeyPlus getInstance() {
        return instance;
    }
}