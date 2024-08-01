package net.jirmjahu.velocitysystem.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.jirmjahu.velocitysystem.config.message.MessageProvider;

@RequiredArgsConstructor
public class PingCommand implements SimpleCommand {

    private final ProxyServer server;
    private final MessageProvider messageProvider;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(messageProvider.onlyPlayersMessage());
            return;
        }

        if (!player.hasPermission("proxy.command.ping")) {
            player.sendMessage(messageProvider.noPermissionMessage());
            return;
        }

        if (invocation.arguments().length != 0 && invocation.arguments().length != 1) {
            player.sendMessage(messageProvider.message("command.ping.usage"));
            return;
        }

        if (invocation.arguments().length == 0) {
            //show self ping
            player.sendMessage(messageProvider.message("command.ping.self").replaceText(text -> text.match("%ping%").replacement(String.valueOf(player.getPing()))));
            return;
        }

        if (invocation.arguments().length == 1) {
            if (!player.hasPermission("proxy.command.ping.other")) {
                player.sendMessage(messageProvider.noPermissionMessage());
                return;
            }
            final var target = this.server.getPlayer(invocation.arguments()[0]);

            if (target.isEmpty()) {
                player.sendMessage(messageProvider.noPlayerMessage());
                return;
            }

            player.sendMessage(messageProvider.message("command.ping.other")
                    .replaceText(text -> text.match("%player%").replacement(target.get().getUsername()))
                    .replaceText(text -> text.match("%ping%").replacement(String.valueOf(target.get().getPing()))));
        }
    }
}
