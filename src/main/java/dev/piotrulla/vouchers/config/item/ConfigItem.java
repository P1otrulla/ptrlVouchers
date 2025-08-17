package dev.piotrulla.vouchers.config.item;

import static dev.piotrulla.vouchers.util.AdventureUtil.reset;
import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection;

import dev.piotrulla.vouchers.util.legacy.LegacyColorProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Exclude;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import panda.utilities.text.Formatter;

@Contextual
public class ConfigItem {

    @Exclude
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new LegacyColorProcessor())
            .build();

    @Exclude
    private static final Formatter EMPTY_FORMATTER = new Formatter();

    private String name = "<aqua> Kozacki dirt";
    private List<String> lore = new ArrayList<>();

    private Material material = Material.DIRT;

    private int amount = 1;

    private List<ItemFlag> flags = new ArrayList<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

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
                Component reset = reset(MINI_MESSAGE.deserialize(formatter.format(this.name)));
                meta.setDisplayName(legacySection().serialize(reset));
            }

            if (this.lore != null && !this.lore.isEmpty()) {
                meta.setLore(this.lore.stream()
                        .map(component -> {
                            Component reset = reset(MINI_MESSAGE.deserialize(formatter.format(component)));
                            return legacySection().serialize(reset);
                        })
                        .collect(Collectors.toList()));
            }

            if (!this.flags.isEmpty()) {
                meta.addItemFlags(this.flags.toArray(new ItemFlag[0]));
            }

            if (!this.enchantments.isEmpty()) {
                for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                    meta.addEnchant(entry.getKey(), entry.getValue(), true);
                }
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

        public ConfigItem.Builder flag(ItemFlag flag) {
            this.itemElement.flags.add(flag);
            return this;
        }

        public ConfigItem.Builder enchantment(Enchantment enchantment, int level) {
            this.itemElement.enchantments.put(enchantment, level);
            return this;
        }

        public ConfigItem build() {
            return this.itemElement;
        }
    }
}