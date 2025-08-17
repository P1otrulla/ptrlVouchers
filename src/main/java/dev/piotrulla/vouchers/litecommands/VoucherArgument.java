package dev.piotrulla.vouchers.litecommands;

import dev.piotrulla.vouchers.Voucher;
import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.VoucherService;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public class VoucherArgument extends ArgumentResolver<CommandSender, Voucher> {

    private final VoucherService repository;
    private final VoucherConfig config;

    public VoucherArgument(VoucherService repository, VoucherConfig config) {
        this.repository = repository;
        this.config = config;
    }

    @Override
    protected ParseResult<Voucher> parse(
            Invocation<CommandSender> invocation,
            Argument<Voucher> context,
            String argument
    ) {

        Optional<Voucher> voucher = this.repository.findVoucher(argument);

        return voucher.<ParseResult<Voucher>>map(ParseResult::success)
                .orElseGet(() -> ParseResult.failure(this.config.voucherNotFound));
    }

    @Override
    public SuggestionResult suggest(
            Invocation<CommandSender> invocation,
            Argument<Voucher> argument,
            SuggestionContext context
    ) {

        return SuggestionResult.of(this.repository.findAllVouchers());
    }
}