package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.bridge.BridgeService;
import dev.piotrulla.vouchers.bridge.vault.MoneyService;
import dev.piotrulla.vouchers.command.argument.PlayerArgument;
import dev.piotrulla.vouchers.command.argument.VoucherArgument;
import dev.piotrulla.vouchers.command.handler.InvalidUsageHandler;
import dev.piotrulla.vouchers.command.handler.NotificationHandler;
import dev.piotrulla.vouchers.command.handler.PermissionHandler;
import dev.piotrulla.vouchers.config.ConfigService;
import dev.piotrulla.vouchers.notification.Notification;
import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import dev.piotrulla.vouchers.updater.UpdaterController;
import dev.piotrulla.vouchers.updater.UpdaterService;
import dev.piotrulla.vouchers.util.legacy.LegacyColorProcessor;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VoucherPlugin extends JavaPlugin {

    private VoucherConfig config;

    private MoneyService moneyService;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        ConfigService configService = new ConfigService(this.getDataFolder());
        VoucherDataConfig data = configService.load(new VoucherDataConfig());
        this.config = configService.load(new VoucherConfig());

        Server server = this.getServer();

        UpdaterService updaterService = new UpdaterService(this.getDescription());

        BridgeService bridgeService =
                new BridgeService(server.getServicesManager(), server.getPluginManager(), this.moneyService);
        bridgeService.init();

        this.moneyService = bridgeService.borrowMoneyService();

        VoucherService voucherService = new VoucherService(data, configService, this.moneyService);

        AudienceProvider audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();

        NotificationBroadcaster broadcaster = new NotificationBroadcaster(audienceProvider, miniMessage);

        server.getPluginManager()
                .registerEvents(
                        new VoucherController(broadcaster, this.config, voucherService, this.config),
                        this);

        if (this.config.reciveUpdateOnJoin) {
            server.getPluginManager()
                    .registerEvents(new UpdaterController(broadcaster, updaterService), this);
        }

        this.liteCommands = LiteBukkitFactory.builder(server, "vouchers")
                .resultHandler(Notification.class, new NotificationHandler(broadcaster))
                .resultHandler(Schematic.class, new InvalidUsageHandler(broadcaster, this.config))
                .resultHandler(RequiredPermissions.class, new PermissionHandler(broadcaster, this.config))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("only player"))

                .argument(Player.class, new PlayerArgument(this.config, server))
                .argument(Voucher.class, new VoucherArgument(this.config))

                .commandInstance(new VoucherCommand(broadcaster, this.config, this.config))

                .register();

        Metrics metrics = new Metrics(this, 18960);

        metrics.addCustomChart(new SingleLineChart("used_vouchers", data::getUsedVouchers));
        metrics.addCustomChart(new SingleLineChart("total_vouchers", () -> this.config.vouchers.size()));

        updaterService.isUpToDate().whenComplete((state, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            if (state) {
                return;
            }

            this.getLogger().warning(" ");
            this.getLogger().warning("ptrlVouchers has an update available!");
            this.getLogger().warning("Download the latest version from:");
            this.getLogger().warning("https://github.com/P1otrulla/ptrlVouchers/releases/latest");
            this.getLogger().warning(" ");
        });
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }
}
