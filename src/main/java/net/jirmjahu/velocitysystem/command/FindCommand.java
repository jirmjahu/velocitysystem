package net.jirmjahu.velocitysystem.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.jirmjahu.velocitysystem.config.message.MessageProvider;

import java.util.List;

@RequiredArgsConstructor
public class FindCommand implements SimpleCommand {

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

        if (invocation.arguments().length == 1) {
            final var target = this.server.getPlayer(invocation.arguments()[0]);

            if (target.isEmpty()) {
                player.sendMessage(messageProvider.noPlayerMessage());
                return;
            }

            final var targetServer = target.get().getCurrentServer();

            //check if the target server is present
            if (targetServer.isEmpty()) {
                return;
            }

            player.sendMessage(messageProvider.message("command.find.found-player")
                    .replaceText(text -> text.match("%player%").replacement(target.get().getUsername()))
                    .replaceText(text -> text.match("%server%").replacement(targetServer.get().getServerInfo().getName())));
            return;
        }

        player.sendMessage(messageProvider.message("command.find.usage"));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        var args = invocation.arguments();

        if (args.length == 0) {
            return this.server.getAllPlayers().stream().map(Player::getUsername).toList();
        }

        if (args.length == 1) {
            return this.server.getAllPlayers().stream().map(Player::getUsername).filter(username -> username.startsWith(args[0])).toList();
        }

        return List.of();
    }
}
