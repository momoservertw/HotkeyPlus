package tw.momocraft.hotkeyplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import tw.momocraft.coreplus.api.CorePlusAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        int length = args.length;
        if (length == 1) {
            if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.use"))
                commands.add("help");
            if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.reload"))
                commands.add("reload");
            if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.version"))
                commands.add("version");
            if (CorePlusAPI.getPlayer().hasPerm(sender, "hotkeyplus.command.prompt"))
                commands.add("prompt");
        }
        /*
        else {
            switch (args[0].toLowerCase()) {

            }
        }
         */
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}