package tw.momocraft.hotkeyplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         Message Variables                       //
    //  ============================================== //
    private String msgCmdTitle;
    private String msgCmdHelp;
    private String msgCmdReload;
    private String msgCmdVersion;
    private String msgCmdPrompt;

    private String msgPrompt;

    //  ============================================== //
    //         Hotkey Variables                        //
    //  ============================================== //
    private boolean hotkey;
    private boolean hotkeyCooldown;
    private int hotkeyCdInterval;
    private boolean hotkeyCdMsg;
    private boolean hotkeyPrompt;
    private String hotkeyPromptCmd;

    private final Map<String, HotkeyMap> hotkeyProp = new HashMap<>();

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
        msgCmdTitle = ConfigHandler.getConfig("message.yml").getString("Message.Commands.title");
        msgCmdHelp = ConfigHandler.getConfig("message.yml").getString("Message.Commands.help");
        msgCmdReload = ConfigHandler.getConfig("message.yml").getString("Message.Commands.reload");
        msgCmdVersion = ConfigHandler.getConfig("message.yml").getString("Message.Commands.version");
        msgCmdPrompt = ConfigHandler.getConfig("message.yml").getString("Message.Commands.prompt");

        msgPrompt = ConfigHandler.getConfig("message.yml").getString("Message.HotkeyPlus.prompt");
    }


    //  ============================================== //
    //         HotKey Setter                           //
    //  ============================================== //
    private void setHotKey() {
        hotkey = ConfigHandler.getConfig("config.yml").getBoolean("Hotkey.Enable");
        hotkeyCooldown = ConfigHandler.getConfig("config.yml").getBoolean("Hotkey.Settings.Cooldown.Enable");
        hotkeyCdInterval = ConfigHandler.getConfig("config.yml").getInt("Hotkey.Settings.Cooldown.Interval") * 1000;
        hotkeyCdMsg = ConfigHandler.getConfig("config.yml").getBoolean("Hotkey.Settings.Cooldown.Message");
        hotkeyPrompt = ConfigHandler.getConfig("config.yml").getBoolean("Hotkey.Settings.Double-Shift-Prompt.Enable");
        hotkeyPromptCmd = ConfigHandler.getConfig("config.yml").getString("Hotkey.Settings.Double-Shift-Prompt.Command");

        ConfigurationSection hotkeyKeyboardConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Hotkey.Groups");
        if (hotkeyKeyboardConfig == null)
            return;
        List<String> commands;
        for (String group : hotkeyKeyboardConfig.getKeys(false)) {
            if (!ConfigHandler.getConfig("config.yml").getBoolean("Hotkey.Groups." + group + ".Enable", true))
                continue;
            commands = ConfigHandler.getConfig("config.yml").getStringList("Hotkey.Groups." + group + ".Commands");
            if (commands.isEmpty())
                continue;
            HotkeyMap hotkeyMap = new HotkeyMap();
            hotkeyMap.setGroupName(group);
            hotkeyMap.setKey(group.split("Shift-")[1].toLowerCase());
            hotkeyMap.setFeaturePlaceHolder(
                    ConfigHandler.getConfig("config.yml").getString("Hotkey.Groups." + group + ".PlaceHolder"));
            hotkeyMap.setCommands(commands);
            hotkeyProp.put(group, hotkeyMap);
            CorePlusAPI.getMsg().sendDetailMsg(ConfigHandler.isDebug(), ConfigHandler.getPluginName(),
                    "Hotkey", "Groups", "setup", "continue", group, new Throwable().getStackTrace()[0]);
        }
    }


    //  ============================================== //
    //         Message Getter                          //
    //  ============================================== //
    public String getMsgCmdTitle() {
        return msgCmdTitle;
    }

    public String getMsgCmdHelp() {
        return msgCmdHelp;
    }

    public String getMsgCmdReload() {
        return msgCmdReload;
    }

    public String getMsgCmdVersion() {
        return msgCmdVersion;
    }

    public String getMsgCmdPrompt() {
        return msgCmdPrompt;
    }

    public String getMsgPrompt() {
        return msgPrompt;
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

    public boolean isHotkeyPrompt() {
        return hotkeyPrompt;
    }

    public String getHotkeyPromptCmd() {
        return hotkeyPromptCmd;
    }

    public Map<String, HotkeyMap> getHotkeyProp() {
        return hotkeyProp;
    }
}
