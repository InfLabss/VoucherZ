
**VoucherZ**  
A Minecraft Plugin for Creating Redeemable Vouchers


### Overview:
VoucherZ is a Minecraft plugin that allows server administrators to create redeemable vouchers for their players. These vouchers can be used to grant players various rewards, such as ranks, items, or permissions.

---

### Features:
- Create custom vouchers with unique names, descriptions, and rewards.
- Set permissions for each voucher to control who can use them.
- Give vouchers to players using the `/voucher give` command.
- Supports various reward types, including commands, items, and permissions.

---

### Installation:
1. Download the latest version of VoucherZ from the releases page.
2. Place the `VoucherZ.jar` file in your server's plugins directory.
3. Restart your server to load the plugin.
4. Configure the plugin by editing the `config.yml` file in the VoucherZ directory.

---

### Configuration:
The `config.yml` file contains settings for the plugin, including the default permission and voucher configurations. You can add, remove, or modify vouchers as needed.

**Example Voucher Configuration:**
```yaml
vouchers:
  DragonLordVoucher:
    name: "&4&lDragon &6&lLord &7&lVoucher" 
    lore:
      - "&7Right-click to unleash the &4fire &7of the ancient dragons."
      - " "
      - "&6Grants you the &4&lDragon Lord &6Rank!"
      - " "
      - "&8&oThe ground trembles as your power awakens..."
      - "&8&oA fiery force burns within, waiting to be unleashed."
      - " "
      - "&7Master the flames of the &4&lancient &7dragons."
      - "&7Command power feared by all who stand in your way."
      - " "
      - "&6&lWill you rise as the true Dragon Lord?"
    material: "BLAZE_ROD"  # Blaze rod to symbolize fire and power
    command: "lp user %player% parent set dragonlord"  # Command for Dragon Lord rank
    permission: "voucher.dragonlord"
    sound: "ENTITY_ENDER_DRAGON_GROWL"  # Deep dragon growl sound effect
    particles: "FLAME"  # Flaming particles for a fiery, powerful atmosphere
    redeem-message:
      - "&8&m----------------------------"
      - " "
      - "&4&l✦ &6The ancient flames bow to you, &4&oDragon Lord."
      - " "
      - "&7You now wield the fiery strength of the dragons."
      - "&7The skies tremble before your power."
      - " "
      - "&8&oThe flames whisper your legacy... &7&oWield it with honor."
      - " "
      - "&8&m----------------------------"
```

---

### Commands:
- `/voucher give <player> <voucher>`: Give a voucher to a player.
- `/voucher reload`: Reload the voucher configuration.

---

### Permissions:
- `voucher.use`: Allows players to use the `/voucher` command.
- `voucher.reload`: Allows players to reload the voucher configuration.
- `voucher.give`: Allows players to give vouchers to others.

---

### API:
VoucherZ provides an API for developers to create custom voucher types and integrate with other plugins. See the API documentation for more information.

---

### Contributing:
Contributions are welcome! If you'd like to report a bug or suggest a feature, please open an issue on the issue tracker. If you'd like to contribute code, fork the repository and submit a pull request.

---

### License:
VoucherZ is licensed under the MIT License.

---

### Building:
To build VoucherZ, you'll need to have Maven installed on your system. You can build the plugin using the following command:

```bash
mvn clean package
```

This will create a `VoucherZ.jar` file in the target directory, which you can then use on your Minecraft server.

---

### Dependencies:
VoucherZ depends on the following libraries:
- `paper-api`: The PaperMC API for Minecraft servers.
- `mini-message`: A library for parsing and rendering Minecraft chat messages.

---

### Credits:
VoucherZ was created by Infnoxx.

---
