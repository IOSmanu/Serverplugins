import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;

public class AppearCommand implements CommandExecutor {

    private final File file;
    private final FileConfiguration config;

    public AppearCommand(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 2) {
            player.sendMessage(ChatColor.DARK_GRAY + "»");
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + " §lAppear: " + ChatColor.RESET + "" + ChatColor.AQUA + "§7verwende §b/appear <prefix> Spieler");
            player.sendMessage(ChatColor.DARK_GRAY + "»");
            return true;
        }

        String prefix = ChatColor.translateAlternateColorCodes('&', args[0]);
        Player targetPlayer;

        if (args.length == 2) {
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.DARK_GRAY + "»");
                player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + " §lAppear: " + ChatColor.RESET + "" + ChatColor.AQUA + "§7Der Spieler wurde §bnicht gefunden §7oder ist §boffline");
                player.sendMessage(ChatColor.DARK_GRAY + "»");
                return true;
            }
        } else {
            targetPlayer = player;
        }

        config.set(targetPlayer.getUniqueId().toString(), prefix);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team team = board.registerNewTeam("customNameTeam");

        team.setPrefix(prefix + ChatColor.GRAY + " | ");
        team.addEntry(targetPlayer.getName());

        targetPlayer.setScoreboard(board);

        player.sendMessage(ChatColor.DARK_GRAY + "»");
        player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + " §lAppear: " + ChatColor.RESET + "" + ChatColor.AQUA + "§7Der Prefix wurde für §b" + targetPlayer.getName() + "§7 geändert!");
        player.sendMessage(ChatColor.DARK_GRAY + "»");

        return true;
    }

    public String getAppear(Player player) {
        return config.getString(player.getUniqueId().toString(), "");
    }
}
