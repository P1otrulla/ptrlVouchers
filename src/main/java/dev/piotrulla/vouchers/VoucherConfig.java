package dev.piotrulla.vouchers;

import com.cryptomorin.xseries.XEnchantment;
import com.eternalcode.multification.notice.Notice;
import dev.piotrulla.vouchers.config.item.ConfigItem;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class VoucherConfig extends OkaeriConfig {

    public Map<String, Voucher> vouchers = Map.of(
            "vip7d", new Voucher(
                    "vip7d",
                    ConfigItem.builder()
                            .material(Material.PAPER)
                            .name("<gold>Voucher: &fVIP 7d")
                            .lore("&7Contains:", "&7- &eVIP rank for 7 days", "&7- &21000$")
                            .enchantment(XEnchantment.AQUA_AFFINITY, 1)
                            .flag(ItemFlag.HIDE_ENCHANTS)
                            .customModelData(1001)
                            .build(),
                    Map.of(),
                    List.of("lp user {PLAYER} parent addtemp vip 7d"),
                    1000.0
            ),
            "money1k", new Voucher(
                    "money1k",
                    ConfigItem.builder()
                            .material(Material.GOLD_INGOT)
                            .name("<gold>Voucher: &f1000$")
                            .lore("&7Contains:", "&7- &21000$")
                            .build(),
                    Map.of(),
                    List.of(),
                    1000.0
            ),
            "itemsPack", new Voucher(
                    "itemsPack",
                    ConfigItem.builder()
                            .material(Material.CHEST)
                            .name("<gold>Voucher: &fStarter Pack")
                            .lore("&7Contains:", "&7- &eStarter items")
                            .build(),
                    Map.of(
                            1, ConfigItem.builder().material(Material.DIAMOND_SWORD).name("&bStarter Sword").build(),
                            2, ConfigItem.builder().material(Material.DIAMOND_PICKAXE).name("&bStarter Pickaxe").build()
                    ),
                    List.of(),
                    0.0
            )
    );

    @Comment({"", "# Messages"})
    public Notice invalidAmount = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <color:#C21008>Invalid amount!");
    public Notice playerNotFound = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <color:#C21008>Player not found!");
    public Notice noPermission = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <color:#C21008>You don't have permission! &f({PERMISSION})");
    public Notice configReloaded = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Config reloaded!");
    public Notice playerOnly = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>This command can only be used by players!");
    public Notice correctUsage = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Correct usage: <white{USAGE}");
    public Notice correctUsageHeader = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Correct usage:");
    public Notice correctUsageEntry = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>{USAGE}");

    public Notice vouchersList = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Available vouchers: &7{VOUCHERS}");
    public Notice voucherNotFound = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <color:#C21008>Voucher not found!");
    public Notice voucherReceived = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>You have received a voucher: &7{VOUCHER}");
    public Notice voucherGiven = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>You have given the voucher: &f{VOUCHER} <gray>to</gray> &6{PLAYER}");
    public Notice voucherUsed = Notice.chat("<b><gradient:#F0C98C:#FFE4BA:#F0C98C>Voucher</gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>You have used the voucher: &7{VOUCHER}");
}
