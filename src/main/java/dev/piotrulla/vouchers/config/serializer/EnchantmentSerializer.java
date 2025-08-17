package dev.piotrulla.vouchers.config.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

public class EnchantmentSerializer implements ObjectSerializer<Enchantment> {

    @Override
    public boolean supports(Class<? super Enchantment> type) {
        return Enchantment.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(Enchantment enchantment, SerializationData data, GenericsDeclaration generics) {
        data.setValue(enchantment.getKey().toString());
    }

    @Override
    public Enchantment deserialize(DeserializationData data, GenericsDeclaration generics) {
        NamespacedKey key = NamespacedKey.fromString(data.getValue(String.class));
        return key != null ? RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key) : null;
    }
}