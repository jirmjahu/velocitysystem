package net.jirmjahu.velocitysystem.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.jirmjahu.velocitysystem.config.message.MessageProvider;

import java.util.List;

@RequiredArgsConstructor
public class JumpToCommand implements SimpleCommand {

    private final ProxyServer server;
    private final MessageProvider messageProvider;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(messageProvider.onlyPlayersMessage());
            return;
        }

        if (!player.hasPermission("proxy.command.jumpto")) {
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

            //check if the player is already connected to the same server as the target
            if (player.getCurrentServer().equals(target.get().getCurrentServer())) {
                player.sendMessage(messageProvider.alreadyConnectedMessage());
                return;
            }

            player.createConnectionRequest(targetServer.get().getServer()).fireAndForget();

            player.sendMessage(messageProvider.message("command.jumpto.connected")
                    .replaceText(text -> text.match("%player%").replacement(target.get().getUsername()))
                    .replaceText(text -> text.match("%server%").replacement(targetServer.get().getServerInfo().getName())));
            return;
        }

        player.sendMessage(messageProvider.message("command.jumpto.usage"));
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
