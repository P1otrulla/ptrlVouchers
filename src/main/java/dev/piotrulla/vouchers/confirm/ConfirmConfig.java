package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.config.item.ConfigItem;
import eu.okaeri.configs.OkaeriConfig;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfirmConfig extends OkaeriConfig {

    public boolean enabled = true;

    public ItemStack yesItem = ConfigItem.builder()
            .material(Material.GREEN_STAINED_GLASS_PANE)
            .name("<green>Confirm")
            .build()
            .toItemStack();

    public List<Integer> yesSlots = List.of(11);

    public ItemStack noItem = ConfigItem.builder()
            .material(Material.RED_STAINED_GLASS_PANE)
            .name("<red>Cancel")
            .build()
            .toItemStack();

    public List<Integer> noSlots = List.of(15);

    public String title = "<gold>Confirm Voucher Redemption";
    public int size = 27;

    public boolean cloneItem = true;
    public int cloneItemSlot = 13;

}
