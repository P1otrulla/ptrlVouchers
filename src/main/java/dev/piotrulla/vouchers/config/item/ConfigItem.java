package dev.piotrulla.vouchers.config.item;

import com.cryptomorin.xseries.XEnchantment;
import com.eternalcode.commons.adventure.AdventureUtil;
import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.vouchers.MiniMessageHolder;
import eu.okaeri.configs.annotation.Exclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigItem implements Serializable, MiniMessageHolder {

    @Exclude
    private static final Formatter EMPTY_FORMATTER = new Formatter();

    private final List<ItemFlag> flags = new ArrayList<>();
    private final Map<XEnchantment, Integer> enchantments = new HashMap<>();
    private String name = "<aqua> Kozacki dirt";
    private List<String> lore = new ArrayList<>();
    private Material material = Material.DIRT;
    private int amount = 1;
    private Integer customModelData = 0;

    public static Builder builder() {
        return new Builder();
    }

    public String name() {
        return this.name;
    }

    public ItemStack toItemStack() {
        return this.toItemStack(EMPTY_FORMATTER);
    }

    public ItemStack toItemStack(Formatter formatter) {
        ItemStack itemStack = new ItemStack(this.material, this.amount);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            if (this.name != null) {
                meta.displayName(AdventureUtil.resetItalic(MINI_MESSAGE.deserialize(formatter.format(this.name))));
            }

            if (this.lore != null && !this.lore.isEmpty()) {
                meta.lore(this.lore.stream()
                        .map(component -> AdventureUtil.resetItalic(MINI_MESSAGE.deserialize(formatter.format(component))))
                        .collect(Collectors.toList()));
            }

            if (!this.flags.isEmpty()) {
                meta.addItemFlags(this.flags.toArray(new ItemFlag[0]));
            }

            if (!this.enchantments.isEmpty()) {
                for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
                    XEnchantment xEnchantment = entry.getKey();
                    Enchantment bukkitEnchantment = xEnchantment.get();
                    if (bukkitEnchantment != null) {
                        meta.addEnchant(bukkitEnchantment, entry.getValue(), true);
                    }
                }
            }

            if (this.customModelData != null && this.customModelData > 0) {
                meta.setCustomModelData(this.customModelData);
            }

            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public static class Builder {

        private final ConfigItem itemElement;

        public Builder() {
            this.itemElement = new ConfigItem();
        }

        public ConfigItem.Builder name(String name) {
            this.itemElement.name = name;
            return this;
        }

        public ConfigItem.Builder lore(String... lore) {
            this.itemElement.lore = List.of(lore);
            return this;
        }

        public ConfigItem.Builder material(Material material) {
            this.itemElement.material = material;
            return this;
        }

        public ConfigItem.Builder amount(int amount) {
            this.itemElement.amount = amount;
            return this;
        }

        public ConfigItem.Builder customModelData(int customModelData) {
            this.itemElement.customModelData = customModelData;
            return this;
        }

        public ConfigItem.Builder flag(ItemFlag flag) {
            this.itemElement.flags.add(flag);
            return this;
        }

        public ConfigItem.Builder enchantment(XEnchantment enchantment, int level) {
            this.itemElement.enchantments.put(enchantment, level);
            return this;
        }

        public ConfigItem build() {
            return this.itemElement;
        }
    }
}