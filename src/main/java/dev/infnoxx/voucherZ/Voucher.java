package dev.infnoxx.voucherZ;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class Voucher {
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final String command;
    private final String permission;
    private final Sound sound;
    private final Particle particle;
    private final List<String> redeemMessage; // New field for redeem message

    public Voucher(Material material, String name, List<String> lore, String command, String permission, Sound sound, Particle particle, List<String> redeemMessage) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.command = command;
        this.permission = permission;
        this.sound = sound;
        this.particle = particle;
        this.redeemMessage = redeemMessage;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public Sound getSound() {
        return sound;
    }

    public Particle getParticle() {
        return particle;
    }

    public List<String> getRedeemMessage() {
        return redeemMessage;
    }

    public boolean canClaim(Player player) {
        return permission == null || permission.isEmpty() || player.hasPermission(permission);
    }
}
