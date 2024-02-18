package dev.piotrulla.vouchers.bridge.vault;

import org.bukkit.entity.Player;

public interface MoneyService {

    void deposit(Player player, double money);

    void withdraw(Player player, double money);
}
