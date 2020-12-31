package tw.momocraft.hotkeyplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;

import java.util.*;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         Message Variables                       //
    //  ============================================== //
    private String msgTitle;
    private String msgHelp;
    private String msgReload;
    private String msgVersion;

    //  ============================================== //
    //         Hotkey Variables                        //
    //  ============================================== //
    private boolean hotkey;
    private boolean hotkeyKeyboard;
    private boolean hotkeyCooldown;
    private int hotkeyCooldownInt;
    private boolean hotkeyCooldownMsg;
    private final Map<Integer, KeyboardMap> hotkeyKeyboardProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration                 //
    //  ============================================== //
    private void setUp() {
        setupMsg();
        setHotKey();
    }

    //  ============================================== //
    //         Message Setter                          //
    //  ============================================== //
    private void setupMsg() {
        msgTitle = ConfigHandler.getConfig("config.yml").getString("Message.Commands.title");
        msgHelp = ConfigHandler.getConfig("config.yml").getString("Message.Commands.help");
        msgReload = ConfigHandler.getConfig("config.yml").getString("Message.Commands.reload");
        msgVersion = ConfigHandler.getConfig("config.yml").getString("Message.Commands.version");
    }


    //  ============================================== //
    //         HotKey Setter                           //
    //  ============================================== //
    private void setHotKey() {
        hotkey = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Enable");
        if (hotkey) {
            hotkeyKeyboard = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Enable");
        }
        hotkeyCooldown = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Settings.Cooldown.Enable");
        hotkeyCooldownInt = ConfigHandler.getConfig("config.yml").getInt("HotKey.Keyboard.Settings.Cooldown.Interval") * 50;
        hotkeyCooldownMsg = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Settings.Cooldown.Message");

        ConfigurationSection hotkeyKeyboardConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("HotKey.Keyboard.Groups");
        if (hotkeyKeyboardConfig == null) {
            return;
        }
        KeyboardMap keyboardMap;
        for (String group : hotkeyKeyboardConfig.getKeys(false)) {
            if (!ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Groups." + group + ".Enable", true)) {
                continue;
            }
            keyboardMap = new KeyboardMap();
            keyboardMap.setGroupName(group);
            keyboardMap.setCancel(ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Groups." + group + ".Cancel"));
            keyboardMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Keyboard.Groups." + group + ".Commands"));
            hotkeyKeyboardProp.put(ConfigHandler.getConfig("config.yml").getInt("HotKey.Keyboard.Groups." + group + ".Slot"), keyboardMap);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPlugin(), "HotKey", "Keyboard", "setup", "continue", group,
                    new Throwable().getStackTrace()[0]);
        }
    }


    //  ============================================== //
    //         Message Getter                          //
    //  ============================================== //
    public String getMsgTitle() {
        return msgTitle;
    }

    public String getMsgHelp() {
        return msgHelp;
    }

    public String getMsgReload() {
        return msgReload;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    //  ============================================== //
    //         Hotkey Getter                           //
    //  ============================================== //
    public boolean isHotkey() {
        return hotkey;
    }

    public boolean isHotkeyKeyboard() {
        return hotkeyKeyboard;
    }

    public boolean isHotkeyCooldown() {
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
