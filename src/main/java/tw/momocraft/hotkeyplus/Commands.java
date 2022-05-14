package tw.momocraft.hotkeyplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;
import tw.momocraft.hotkeyplus.utils.HotkeyMap;

import java.util.Collection;


public class Commands implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        int length = args.length;
        if (length == 0) {
            if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.use")) {
                CorePlusAPI.getMsg().sendMsg(sender, "");
                CorePlusAPI.getMsg().sendMsg(sender, ConfigHandler.getConfigPath().getMsgCmdTitle());
                CorePlusAPI.getMsg().sendMsg(sender,
                        "&f " + HotkeyPlus.getInstance().getDescription().getName()
                                + " &ev" + HotkeyPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                CorePlusAPI.getMsg().sendMsg(sender, ConfigHandler.getConfigPath().getMsgCmdHelp());
                CorePlusAPI.getMsg().sendMsg(sender, "");
            } else {
                CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
            }
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.use")) {
                    CorePlusAPI.getMsg().sendMsg(sender, "");
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getConfigPath().getMsgCmdTitle(), sender);
                    CorePlusAPI.getMsg().sendMsg(sender,
                            "&f " + HotkeyPlus.getInstance().getDescription().getName()
                                    + " &ev" + HotkeyPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getConfigPath().getMsgCmdHelp(), sender);
                    if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.reload"))
                        CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getConfigPath().getMsgCmdReload(), sender);
                    if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.version"))
                        CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getConfigPath().getMsgCmdVersion(), sender);
                    if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.prompt"))
                        CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getConfigPath().getMsgCmdPrompt(), sender);
                    CorePlusAPI.getMsg().sendMsg(sender, "");
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "reload":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.reload")) {
                    ConfigHandler.generateData(true);
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.configReload", sender);
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "version":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.version")) {
                    CorePlusAPI.getMsg().sendMsg(ConfigHandler.getPrefix(), sender,
                            "&f " + HotkeyPlus.getInstance().getDescription().getName()
                                    + " &ev" + HotkeyPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getUpdate().check(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(), sender,
                            HotkeyPlus.getInstance().getName(), HotkeyPlus.getInstance().getDescription().getVersion(), true);
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "prompt":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.prompt")) {
                    String prompt = ConfigHandler.getConfigPath().getMsgPrompt();
                    Collection<HotkeyMap> values = ConfigHandler.getConfigPath().getHotkeyProp().values();
                    for (HotkeyMap hotkeyMap : values)
                        prompt = prompt.replace("%key_" + hotkeyMap.getKey() + "%", hotkeyMap.getFeaturePlaceHolder());
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(), prompt, sender);
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
        }
        CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                "Message.unknownCommand", sender);
        return true;
    }
}