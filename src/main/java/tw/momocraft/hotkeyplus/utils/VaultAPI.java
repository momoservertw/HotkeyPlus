package tw.momocraft.hotkeyplus.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import tw.momocraft.hotkeyplus.HotkeyPlus;
import tw.momocraft.hotkeyplus.handlers.ServerHandler;

public class VaultAPI {
    private Economy econ = null;
    private Permission perms = null;

    VaultAPI() {
        if (!this.setupEconomy()) {
            ServerHandler.sendErrorMessage("&cCan not find the Economy plugin.");
        }
        if (!this.setupPermissions()) {
            ServerHandler.sendErrorMessage("&cCan not find the Permission plugin.");
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = HotkeyPlus.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.econ = rsp.getProvider();
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = HotkeyPlus.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        this.perms = rsp.getProvider();
        return true;
    }

    public Economy getEconomy() {
        return this.econ;
    }

    public Permission getPermissions() {
        return this.perms;
    }

    public double getBalance(OfflinePlayer offlinePlayer) {
        return econ.getBalance(offlinePlayer);
    }
}