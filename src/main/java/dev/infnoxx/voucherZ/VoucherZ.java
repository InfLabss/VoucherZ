package dev.infnoxx.voucherZ;

import dev.infnoxx.voucherZ.commands.VoucherCommand;
import dev.infnoxx.voucherZ.listeners.PlayerInteractListener;
import dev.infnoxx.voucherZ.manager.VoucherManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VoucherZ extends JavaPlugin {

    private VoucherManager voucherManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        voucherManager = new VoucherManager(this);
        voucherManager.loadVouchers();

        VoucherCommand voucherCommand = new VoucherCommand(this, voucherManager);
        Objects.requireNonNull(getCommand("voucher")).setExecutor(voucherCommand);
        Objects.requireNonNull(getCommand("voucher")).setTabCompleter(voucherCommand);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractListener(voucherManager), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadVoucherConfig() {
        reloadConfig();
        voucherManager.loadVouchers();
    }
}
