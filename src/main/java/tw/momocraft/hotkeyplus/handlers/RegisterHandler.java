package tw.momocraft.hotkeyplus.handlers;

import tw.momocraft.hotkeyplus.Commands;
import tw.momocraft.hotkeyplus.HotkeyPlus;
import tw.momocraft.hotkeyplus.listeners.*;
import tw.momocraft.hotkeyplus.utils.TabComplete;

public class RegisterHandler {

    public static void registerEvents() {
        HotkeyPlus.getInstance().getCommand("HotkeyPlus").setExecutor(new Commands());
        HotkeyPlus.getInstance().getCommand("HotkeyPlus").setTabCompleter(new TabComplete());

        HotkeyPlus.getInstance().getServer().getPluginManager().registerEvents(new Hotkey(), HotkeyPlus.getInstance());
        ServerHandler.sendFeatureMessage("Register-Event", "Hotkey", "Hotkey", "continue",
                new Throwable().getStackTrace()[0]);
    }
}
