package dev.piotrulla.vouchers.config.item;

import dev.piotrulla.vouchers.util.legacy.LegacyColorProcessor;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Exclude;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import panda.utilities.text.Formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.piotrulla.vouchers.util.AdventureUtil.reset;


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

    public String name() {
        return this.name;
    }

    public List<String> lore() {
        return this.lore;
    }

    public void updateLore(List<String> lore) {
        this.lore = lore;
    }

    public Material material() {
        return this.material;
    }

    public int amount() {
        return this.amount;
    }

    public List<ItemFlag> flags() {
        return this.flags;
    }

    public Map<Enchantment, Integer> enchantments() {
        return this.enchantments;
    }

    public ItemStack toItemStack() {
        return this.toItemStack(EMPTY_FORMATTER);
    }

    public ItemStack toItemStack(Formatter formatter) {
        ItemBuilder itemBuilder = ItemBuilder.from(this.material);

        if (this.name != null) {
            itemBuilder.name(reset(MINI_MESSAGE.deserialize(formatter.format(this.name))));
        }

        if (this.lore != null) {
            itemBuilder.lore(this.lore.stream()
                    .map(component -> reset(MINI_MESSAGE.deserialize(formatter.format(component))))
                    .collect(Collectors.toList()));
        }

        if (!this.flags.isEmpty()) {
            itemBuilder.flags(this.flags.toArray(new ItemFlag[0]));
        }

        if (!this.enchantments.isEmpty()) {
            itemBuilder.enchant(this.enchantments);
        }

        itemBuilder.amount(this.amount);

        return itemBuilder.build();
    }

    public static Builder builder() {
        return new Builder();
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

        public ConfigItem.Builder lore(List<String> lore) {
            this.itemElement.lore = lore;

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

        public ConfigItem.Builder flags(List<ItemFlag> flags) {
            this.itemElement.flags = flags;

            return this;
        }

        public ConfigItem.Builder flag(ItemFlag flag) {
            this.itemElement.flags.add(flag);

            return this;
        }

        public ConfigItem.Builder enchantments(Map<Enchantment, Integer> enchantments) {
            this.itemElement.enchantments = enchantments;

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
