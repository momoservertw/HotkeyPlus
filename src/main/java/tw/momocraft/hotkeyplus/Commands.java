package tw.momocraft.hotkeyplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;
import tw.momocraft.hotkeyplus.handlers.PermissionsHandler;
import tw.momocraft.hotkeyplus.handlers.ServerHandler;
import tw.momocraft.hotkeyplus.utils.Language;


public class Commands implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        if (args.length == 0) {
            if (PermissionsHandler.hasPermission(sender, "hotkeyplus.use")) {
                Language.dispatchMessage(sender, "");
                Language.sendLangMessage("Message.HotkeyPlus.Commands.title", sender, false);
                if (PermissionsHandler.hasPermission(sender, "HotkeyPlus.command.version")) {
                    Language.dispatchMessage(sender, "&d&lHotkeyPlus &e&lv" + HotkeyPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                }
                Language.sendLangMessage("Message.HotkeyPlus.Commands.help", sender, false);
                Language.dispatchMessage(sender, "");
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            if (PermissionsHandler.hasPermission(sender, "hotkeyplus.use")) {
                Language.dispatchMessage(sender, "");
                Language.sendLangMessage("Message.HotkeyPlus.Commands.title", sender, false);
                if (PermissionsHandler.hasPermission(sender, "HotkeyPlus.command.version")) {
                    Language.dispatchMessage(sender, "&d&lHotkeyPlus &e&lv" + HotkeyPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                }
                Language.sendLangMessage("Message.HotkeyPlus.Commands.help", sender, false);
                if (PermissionsHandler.hasPermission(sender, "HotkeyPlus.command.reload")) {
                    Language.sendLangMessage("Message.HotkeyPlus.Commands.reload", sender, false);
                }
                Language.dispatchMessage(sender, "");
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (PermissionsHandler.hasPermission(sender, "hotkeyplus.command.reload")) {
                // working: close purge.Auto-Clean schedule
                ConfigHandler.generateData(true);
                Language.sendLangMessage("Message.configReload", sender);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            if (PermissionsHandler.hasPermission(sender, "hotkeyplus.command.version")) {
                Language.dispatchMessage(sender, "&d&lHotkeyPlus &e&lv" + HotkeyPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                ConfigHandler.getUpdater().checkUpdates(sender);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else {
            Language.sendLangMessage("Message.unknownCommand", sender);
            return true;
        }
    }
}