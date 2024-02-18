package dev.piotrulla.vouchers.updater;

import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.notification.Notification;
import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdaterController implements Listener {

    private static final String UPDATE_AVAILABLE = "<gold>ptrlVouchers <gray>has an update available! <click:open_url:'https://github.com/P1otrulla/ptrlVouchers/releases/latest'><yellow><b>Click here to download</b></click>";

    private final NotificationBroadcaster broadcaster;
    private final UpdaterService updater;

    public UpdaterController(NotificationBroadcaster broadcaster, UpdaterService updater) {
        this.broadcaster = broadcaster;
        this.updater = updater;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.isOp() || player.hasPermission("ptrlvouchers.update")) {
            return;
        }

        this.updater.isUpToDate().whenComplete((state, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            if (state) {
                return;
            }

            this.broadcaster.sendAnnounce(player, Notification.chat(UPDATE_AVAILABLE));
        });
    }
}
