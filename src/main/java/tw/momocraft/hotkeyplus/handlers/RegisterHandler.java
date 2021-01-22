package tw.momocraft.hotkeyplus.handlers;

import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.Commands;
import tw.momocraft.hotkeyplus.HotkeyPlus;
import tw.momocraft.hotkeyplus.listeners.*;
import tw.momocraft.hotkeyplus.TabComplete;

public class RegisterHandler {

    public static void registerEvents() {
        HotkeyPlus.getInstance().getCommand("HotkeyPlus").setExecutor(new Commands());
        HotkeyPlus.getInstance().getCommand("HotkeyPlus").setTabCompleter(new TabComplete());

        HotkeyPlus.getInstance().getServer().getPluginManager().registerEvents(new Hotkey(), HotkeyPlus.getInstance());
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                "Register-Event", "Hotkey", "Hotkey", "continue", new Throwable().getStackTrace()[0]);
    }
}
