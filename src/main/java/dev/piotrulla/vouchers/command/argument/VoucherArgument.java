package dev.piotrulla.vouchers.command.argument;

import dev.piotrulla.vouchers.Voucher;
import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.VoucherRepository;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.util.List;
import java.util.Optional;

@ArgumentName("voucher")
public class VoucherArgument implements OneArgument<Voucher> {

    private final VoucherRepository repository;
    private final VoucherConfig config;

    public VoucherArgument(VoucherConfig config) {
        this.config = config;
        this.repository = config;
    }

    @Override
    public Result<Voucher, ?> parse(LiteInvocation invocation, String argument) {
        Optional<Voucher> voucher = this.repository.findVoucher(argument);

        if (voucher.isPresent()) {
            return Result.ok(voucher.get());
        }

        return Result.error(this.config.voucherNotFound);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.repository.findAllVouchers()
                .stream()
                .map(Suggestion::of)
                .toList();
    }
}
