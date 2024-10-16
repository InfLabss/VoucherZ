package dev.infnoxx.voucherZ.commands;

import dev.infnoxx.voucherZ.VoucherZ;
import dev.infnoxx.voucherZ.manager.VoucherManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoucherCommand implements TabExecutor {

    private final VoucherZ plugin;
    private final VoucherManager voucherManager;
    private final MiniMessage miniMessage;
    private static final String PREFIX = "<gray>[<gold>VoucherZ</gold>]</gray> ";

    public VoucherCommand(VoucherZ plugin, VoucherManager voucherManager) {
        this.plugin = plugin;
        this.voucherManager = voucherManager;
        this.miniMessage = MiniMessage.miniMessage();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(miniMessage.deserialize(PREFIX + "<red>Usage: /voucher give <player> <voucher> or /voucher reload"));
            return true;
        }

        // Reload command
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("voucher.reload")) {
                sender.sendMessage(miniMessage.deserialize(PREFIX + "<red>You do not have permission to execute this command."));
                return true;
            }
            plugin.reloadVoucherConfig();
            sender.sendMessage(miniMessage.deserialize(PREFIX + "<green>Config has been reloaded."));
            return true;
        }

        // Give voucher command
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 3) {
                sender.sendMessage(miniMessage.deserialize(PREFIX + "<red>Usage: /voucher give <player> <voucher>"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(miniMessage.deserialize(PREFIX + "<red>Player not found!"));
                return true;
            }

            String voucherKey = args[2];
            if (!voucherManager.voucherExists(voucherKey)) {
                sender.sendMessage(miniMessage.deserialize(PREFIX + "<red>Voucher does not exist."));
                return true;
            }

            if (voucherManager.giveVoucher(target, voucherKey)) {
                sender.sendMessage(miniMessage.deserialize(PREFIX + "<green>Voucher given to " + target.getName() + " successfully!"));
            } else {
                sender.sendMessage(miniMessage.deserialize(PREFIX + "<red>Failed to give the voucher to " + target.getName()));
            }

            return true;
        }

        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("give");
            suggestions.add("reload");
            StringUtil.copyPartialMatches(args[0], suggestions, completions);
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
            StringUtil.copyPartialMatches(args[1], suggestions, completions);
        }

        else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            suggestions.addAll(voucherManager.getVoucherKeys());
            StringUtil.copyPartialMatches(args[2], suggestions, completions);
        }

        Collections.sort(completions);

        return completions;
    }
}
