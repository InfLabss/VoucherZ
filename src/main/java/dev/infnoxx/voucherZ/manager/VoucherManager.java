package dev.infnoxx.voucherZ.manager;

import dev.infnoxx.voucherZ.Voucher;
import dev.infnoxx.voucherZ.VoucherZ;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherManager {

    private final VoucherZ plugin;
    private final Map<String, Voucher> vouchers = new HashMap<>();
    private final NamespacedKey voucherKeyPDC;
    private final boolean placeholderAPIEnabled;

    public VoucherManager(VoucherZ plugin) {
        this.plugin = plugin;
        this.voucherKeyPDC = new NamespacedKey(plugin, "voucher_key");
        this.placeholderAPIEnabled = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public void loadVouchers() {
        vouchers.clear();
        FileConfiguration config = plugin.getConfig();
        for (String key : config.getConfigurationSection("vouchers").getKeys(false)) {
            String material = config.getString("vouchers." + key + ".material");
            String name = config.getString("vouchers." + key + ".name");
            List<String> lore = config.getStringList("vouchers." + key + ".lore");
            String command = config.getString("vouchers." + key + ".command");
            String permission = config.getString("vouchers." + key + ".permission", "");

            String soundStr = config.getString("vouchers." + key + ".sound", null);
            String particleStr = config.getString("vouchers." + key + ".particles", null);

            Sound sound = (soundStr != null) ? Sound.valueOf(soundStr) : null;
            Particle particle = (particleStr != null) ? Particle.valueOf(particleStr) : null;

            List<String> redeemMessage = config.getStringList("vouchers." + key + ".redeem-message");

            vouchers.put(key, new Voucher(Material.valueOf(material), name, lore, command, permission, sound, particle, redeemMessage));
        }
    }

    public boolean giveVoucher(Player player, String voucherKey) {
        Voucher voucher = vouchers.get(voucherKey);

        if (voucher == null) {
            player.sendMessage(ChatColor.RED + "Voucher not found");
            return false;
        }

        if (!voucher.canClaim(player)) {
            player.sendMessage(ChatColor.RED + "You don't have permissions to claim this voucher.");
            return false;
        }

        ItemStack item = new ItemStack(voucher.getMaterial());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', voucher.getName()));
            List<String> coloredLore = new ArrayList<>();
            for (String line : voucher.getLore()) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(coloredLore);

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(voucherKeyPDC, PersistentDataType.STRING, voucherKey);

            item.setItemMeta(meta);
        }

        player.getInventory().addItem(item);
        player.sendMessage(ChatColor.GREEN + "You have received a voucher: " + ChatColor.translateAlternateColorCodes('&', voucher.getName()));

        return true;
    }

    public void redeemVoucher(Player player, Voucher voucher) {
        String command = voucher.getCommand().replace("%player%", player.getName());

        if (placeholderAPIEnabled) {
            command = PlaceholderAPI.setPlaceholders(player, command);
        }

        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), command);

        if (voucher.getSound() != null) {
            player.playSound(player.getLocation(), voucher.getSound(), 1.0f, 1.0f);
        }

        if (voucher.getParticle() != null) {
            new BukkitRunnable() {
                private int ticks = 0;

                @Override
                public void run() {
                    if (ticks >= 60) {
                        cancel();
                        return;
                    }
                    player.getWorld().spawnParticle(voucher.getParticle(), player.getLocation(), 50, 0.5, 0.5, 0.5, 0.01);
                    ticks++;
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }

        for (String message : voucher.getRedeemMessage()) {
            String formattedMessage = ChatColor.translateAlternateColorCodes('&', message).replace("%voucher%", voucher.getName());

            if (placeholderAPIEnabled) {
                formattedMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
            }

            player.sendMessage(formattedMessage);
        }
    }

    public boolean voucherExists(String voucherKey) {
        return vouchers.containsKey(voucherKey);
    }

    public Voucher getVoucherFromItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();

            if (data.has(voucherKeyPDC, PersistentDataType.STRING)) {
                String voucherKey = data.get(voucherKeyPDC, PersistentDataType.STRING);
                return vouchers.get(voucherKey);
            }
        }

        return null;
    }

    public List<String> getVoucherKeys() {
        return new ArrayList<>(vouchers.keySet());
    }

    private List<String> translateLore(List<String> lore) {
        List<String> translatedLore = new ArrayList<>();
        for (String line : lore) {
            translatedLore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return translatedLore;
    }
}
