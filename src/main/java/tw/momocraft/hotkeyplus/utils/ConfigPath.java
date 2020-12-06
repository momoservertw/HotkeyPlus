package tw.momocraft.hotkeyplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;

import java.util.*;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    private Map<String, String> customCmdProp;

    private boolean logDefaultNew;
    private boolean logDefaultZip;
    private boolean logCustomNew;
    private boolean logCustomZip;
    private String logCustomPath;
    private String logCustomName;

    //  ============================================== //
    //         Hotkey Settings                         //
    //  ============================================== //
    private boolean hotkey;
    private boolean hotkeyMenu;
    private boolean hotkeyKeyboard;
    private boolean hotkeyCooldown;
    private int hotkeyCooldownInt;
    private boolean hotkeyCooldownMsg;
    private final Map<Integer, KeyboardMap> hotkeyKeyboardProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setGeneral();
        setHotKey();
    }


    //  ============================================== //
    //         Setup General.                          //
    //  ============================================== //
    private void setGeneral() {
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands." + group));
            }
        }
    }

    //  ============================================== //
    //         Setup HotKey.                           //
    //  ============================================== //
    private void setHotKey() {
        hotkey = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Enable");
        if (hotkey) {
            hotkeyMenu = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Menu.Enable");
            hotkeyKeyboard = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Enable");
        }
        hotkeyCooldown = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Settings.Cooldown.Enable");
        hotkeyCooldownInt = ConfigHandler.getConfig("config.yml").getInt("HotKey.Keyboard.Settings.Cooldown.Interval") * 50;
        hotkeyCooldownMsg = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Settings.Cooldown.Message");

        ConfigurationSection hotkeyKeyboardConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("HotKey.Keyboard.Groups");
        if (hotkeyKeyboardConfig != null) {
            KeyboardMap keyboardMap;
            for (String group : hotkeyKeyboardConfig.getKeys(false)) {
                if (!Utils.isEnable(ConfigHandler.getConfig("config.yml").getString("HotKey.Keyboard.Groups." + group + ".Enable"), true)) {
                    continue;
                }
                keyboardMap = new KeyboardMap();
                keyboardMap.setGroupName(group);
                keyboardMap.setCancel(ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Groups." + group + ".Cancel"));
                keyboardMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Keyboard.Groups." + group + ".Commands"));
                hotkeyKeyboardProp.put(ConfigHandler.getConfig("config.yml").getInt("HotKey.Keyboard.Groups." + group + ".Slot"), keyboardMap);
            }
        }
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    public Map<String, String> getCustomCmdProp() {
        return customCmdProp;
    }

    public boolean isLogDefaultNew() {
        return logDefaultNew;
    }

    public boolean isLogDefaultZip() {
        return logDefaultZip;
    }

    public boolean isLogCustomNew() {
        return logCustomNew;
    }

    public boolean isLogCustomZip() {
        return logCustomZip;
    }

    public String getLogCustomName() {
        return logCustomName;
    }

    public String getLogCustomPath() {
        return logCustomPath;
    }

    //  ============================================== //
    //         Hotkey Settings                         //
    //  ============================================== //
    public boolean isHotkey() {
        return hotkey;
    }

    public boolean isHotkeyMenu() {
        return hotkeyMenu;
    }

    public boolean isHotkeyKeyboard() {
        return hotkeyKeyboard;
    }

    public boolean getHotkeyCooldown() {
        return hotkeyCooldown;
    }

    public boolean isHotkeyCooldownMsg() {
        return hotkeyCooldownMsg;
    }

    public int getHotkeyCooldownInt() {
        return hotkeyCooldownInt;
    }

    public Map<Integer, KeyboardMap> getHotkeyKeyboardProp() {
        return hotkeyKeyboardProp;
    }
}
