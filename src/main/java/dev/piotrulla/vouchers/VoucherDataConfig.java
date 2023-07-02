package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.config.ReloadableConfig;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class VoucherDataConfig implements ReloadableConfig {

    private int usedVouchers = 0;

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "data/usedVouchers.data");
    }

    public int getUsedVouchers() {
        return this.usedVouchers;
    }

    public void addUsedVoucher() {
        this.usedVouchers++;
    }
}
