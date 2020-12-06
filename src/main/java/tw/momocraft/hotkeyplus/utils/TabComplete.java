package tw.momocraft.hotkeyplus.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import tw.momocraft.hotkeyplus.handlers.PermissionsHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        switch (args.length) {
            case 1:
                if (PermissionsHandler.hasPermission(sender, "barrierplus.use")) {
                    commands.add("help");
                }
                if (PermissionsHandler.hasPermission(sender, "barrierplus.command.reload")) {
                    commands.add("reload");
                }
                if (PermissionsHandler.hasPermission(sender, "barrierplus.command.version")) {
                    commands.add("version");
                }
                break;
        }
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}