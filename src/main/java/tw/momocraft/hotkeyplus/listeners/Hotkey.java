package tw.momocraft.hotkeyplus.listeners;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import javafx.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import sun.security.krb5.Config;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;
import tw.momocraft.hotkeyplus.utils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class Hotkey implements Listener {
    // Map< playerName, List<KeyboardMap>>
    private final Map<String, Table<Integer, String, KeyboardMap>> customKeyMap = new HashMap<>();
    private final Map<String, Integer> triggerMap = new HashMap<>();
    private final Map<String, Long> trigMap = new HashMap<>();

    private final Map<String, Long> cooldownMap = new HashMap<>();


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        if (!ConfigHandler.getConfigPath().isDoubleShiftCustom()) {
            return;
        }
        if (!CorePlusAPI.getMySQLManager().isConnect(ConfigHandler.getPluginName())) {
            return;
        }
        String playerName = e.getPlayer().getName();
        ResultSet resultSet;
        try {
            resultSet = CorePlusAPI.getMySQLManager().getResultSet(ConfigHandler.getPlugin(), ConfigHandler.getPluginName(),
                    "SELECT page, key, commands FROM playerdata where player_name = '" + playerName + " ORDER BY key DESC'");
            Table<Integer, String, KeyboardMap> table = HashBasedTable.create();
            int page;
            String key;
            String commands;
            KeyboardMap keyboardMap;
            while (resultSet.next()) {
                keyboardMap = new KeyboardMap();
                page = resultSet.getInt("page");
                key = resultSet.getString("key");
                commands = resultSet.getString("commands");
                keyboardMap.setGroup(page + "-" + key);
                keyboardMap.setKey(key);
                keyboardMap.setPage(page);
                keyboardMap.setCommands(Arrays.asList(commands.split(", ")));
                table.put(page, key, keyboardMap);
            }
            customKeyMap.put(playerName, table);
        } catch (Exception ex) {
        }
    }

    /*
    Main: "%message%&8|| &fClose: Shift &8|| &f◀ ▶ Change Page"
          Separate: " &8|&r "
          Key: "&a%key%: &e"
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        if (e.isSneaking() || e.isCancelled()) {
            return;
        }
        Player player = e.getPlayer();
        if (!isTrigger(player)) {
            return;
        }
        addTrigger(player);
        Table<Integer, String, KeyboardMap> table = customKeyMap.get(player.getName());
        String messageFormat = ConfigHandler.getConfigPath().getDoubleShiftMenuMain();
        String displayList = "";
        String separateFormat = ConfigHandler.getConfigPath().getDoubleShiftMenuSeparate();
        String keyFormat = ConfigHandler.getConfigPath().getDoubleShiftMenuKey();
        for (KeyboardMap keyboardMap : table.values()) {
            displayList = displayList + keyFormat.replace("%key%", keyboardMap.getKey()) + keyboardMap.getDisplay() + separateFormat;
        }
        messageFormat = messageFormat.replace("%message%", displayList);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (!player.isSneaking() || e.isCancelled()) {
            return;
        }
        String playerName = player.getName();
        if (triggerMap.containsKey(playerName)) {
            String key = "f";
            KeyboardMap keyboardMap;
            try {
                int page = triggerMap.get(playerName);
                keyboardMap = customKeyMap.get(playerName).get(page, key);
            } catch (Exception ex) {
                keyboardMap = ConfigHandler.getConfigPath().getDoubleShiftProp().get(key);
            }
            if (keyboardMap == null) {
                return;
            }
            // Duplicate Event
            if (onDuplicate(player)) {
                return;
            }
            // Permission
            if (!CorePlusAPI.getPlayerManager().hasPerm(ConfigHandler.getPluginName(), player, "hotkeyplus.hotkey.doubleshift.*") &&
                    !CorePlusAPI.getPlayerManager().hasPerm(ConfigHandler.getPluginName(), player, "hotkeyplus.hotkey.keyboard." + keyboardMap.getGroup())) {
                return;
            }
            // Execute
            e.setCancelled(true);
            triggerMap.remove(playerName);
            if (!executeHotkey(player, keyboardMap.getCommands(), !keyboardMap.isCustom())) {
                CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                        "Double-Shift", player.getName(), "cooldown", "fail", keyboardMap.getGroup(), new Throwable().getStackTrace()[0]);
                return;
            }
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                    "Hotkey", playerName, "final", "sucess", keyboardMap.getGroup(), new Throwable().getStackTrace()[0]);
            return;
        }
        if (!ConfigHandler.getConfigPath().isShiftF()) {
            return;
        }
        KeyboardMap keyboardMap;
        try {
            int page = triggerMap.get(playerName);
            keyboardMap = customKeyMap.get(playerName).get(page, "f");
        } catch (Exception ex) {
            keyboardMap = ConfigHandler.getConfigPath().getShiftFKeyboardMap();
        }
        // Cancel Event.
        e.setCancelled(true);
        // Execute
        if (!executeHotkey(player, keyboardMap.getCommands(), !keyboardMap.isCustom())) {
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                    "Shift-F", player.getName(), "cooldown", "fail", keyboardMap.getGroup(), new Throwable().getStackTrace()[0]);
            return;
        }
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                "Shift-F", player.getName(), "cooldown", "success", keyboardMap.getGroup(), new Throwable().getStackTrace()[0]);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        if (!ConfigHandler.getConfigPath().isDoubleShift() || e.isCancelled()) {
            return;
        }
        Player player = e.getPlayer();
        String playerName = player.getName();
        if (!triggerMap.containsKey(playerName)) {
            return;
        }
        String key = String.valueOf(e.getNewSlot());
        KeyboardMap keyboardMap;
        try {
            int page = triggerMap.get(playerName);
            keyboardMap = customKeyMap.get(playerName).get(page, key);
        } catch (Exception ex) {
            keyboardMap = ConfigHandler.getConfigPath().getDoubleShiftProp().get(key);
        }
        if (keyboardMap == null) {
            return;
        }
        // Permission
        if (!CorePlusAPI.getPlayerManager().hasPerm(ConfigHandler.getPluginName(), player, "hotkeyplus.hotkey.doubleshift.*") &&
                !CorePlusAPI.getPlayerManager().hasPerm(ConfigHandler.getPluginName(), player, "hotkeyplus.hotkey.keyboard." + keyboardMap.getGroup())) {
            return;
        }
        // Duplicate Event
        if (onDuplicate(player)) {
            return;
        }
        // Cancel Event.
        e.setCancelled(true);
        triggerMap.remove(playerName);
        // Execute
        if (!executeHotkey(player, keyboardMap.getCommands(), !keyboardMap.isCustom())) {
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                    "Double-Shift", player.getName(), "cooldown", "fail", keyboardMap.getGroup(), new Throwable().getStackTrace()[0]);
            return;
        }
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(),
                "Hotkey", playerName, "final", "sucess", keyboardMap.getGroup(), new Throwable().getStackTrace()[0]);
    }


    private void addTrigger(Player player) {
        trigMap.put(player.getName(), System.currentTimeMillis());
    }

    private boolean isTrigger(Player player) {
        long playerCD = 0L;
        if (trigMap.containsKey(player.getName())) {
            playerCD = trigMap.get(player.getName());
        }
        return System.currentTimeMillis() - playerCD < ConfigHandler.getConfigPath().getDoubleShiftInterval();
    }

    private boolean onDuplicate(Player player) {
        long cooldown = 0L;
        if (cooldownMap.containsKey(player.getName())) {
            cooldown = cooldownMap.get(player.getName());
        }
        return System.currentTimeMillis() - cooldown < 50;
    }

    private boolean onCooldown(Player player) {
        long cooldown = 0L;
        if (cooldownMap.containsKey(player.getName())) {
            cooldown = cooldownMap.get(player.getName());
        }
        return System.currentTimeMillis() - cooldown < ConfigHandler.getConfigPath().getHotkeyCdInterval();
    }

    private void addCooldown(Player player) {
        cooldownMap.put(player.getWorld().getName() + "." + player.getName(), System.currentTimeMillis());
    }

    private boolean executeHotkey(Player player, List<String> commands, boolean placeholder) {
        if (ConfigHandler.getConfigPath().isHotkeyCooldown()) {
            if (onCooldown(player)) {
                if (ConfigHandler.getConfigPath().isHotkeyCdMsg()) {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.cooldown", player);
                }
                return false;
            }
            addCooldown(player);
        }
        CorePlusAPI.getCommandManager().executeCmdList(ConfigHandler.getPluginName(), player, commands, placeholder);
        return true;
    }
}
