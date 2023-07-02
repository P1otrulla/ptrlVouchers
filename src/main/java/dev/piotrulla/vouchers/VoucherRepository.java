package dev.piotrulla.vouchers;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository {

    Optional<Voucher> findVoucher(String name);

    Optional<Voucher> findVoucher(ItemStack itemStack);

    List<String> findAllVouchers();
}
