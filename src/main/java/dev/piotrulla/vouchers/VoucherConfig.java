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

    @Description({ "", "# Messages" })
    public Notification invalidAmount = Notification.chat("&4Blad:︎ &cPodana ilość jest nieprawidłowa!");
    public Notification playerNotFound = Notification.chat("&4Blad:︎ &cNie znaleziono takiego gracza!");
    public Notification noPermission = Notification.chat("&cObawiam się kolego, że nie masz uprawnień! &f({PERMISSION})");
    public Notification correctUsage = Notification.chat("&ePoprawne użycie: &9{USAGE}");

    public Notification correctUsageHeader = Notification.chat("&ePoprawne użycie:");
    public Notification correctUsageEntry = Notification.chat("&9{USAGE}");

    public Notification vouchersList = Notification.chat("&6Dostepne vouchery: &7{VOUCHERS}");
    public Notification voucherNotFound = Notification.chat("&cNie znaleziono takiego voucheru!");
    public Notification voucherRecived = Notification.chat("&6Otrzymales voucher &7{VOUCHER}");
    public Notification voucherGiven = Notification.chat("&6Nadałeś voucher: &f{VOUCHER} &6graczowi &f{PLAYER}");
    public Notification voucherUsed = Notification.title("&6Użyłeś vouchera &7{VOUCHER}");

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
