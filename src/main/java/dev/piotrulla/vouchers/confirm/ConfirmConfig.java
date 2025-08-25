package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.config.item.ConfigItem;
import eu.okaeri.configs.OkaeriConfig;
import java.util.List;
import org.bukkit.Material;

public class ConfirmConfig extends OkaeriConfig {

    public boolean enabled = true;

    public ConfigItem yesItem = ConfigItem.builder()
            .material(Material.GREEN_STAINED_GLASS_PANE)
            .name("<green>Confirm")
            .build();

    public List<Integer> yesSlots = List.of(11);

    public ConfigItem noItem = ConfigItem.builder()
            .material(Material.RED_STAINED_GLASS_PANE)
            .name("<red>Cancel")
            .build();

    public List<Integer> noSlots = List.of(15);

    public String title = "<gold>Confirm Voucher Redemption";
    public int size = 27;

    public boolean cloneItem = true;
    public int cloneItemSlot = 13;

}
