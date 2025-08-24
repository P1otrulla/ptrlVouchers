package dev.piotrulla.vouchers;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.actionbar.ActionbarResolver;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarResolver;
import com.eternalcode.multification.notice.resolver.bossbar.BossBarService;
import com.eternalcode.multification.notice.resolver.chat.ChatResolver;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventureResolver;
import com.eternalcode.multification.notice.resolver.title.SubtitleResolver;
import com.eternalcode.multification.notice.resolver.title.SubtitleWithEmptyTitleResolver;
import com.eternalcode.multification.notice.resolver.title.TimesResolver;
import com.eternalcode.multification.notice.resolver.title.TitleHideResolver;
import com.eternalcode.multification.notice.resolver.title.TitleResolver;
import com.eternalcode.multification.notice.resolver.title.TitleWithEmptySubtitleResolver;
import dev.piotrulla.vouchers.bridge.BridgeService;
import dev.piotrulla.vouchers.bridge.vault.MoneyService;
import dev.piotrulla.vouchers.config.ConfigService;
import dev.piotrulla.vouchers.litecommands.InvalidUsageHandlerImpl;
import dev.piotrulla.vouchers.litecommands.MissingPermissionHandlerImpl;
import dev.piotrulla.vouchers.litecommands.VoucherArgument;
import dev.piotrulla.vouchers.notification.NoticeService;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.message.LiteMessages;
import java.io.File;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoucherPlugin extends JavaPlugin {

    private MoneyService moneyService;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        Server server = this.getServer();
        MiniMessage miniMessage = MiniMessage.builder()
                .postProcessor(new AdventureLegacyColorPostProcessor())
                .preProcessor(new AdventureLegacyColorPreProcessor())
                .build();

        NoticeResolverRegistry noticeResolverRegistry = new NoticeResolverRegistry()
                .registerResolver(new ChatResolver())
                .registerResolver(new TitleResolver())
                .registerResolver(new SubtitleResolver())
                .registerResolver(new TitleWithEmptySubtitleResolver())
                .registerResolver(new SubtitleWithEmptyTitleResolver())
                .registerResolver(new TimesResolver())
                .registerResolver(new TitleHideResolver())
                .registerResolver(new SoundAdventureResolver())
                .registerResolver(new ActionbarResolver())
                .registerResolver(new BossBarResolver(new BossBarService()));
        ConfigService configService = new ConfigService(noticeResolverRegistry);
        VoucherConfig voucherConfig = configService.create(
                VoucherConfig.class,
                new File(this.getDataFolder(), "config.yml")
        );
        NoticeService noticeService = new NoticeService(voucherConfig, miniMessage);
        configService = new ConfigService(noticeResolverRegistry);

        PluginManager pluginManager = server.getPluginManager();
        BridgeService bridgeService = new BridgeService(
                server.getServicesManager(),
                pluginManager,
                this.moneyService
        );
        bridgeService.init();

        this.moneyService = bridgeService.borrowMoneyService();

        VoucherItemService voucherItemService = new VoucherItemService(this.moneyService, this);

        VoucherService voucherService = new VoucherService(voucherConfig, voucherItemService);

        pluginManager.registerEvents(
                new VoucherController(
                        voucherService,
                        voucherItemService,
                        noticeService
                ),
                this
        );

        this.liteCommands = LiteBukkitFactory.builder("vouchers", this, server)
                .extension(new LiteAdventureExtension<>(), extension -> extension.serializer(miniMessage))

                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> voucherConfig.playerNotFound)
                .message(LiteMessages.MISSING_PERMISSIONS, input -> voucherConfig.noPermission)
                .message(LiteBukkitMessages.PLAYER_ONLY, input -> voucherConfig.playerOnly)

                .invalidUsage(new InvalidUsageHandlerImpl(noticeService))
                .missingPermission(new MissingPermissionHandlerImpl(noticeService))

                .argument(Voucher.class, new VoucherArgument(voucherService, voucherConfig))

                .commands(new VoucherCommand(noticeService, voucherService, voucherItemService, configService))

                .build();

        Metrics metrics = new Metrics(this, 18960);
        metrics.addCustomChart(new SingleLineChart("total_vouchers", () -> voucherConfig.vouchers.size()));
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
    }
}
