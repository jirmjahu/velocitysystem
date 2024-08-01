package net.jirmjahu.velocitysystem;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

@Getter
@Plugin(id = "velocitysystem", name = "VelocitySystem", description = "Simple BungeeSystem but for Velocity.", authors = "jirmjahu_u")
public class VelocitySystem {

    private final Logger logger;
    private final ProxyServer server;

    @Inject
    public VelocitySystem(Logger logger, ProxyServer server) {
        this.logger = logger;
        this.server = server;
    }

    @Subscribe
    public void initialize(ProxyInitializeEvent event) {
        long currentTime = System.currentTimeMillis();



        logger.info("[VelocitySystem] The Plugin has been enabled! (Took {}ms)", System.currentTimeMillis() - currentTime);
    }
}