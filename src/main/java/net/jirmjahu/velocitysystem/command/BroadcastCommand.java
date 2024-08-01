package net.jirmjahu.velocitysystem.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.jirmjahu.velocitysystem.config.message.MessageProvider;
import net.kyori.adventure.text.Component;

@RequiredArgsConstructor
public class BroadcastCommand implements SimpleCommand {

    private final ProxyServer server;
    private final MessageProvider messageProvider;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(messageProvider.onlyPlayersMessage());
            return;
        }

        if (!player.hasPermission("proxy.command.find")) {
            player.sendMessage(messageProvider.noPermissionMessage());
            return;
        }

        if (invocation.arguments().length < 1) {
            player.sendMessage(messageProvider.message("command.broadcast.usage"));
            return;
        }

        //replace the & char so the player that broadcasts can use color codes
        final var message = String.join(" ", invocation.arguments()).replace("&", "§");

        server.getAllPlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(messageProvider.message("command.broadcast.message", false)
                .replaceText(text -> text.match("%content%").replacement(Component.text(message)))));
    }
}
