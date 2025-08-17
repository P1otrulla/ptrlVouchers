package dev.piotrulla.vouchers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class VoucherService {

    private final VoucherConfig config;
    private final VoucherItemService voucherItemService;

    public VoucherService(VoucherConfig config, VoucherItemService voucherItemService) {
        this.config = config;
        this.voucherItemService = voucherItemService;
    }

    public Optional<Voucher> findVoucher(String name) {
        return Optional.ofNullable(this.config.vouchers.get(name));
    }

    public Optional<Voucher> findVoucher(ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }

        String voucherName = this.voucherItemService.getVoucherNameFromItem(itemStack);
        if (voucherName == null) {
            return Optional.empty();
        }

        Optional<Voucher> voucherOptional = findVoucher(voucherName);
        if (voucherOptional.isEmpty()) {
            return Optional.empty();
        }

        Voucher voucher = voucherOptional.get();

        if (!this.voucherItemService.isValidVoucher(itemStack, voucher)) {
            return Optional.empty();
        }

        return Optional.of(voucher);
    }

    public List<String> findAllVouchers() {
        return this.config.vouchers.values().stream()
                .filter(Objects::nonNull)
                .map(Voucher::name)
                .toList();
    }
}