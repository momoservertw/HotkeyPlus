package tw.momocraft.hotkeyplus.handlers;

import org.bukkit.Bukkit;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.Commands;
import tw.momocraft.hotkeyplus.HotkeyPlus;
import tw.momocraft.hotkeyplus.TabComplete;
import tw.momocraft.hotkeyplus.listeners.Hotkey;

public class DependHandler {

    public void setup(boolean reload) {
        if (!reload) {
            registerEvents();
            checkUpdate();
        }
    }

    public void checkUpdate() {
        if (!ConfigHandler.isCheckUpdates())
            return;
        CorePlusAPI.getUpdate().check(ConfigHandler.getPluginName(),
                ConfigHandler.getPluginPrefix(), Bukkit.getConsoleSender(),
                HotkeyPlus.getInstance().getDescription().getName(),
                HotkeyPlus.getInstance().getDescription().getVersion(), true);
    }

    private void registerEvents() {
        HotkeyPlus.getInstance().getCommand("HotkeyPlus").setExecutor(new Commands());
        HotkeyPlus.getInstance().getCommand("HotkeyPlus").setTabCompleter(new TabComplete());

        HotkeyPlus.getInstance().getServer().getPluginManager().registerEvents(new Hotkey(), HotkeyPlus.getInstance());
    }
}
