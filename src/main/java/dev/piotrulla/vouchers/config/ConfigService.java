package dev.piotrulla.vouchers.config;

import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.okaeri.MultificationSerdesPack;
import dev.piotrulla.vouchers.config.serializer.EnchantmentSerializer;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.enchantments.Enchantment;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

public class ConfigService {

    private final Set<OkaeriConfig> configs = new HashSet<>();

    private final NoticeResolverRegistry noticeResolverRegistry;

    public ConfigService(NoticeResolverRegistry noticeResolverRegistry) {
        this.noticeResolverRegistry = noticeResolverRegistry;
    }

    public <T extends OkaeriConfig> T create(Class<T> config, File file) {
        T configFile = ConfigManager.create(config);

        YamlSnakeYamlConfigurer yamlConfigurer = new YamlSnakeYamlConfigurer(this.createYaml());

        configFile
                .withConfigurer(yamlConfigurer, new SerdesCommons())
                .withConfigurer(yamlConfigurer, new SerdesBukkit())
                .withSerdesPack(registry -> registry.register(new EnchantmentSerializer()))
                .withConfigurer(yamlConfigurer, new MultificationSerdesPack(this.noticeResolverRegistry))
                .withBindFile(file)
                .withRemoveOrphans(true)
                .saveDefaults()
                .load(true);

        this.configs.add(configFile);

        return configFile;
    }

    private Yaml createYaml() {
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(loaderOptions);

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
        dumperOptions.setIndent(2);
        dumperOptions.setSplitLines(false);

        Representer representer = new CustomSnakeYamlRepresenter(dumperOptions);
        Resolver resolver = new Resolver();

        return new Yaml(constructor, representer, dumperOptions, loaderOptions, resolver);
    }

    public void reload() {
        for (OkaeriConfig config : this.configs) {
            config.load();
        }
    }

    public void save(OkaeriConfig config) {
        config.save();
    }
}
