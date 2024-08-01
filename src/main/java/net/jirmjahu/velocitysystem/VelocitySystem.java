package net.jirmjahu.velocitysystem;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.jirmjahu.velocitysystem.command.BroadcastCommand;
import net.jirmjahu.velocitysystem.command.JumpToCommand;
import net.jirmjahu.velocitysystem.command.ListCommand;
import net.jirmjahu.velocitysystem.command.PingCommand;
import net.jirmjahu.velocitysystem.config.ConfigManager;
import net.jirmjahu.velocitysystem.config.message.MessageProvider;
import org.slf4j.Logger;

@Getter
@Plugin(id = "velocitysystem", name = "VelocitySystem", description = "Simple BungeeSystem but for Velocity.", authors = "jirmjahu_u")
public class VelocitySystem {

    private final Logger logger;
    private final ProxyServer server;

    private ConfigManager defaultConfig;
    private ConfigManager messagesConfig;
    private MessageProvider messageProvider;

    @Inject
    public VelocitySystem(Logger logger, ProxyServer server) {
        this.logger = logger;
        this.server = server;
    }

    @Subscribe
    public void initialize(ProxyInitializeEvent event) {
        long currentTime = System.currentTimeMillis();

        this.defaultConfig = new ConfigManager(this, "config.toml");
        this.messagesConfig = new ConfigManager(this, "messages.toml");
        this.messageProvider = new MessageProvider(this.messagesConfig);

        //register command
        this.registerCommand("list", new ListCommand(this.server, this.messageProvider), "glist", "onlineplayers");
        this.registerCommand("jumpto", new JumpToCommand(this.server, this.messageProvider), "goto");
        this.registerCommand("broadcast", new BroadcastCommand(this.server, this.messageProvider), "bc", "alert");
        this.registerCommand("ping", new PingCommand(this.server, this.messageProvider), "latency");
        this.registerCommand("find", new BroadcastCommand(this.server, this.messageProvider), "whereis");


        logger.info("[VelocitySystem] The Plugin has been enabled! (Took {}ms)", System.currentTimeMillis() - currentTime);
    }

   private void registerCommand(String name, Command command, String... aliases) {
        this.server.getCommandManager().register(this.server.getCommandManager().metaBuilder(name).aliases(aliases).build(), command);
   }

}