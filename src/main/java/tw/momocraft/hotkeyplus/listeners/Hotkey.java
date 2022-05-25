package tw.momocraft.hotkeyplus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.hotkeyplus.handlers.ConfigHandler;
import tw.momocraft.hotkeyplus.utils.HotkeyMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotkey implements Listener {

    private final Map<String, Long> startMap = new HashMap<>();
    private final Map<String, Integer> lastKeyMap = new HashMap<>();
    private final Map<String, Long> cdMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        if (!ConfigHandler.getConfigPath().isHotkey())
            return;
        if (!e.isSneaking())
            return;
        Player player = e.getPlayer();
        String playerName = player.getName();
        if (isStarting(player.getName())) {
            lastKeyMap.put(playerName, player.getInventory().getHeldItemSlot());
            player.getInventory().setHeldItemSlot(8);
            if (ConfigHandler.getConfigPath().isHotkeyHelp()) {
                String command = ConfigHandler.getConfigPath().getHotkeyHelpCmd();
                command = CorePlusAPI.getMsg().transHolder(ConfigHandler.getPluginName(),
                        player, command);
                CorePlusAPI.getCmd().sendCmd(ConfigHandler.getPluginName(), player, command);
            }
            return;
        }
        addStart(player.getName());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
        if (!ConfigHandler.getConfigPath().isHotkey())
            return;
        Player player = e.getPlayer();
        if (!player.isSneaking())
            return;
        if (player.getInventory().getHeldItemSlot() != 8)
            return;
        executeHotkey(player, "F");
        player.setHealthScale(lastKeyMap.get(player.getName()));
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        if (!ConfigHandler.getConfigPath().isHotkey())
            return;
        if (e.getPreviousSlot() != 8 || e.getNewSlot() == 8)
            return;
        Player player = e.getPlayer();
        if (!player.isSneaking())
            return;
        executeHotkey(player, String.valueOf(e.getNewSlot() + 1));
        player.setHealthScale(lastKeyMap.get(player.getName()));
    }

    private void executeHotkey(Player player, String key) {
        String playerName = player.getName();
        HotkeyMap hotkeyMap = ConfigHandler.getConfigPath().getHotkeyProp().get("Shift-" + key);
        if (hotkeyMap == null)
            return;
        // Permission
        if (!CorePlusAPI.getPlayer().hasPerm(player, "hotkeyplus.hotkey.*") &&
                !CorePlusAPI.getPlayer().hasPerm(player, "hotkeyplus.hotkey." + key)) {
            CorePlusAPI.getMsg().sendDetailMsg(ConfigHandler.isDebug(), ConfigHandler.getPluginName(),
                    "Hotkey", playerName, "permission", "fail", "key=" + key,
                    new Throwable().getStackTrace()[0]);
            return;
        }
        // Check cooldown.
        if (ConfigHandler.getConfigPath().isHotkeyCooldown()) {
            if (onCD(playerName)) {
                if (ConfigHandler.getConfigPath().isHotkeyCdMsg())
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(), "Message.cooldown", player);
                CorePlusAPI.getMsg().sendDetailMsg(ConfigHandler.isDebug(), ConfigHandler.getPluginName(),
                        "Hotkey", playerName, "cooldown", "fail", "key=" + key,
                        new Throwable().getStackTrace()[0]);
                return;
            }
            addCD(playerName);
        }
        List<String> commands = hotkeyMap.getCommands();
        commands = CorePlusAPI.getMsg().transHolder(ConfigHandler.getPluginName(), player, commands);
        CorePlusAPI.getCmd().sendCmd(ConfigHandler.getPluginName(), player, commands);
        CorePlusAPI.getMsg().sendDetailMsg(ConfigHandler.isDebug(), ConfigHandler.getPluginName(),
                "Hotkey", playerName, "final", "sucess", hotkeyMap.getGroupName(),
                new Throwable().getStackTrace()[0]);
    }

    private boolean isStarting(String playerName) {
        long cd = 0L;
        if (startMap.containsKey(playerName))
            cd = startMap.get(playerName);
        return (System.currentTimeMillis() - cd) < 1000;
    }

    private void addStart(String playerName) {
        startMap.put(playerName, System.currentTimeMillis());
    }

    private boolean onCD(String playerName) {
        long cd = 0L;
        if (cdMap.containsKey(playerName))
            cd = cdMap.get(playerName);
        return (System.currentTimeMillis() - cd) < ConfigHandler.getConfigPath().getHotkeyCdInterval();
    }

    private void addCD(String playerName) {
        cdMap.put(playerName, System.currentTimeMillis());
    }
}
