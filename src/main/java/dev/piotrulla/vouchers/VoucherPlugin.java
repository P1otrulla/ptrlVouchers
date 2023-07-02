package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.bridge.BridgeService;
import dev.piotrulla.vouchers.command.argument.PlayerArgument;
import dev.piotrulla.vouchers.command.argument.VoucherArgument;
import dev.piotrulla.vouchers.command.handler.InvalidUsageHandler;
import dev.piotrulla.vouchers.command.handler.NotificationHandler;
import dev.piotrulla.vouchers.command.handler.PermissionHandler;
import dev.piotrulla.vouchers.config.ConfigService;
import dev.piotrulla.vouchers.money.MoneyService;
import dev.piotrulla.vouchers.notification.Notification;
import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
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

    private ConfigService configService;
    private VoucherDataConfig data;
    private VoucherConfig config;

    private BridgeService bridgeService;
    private MoneyService moneyService;

    private VoucherService voucherService;

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;

    private NotificationBroadcaster broadcaster;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.configService = new ConfigService(this.getDataFolder());
        this.data = this.configService.load(new VoucherDataConfig());
        this.config = this.configService.load(new VoucherConfig());

        Server server = this.getServer();

        this.bridgeService = new BridgeService(server.getServicesManager(), server.getPluginManager(), this.moneyService);
        this.bridgeService.init();

        this.moneyService = this.bridgeService.borrowMoneyService();

        this.voucherService = new VoucherService(this.data, this.moneyService);

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();

        this.broadcaster = new NotificationBroadcaster(this.audienceProvider, this.miniMessage);

        server.getPluginManager().registerEvents(new VoucherController(this.broadcaster, this.config, this.voucherService, this.config), this);

        this.liteCommands = LiteBukkitFactory.builder(server, "vouchers")
                .resultHandler(Notification.class, new NotificationHandler(this.broadcaster))
                .resultHandler(Schematic.class, new InvalidUsageHandler(this.broadcaster, this.config))
                .resultHandler(RequiredPermissions.class, new PermissionHandler(this.broadcaster, this.config))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("only player"))

                .argument(Player.class, new PlayerArgument(this.config, server))
                .argument(Voucher.class, new VoucherArgument(this.config))

                .commandInstance(new VoucherCommand(this.broadcaster, this.config, this.config))

                .register();

        Metrics metrics = new Metrics(this, 18960);

        metrics.addCustomChart(new SingleLineChart("used_vouchers", data::getUsedVouchers));
        metrics.addCustomChart(new SingleLineChart("total_vouchers", () -> this.config.vouchers.size()));
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }

}
