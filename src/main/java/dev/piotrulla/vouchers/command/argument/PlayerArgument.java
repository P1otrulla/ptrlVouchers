package dev.piotrulla.vouchers.command.argument;

import dev.piotrulla.vouchers.VoucherConfig;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.List;

@ArgumentName("player")
public class PlayerArgument implements OneArgument<Player> {

    private final VoucherConfig config;
    private final Server server;

    public PlayerArgument(VoucherConfig config, Server server) {
        this.config = config;
        this.server = server;
    }

    @Override
    public Result<Player, ?> parse(LiteInvocation invocation, String argument) {
        Player player = this.server.getPlayer(argument);

        if (player == null) {
            return Result.error(this.config.playerNotFound);
        }

        return Result.ok(player);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.server.getOnlinePlayers()
                .stream()
                .map(HumanEntity::getName)
                .map(Suggestion::of)
                .toList();
    }
}
