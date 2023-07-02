package dev.piotrulla.vouchers.config.composer;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import panda.std.Result;

public class EnchantmentComposer implements SimpleComposer<Enchantment> {

    @Override
    public Result<Enchantment, Exception> deserialize(String source) {
        return Result.ok(Enchantment.getByKey(NamespacedKey.minecraft(source)));
    }

    @Override
    public Result<String, Exception> serialize(Enchantment entity) {
        return Result.ok(entity.getKey().getKey());
    }
}
