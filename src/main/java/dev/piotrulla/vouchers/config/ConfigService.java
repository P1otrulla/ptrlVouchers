package dev.piotrulla.vouchers.config;

import dev.piotrulla.vouchers.config.composer.EnchantmentComposer;
import dev.piotrulla.vouchers.notification.Notification;
import dev.piotrulla.vouchers.notification.NotificationComposer;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigService {

    private final Cdn cdn = CdnFactory
            .createYamlLike()
            .getSettings()
            .withComposer(Notification.class, new NotificationComposer())
            .withComposer(Enchantment.class, new EnchantmentComposer())
            .withMemberResolver(Visibility.PRIVATE)
            .build();

    private final Set<ReloadableConfig> configs = new HashSet<>();
    private final File dataFolder;

    public ConfigService(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public <T extends ReloadableConfig> T load(T config) {
        cdn.load(config.resource(this.dataFolder), config)
                .orThrow(RuntimeException::new);

        cdn.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);

        this.configs.add(config);

        return config;
    }

    public <T extends ReloadableConfig> T save(T config) {
        cdn.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);

        this.configs.add(config);

        return config;
    }

    public void reload() {
        for (ReloadableConfig config : this.configs) {
            load(config);
        }
    }
}