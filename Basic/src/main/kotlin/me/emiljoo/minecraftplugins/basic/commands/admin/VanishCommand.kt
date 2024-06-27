package me.emiljoo.minecraftplugins.basic.commands.admin

import me.emiljoo.minecraftplugins.basic.modules.ChatMessagesModule
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.data.types.BoolPlayerDataEntry
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VanishCommand : EnhancedCommand(
    "vanish",
    "basic.admin",
    usage = "/vanish",
    aliases = listOf("v")
) {
    companion object {
        const val VANISH_ENTRY_NAME = "is-vanished"
    }

    private val enhancedPlugin: EnhancedPlugin = EnhancedPlugin.getInstance()

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "You can only use this command as player!")
            return
        }

        val player: Player = sender
        val playerData = enhancedPlugin.playerDataManager.findPlayerData(player)

        val vanishEntry = playerData!!.getDataEntry(VANISH_ENTRY_NAME) as BoolPlayerDataEntry
        vanishEntry.toggleValue()

        if (vanishEntry.getValue()) {
            val quitMessage = ChatMessagesModule.playerQuitConfigField.get().replace("{player}", player.name)
            messenger.toAllPlayers(quitMessage, false)

            Bukkit.getOnlinePlayers().forEach { onlinePlayer -> onlinePlayer.hidePlayer(enhancedPlugin, player) }
        } else {
            val joinMessage = ChatMessagesModule.playerJoinConfigField.get().replace("{player}", player.name)
            messenger.toAllPlayers(joinMessage, false)

            Bukkit.getOnlinePlayers().forEach { onlinePlayer -> onlinePlayer.showPlayer(enhancedPlugin, player) }
        }
    }
}