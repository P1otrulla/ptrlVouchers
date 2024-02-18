package dev.piotrulla.vouchers.bridge;

import dev.piotrulla.vouchers.bridge.vault.VaultMoneyService;
import dev.piotrulla.vouchers.bridge.vault.MoneyService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import java.util.logging.Logger;

public class BridgeService {

    private static final Logger LOGGER = Logger.getLogger(BridgeService.class.getName());

    private final ServicesManager servicesManager;
    private final PluginManager pluginManager;
    private MoneyService moneyService;

    public BridgeService(ServicesManager servicesManager, PluginManager pluginManager, MoneyService moneyService) {
        this.servicesManager = servicesManager;
        this.pluginManager = pluginManager;
        this.moneyService = moneyService;
    }

    public void init() {
        this.init("Vault", () -> {
            RegisteredServiceProvider<Economy> ecoProvider = this.servicesManager.getRegistration(Economy.class);

            if (ecoProvider == null) {
                LOGGER.warning("Found Vault plugin but can't handle any providers!");

                return;
            }

            this.moneyService = new VaultMoneyService(ecoProvider.getProvider());
        });
    }

    private void init(String pluginName, BridgeInitializer bridge) {
        if (this.pluginManager.isPluginEnabled(pluginName)) {
            bridge.initialize();

            LOGGER.info("Created bridge with "+ pluginName +" plugin!");
        }
    }

    public MoneyService borrowMoneyService() {
        return this.moneyService;
    }
}
