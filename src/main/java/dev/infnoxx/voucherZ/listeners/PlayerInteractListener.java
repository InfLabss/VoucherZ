package dev.infnoxx.voucherZ.listeners;

import dev.infnoxx.voucherZ.Voucher;
import dev.infnoxx.voucherZ.manager.VoucherManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final VoucherManager voucherManager;
    private final MiniMessage miniMessage;

    public PlayerInteractListener(VoucherManager voucherManager) {
        this.voucherManager = voucherManager;
        this.miniMessage = MiniMessage.miniMessage();
    }

    @EventHandler
    public void onPlayerUseVoucher(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        Voucher voucher = voucherManager.getVoucherFromItem(item);
        if (voucher != null) {
            Player player = event.getPlayer();

            if (voucher.canClaim(player)) {
                voucherManager.redeemVoucher(player, voucher);

                item.setAmount(item.getAmount() - 1);

                event.setCancelled(true);
            } else {
                player.sendMessage(miniMessage.deserialize("<red>You do not have permission to use this voucher."));
            }
        }
    }
}
