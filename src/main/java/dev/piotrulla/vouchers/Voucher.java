package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.config.item.ConfigItem;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class Voucher implements Serializable {

    private String name;
    private ConfigItem item;
    private Map<Integer, ConfigItem> items;
    private List<String> commands;
    private double money;

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
