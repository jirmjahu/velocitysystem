package net.jirmjahu.velocitysystem.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.jirmjahu.velocitysystem.config.message.MessageProvider;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListCommand implements SimpleCommand {

    private final ProxyServer server;
    private final MessageProvider messageProvider;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(messageProvider.onlyPlayersMessage());
            return;
        }

        if (!player.hasPermission("proxy.command.list")) {
            player.sendMessage(messageProvider.noPermissionMessage());
            return;
        }

        if (invocation.arguments().length == 0) {
            player.sendMessage(messageProvider.message("command.list.proxy").replaceText(text -> text.match("%player_count%").replacement(String.valueOf(server.getAllPlayers().size()))));
            server.getAllPlayers().stream().filter(onlinePlayer -> onlinePlayer.getCurrentServer().isPresent()).forEach(onlinePlayer ->
                    player.sendMessage(messageProvider.message("command.list.playerinfo", false)
                            .replaceText(text -> text.match("%player%").replacement(onlinePlayer.getUsername()))
                            .replaceText(text -> text.match("%server%").replacement(onlinePlayer.getCurrentServer().get().getServer().getServerInfo().getName()))));
            return;
        }

        if (invocation.arguments().length == 1) {
            final var server = this.server.getServer(invocation.arguments()[0]);
            if (server.isEmpty()) {
                player.sendMessage(messageProvider.noServerMessage());
                return;
            }

            player.sendMessage(messageProvider.message("command.list.server").replaceText(text -> text.match("%player_count%").replacement(String.valueOf(server.get().getPlayersConnected().size()))));
            server.get().getPlayersConnected().forEach(onlinePlayer ->
                    player.sendMessage(messageProvider.message("command.list.playerinfo", false)
                            .replaceText(text -> text.match("%player%").replacement(onlinePlayer.getUsername()))
                            .replaceText(text -> text.match("%server%").replacement(server.get().getServerInfo().getName()))));
            return;
        }

        player.sendMessage(messageProvider.message("command.list.usage"));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        var args = invocation.arguments();

        List<String> suggestions = new ArrayList<>();
        if (args.length == 0) {
            suggestions.addAll(this.server.getAllServers().stream().map(server -> server.getServerInfo().getName()).toList());
        }

        if (args.length == 1) {
            suggestions.addAll(this.server.getAllServers().stream().map(server -> server.getServerInfo().getName()).filter(serverName -> serverName.startsWith(args[0])).toList());
        }
        return suggestions;
    }
}
