package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.config.item.ConfigItem;
import net.dzikoysk.cdn.entity.Contextual;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Contextual
public class Voucher {

    private String name = "default";
    private ConfigItem item = ConfigItem.builder()
            .material(Material.PAPER)
            .name("<gold> Voucher: &fdefault")
            .lore(
                    "&7Zawiera:",
                    "&7- &eRanga VIP na 7dni",
                    "&7- &21000$",
                    "", "&7Kliknij PPM aby odebrać!"
            )
            .enchantment(Enchantment.LUCK, 1)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .build();

    private Map<Integer, ConfigItem> items = Map.of(1, new ConfigItem());
    private List<String> commands = Collections.singletonList("lp user {PLAYER} parent addtemp vip 7d");
    private double money = 1000.0;

    public Voucher(String name, ConfigItem item, Map<Integer, ConfigItem> items, List<String> commands, double money) {
        this.name = name;
        this.item = item;
        this.items = items;
        this.commands = commands;
        this.money = money;
    }

    public Voucher() {
    }

    public String name() {
        return this.name;
    }

    public ItemStack item() {
        return this.item.toItemStack();
    }

    public List<ItemStack> items() {
        return this.items.values()
                .stream()
                .map(ConfigItem::toItemStack)
                .toList();
    }

    public List<String> commands() {
        return Collections.unmodifiableList(this.commands);
    }

    public double money() {
        return this.money;
    }
}
