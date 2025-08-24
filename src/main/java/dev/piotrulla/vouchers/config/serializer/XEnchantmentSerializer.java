package dev.piotrulla.vouchers.config.serializer;

import com.cryptomorin.xseries.XEnchantment;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import java.util.Optional;

public class XEnchantmentSerializer implements ObjectSerializer<XEnchantment> {

    @Override
    public boolean supports(Class<? super XEnchantment> type) {
        return XEnchantment.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(
        XEnchantment object,
        SerializationData data,
        GenericsDeclaration generics
    ) {

        data.setValue(object.name());
    }

    @Override
    public XEnchantment deserialize(
        DeserializationData data,
        GenericsDeclaration generics
    ) {

        String enchantmentName = data.getValue(String.class);

        Optional<XEnchantment> enchantment = XEnchantment.of(enchantmentName);

        if (enchantment.isPresent()) {
            return enchantment.get();
        }

        throw new IllegalArgumentException("Nieznany enchantment: " + enchantmentName);
    }
}
