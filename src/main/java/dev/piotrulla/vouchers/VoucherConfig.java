package dev.piotrulla.vouchers;

import com.google.common.collect.ImmutableMap;
import dev.piotrulla.vouchers.config.ReloadableConfig;
import dev.piotrulla.vouchers.notification.Notification;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VoucherConfig implements ReloadableConfig, VoucherRepository {

    public Map<String, Voucher> vouchers = ImmutableMap.of(
        "default", new Voucher()
    );

    @Description({ "", "# Options" })
    public boolean reciveUpdateOnJoin = true;

    @Description({ "", "# Messages" })
    public Notification invalidAmount = Notification.chat("&4Error: &cThe specified amount is invalid!");
    public Notification playerNotFound = Notification.chat("&4Error: &cPlayer not found!");
    public Notification noPermission = Notification.chat("&cI'm afraid you don't have permission! &f({PERMISSION})");
    public Notification correctUsage = Notification.chat("&eCorrect usage: &9{USAGE}");

    public Notification correctUsageHeader = Notification.chat("&eCorrect usage:");
    public Notification correctUsageEntry = Notification.chat("&9{USAGE}");

    public Notification vouchersList = Notification.chat("&6Available vouchers: &7{VOUCHERS}");
    public Notification voucherNotFound = Notification.chat("&cVoucher not found!");
    public Notification voucherRecived = Notification.chat("&6You have received a voucher: &7{VOUCHER}");
    public Notification voucherGiven = Notification.chat("&6You have given the voucher: &f{VOUCHER} &6to player &f{PLAYER}");
    public Notification voucherUsed = Notification.title("&6You have used the voucher: &7{VOUCHER}");

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "config.yml");
    }

    @Override
    public Optional<Voucher> findVoucher(String name) {
        return Optional.ofNullable(this.vouchers.get(name));
    }

    @Override
    public Optional<Voucher> findVoucher(ItemStack itemStack) {
        for (Voucher voucher : this.vouchers.values()) {
            if (voucher.item().isSimilar(itemStack)) {
                return Optional.of(voucher);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<String> findAllVouchers() {
        return this.vouchers.values().stream().map(Voucher::name).toList();
    }
}
