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
    private boolean hotkeyCooldown;
    private int hotkeyCdInterval;
    private boolean hotkeyCdMsg;

    private boolean shiftF;
    private KeyboardMap shiftFKeyboardMap;

    private boolean doubleShift;
    private int doubleShiftInterval;
    private String doubleShiftMenuMain;
    private String doubleShiftMenuSeparate;
    private String doubleShiftMenuKey;
    private boolean doubleShiftMenuCancel;
    private int doubleShiftMenuCancelTime;
    private boolean doubleShiftCustom;
    private int doubleShiftCustomCmdLimit;
    private List<String> doubleShiftCustomBlackCmds;
    private final Map<String, KeyboardMap> doubleShiftProp = new HashMap<>();

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
        if (!hotkey) {
            return;
        }
        hotkeyCooldown = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Shift-F.Settings.Cooldown.Enable");
        hotkeyCdInterval = ConfigHandler.getConfig("config.yml").getInt("HotKey.Shift-F.Settings.Cooldown.Interval") * 50;
        hotkeyCdMsg = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Shift-F.Settings.Cooldown.Message");

        shiftF = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Shift-F.Enable");
        shiftFKeyboardMap = new KeyboardMap();
        shiftFKeyboardMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Shift-F.Commands"));
        shiftFKeyboardMap.setGroup("Shift-F");

        doubleShift = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Double-Shift.Enable");
        doubleShiftInterval = ConfigHandler.getConfig("config.yml").getInt("HotKey.Double-Shift.Settings.Shift-Interval") * 50;
        doubleShiftMenuMain = ConfigHandler.getConfig("config.yml").getString("HotKey.Double-Shift.Settings.Menu.Format.Main");
        doubleShiftMenuSeparate = ConfigHandler.getConfig("config.yml").getString("HotKey.Double-Shift.Settings.Menu.Format.Separate");
        doubleShiftMenuKey = ConfigHandler.getConfig("config.yml").getString("HotKey.Double-Shift.Settings.Menu.Format.Key");
        doubleShiftMenuCancel = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Double-Shift.Settings.Menu.Auto-Cancel.Enable");
        doubleShiftMenuCancelTime = ConfigHandler.getConfig("config.yml").getInt("HotKey.Double-Shift.Settings.Menu.Auto-Cancel.Time");

        doubleShiftCustom = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Double-Shift.Custom.Enable");
        if (doubleShiftCustom) {
            CorePlusAPI.getMySQLManager().connect(ConfigHandler.getPrefix(), "hotkeyplus");
            if ()
        }
        doubleShiftCustomCmdLimit = ConfigHandler.getConfig("config.yml").getInt("HotKey.Double-Shift.Custom.Commands-Limit");
        doubleShiftCustomBlackCmds = ConfigHandler.getConfig("config.yml").getStringList("HotKey.Double-Shift.Custom.Black-List");

        ConfigurationSection hotkeyKeyboardConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("HotKey.Double-Shift.Groups");
        if (hotkeyKeyboardConfig == null) {
            return;
        }
        KeyboardMap keyboardMap;
        String key;
        String page;
        String sequence;
        for (String group : hotkeyKeyboardConfig.getKeys(false)) {
            if (!ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Groups." + group + ".Enable", true)) {
                continue;
            }
            keyboardMap = new KeyboardMap();
            keyboardMap.setKey(group);
            key = ConfigHandler.getConfig("config.yml").getString("HotKey.Keyboard.Groups." + group + ".Key");
            if (key == null || !key.matches("1-9f")) {
                CorePlusAPI.getLangManager().sendErrorMsg(ConfigHandler.getPluginName(), "You need to set \"Key\" for " + group + " - Available: 1~9, f");
                continue;
            }
            keyboardMap.setKey(key);
            page = ConfigHandler.getConfig("config.yml").getString("HotKey.Keyboard.Groups." + group + ".Page");
            sequence = ConfigHandler.getConfig("config.yml").getString("HotKey.Keyboard.Groups." + group + ".Sequence");
            try {
                keyboardMap.setPage(Integer.parseInt(page));
                keyboardMap.setSequence(Integer.parseInt(sequence));
            } catch (Exception e) {
                CorePlusAPI.getLangManager().sendErrorMsg(ConfigHandler.getPluginName(), "You need to set \"Page\" and \"Sequence\" for " + group);
                continue;
            }
            keyboardMap.setDisplay(ConfigHandler.getConfig("config.yml").getString("HotKey.Keyboard.Groups." + group + ".Display"));
            keyboardMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Keyboard.Groups." + group + ".Commands"));
            doubleShiftProp.put(group, keyboardMap);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                    "HotKey", "Double-Shift", "setup", "continue", group, new Throwable().getStackTrace()[0]);
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


    public boolean isHotkeyCooldown() {
        return hotkeyCooldown;
    }

    public int getHotkeyCdInterval() {
        return hotkeyCdInterval;
    }

    public boolean isHotkeyCdMsg() {
        return hotkeyCdMsg;
    }

    public boolean isShiftF() {
        return shiftF;
    }

    public KeyboardMap getShiftFKeyboardMap() {
        return shiftFKeyboardMap;
    }

    public boolean isDoubleShift() {
        return doubleShift;
    }

    public int getDoubleShiftInterval() {
        return doubleShiftInterval;
    }

    public String getDoubleShiftMenuMain() {
        return doubleShiftMenuMain;
    }

    public String getDoubleShiftMenuSeparate() {
        return doubleShiftMenuSeparate;
    }

    public String getDoubleShiftMenuKey() {
        return doubleShiftMenuKey;
    }

    public boolean isDoubleShiftMenuCancel() {
        return doubleShiftMenuCancel;
    }

    public int getDoubleShiftMenuCancelTime() {
        return doubleShiftMenuCancelTime;
    }

    public boolean isDoubleShiftCustom() {
        return doubleShiftCustom;
    }

    public int getDoubleShiftCustomCmdLimit() {
        return doubleShiftCustomCmdLimit;
    }

    public List<String> getDoubleShiftCustomBlackCmds() {
        return doubleShiftCustomBlackCmds;
    }

    public Map<String, KeyboardMap> getDoubleShiftProp() {
        return doubleShiftProp;
    }
}
