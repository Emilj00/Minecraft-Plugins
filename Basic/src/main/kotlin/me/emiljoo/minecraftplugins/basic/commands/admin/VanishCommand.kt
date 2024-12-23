package me.emiljoo.minecraftplugins.basic.commands.admin

import me.emiljoo.minecraftplugins.basic.helpers.ChatMessagesHelper
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

        val playerData = enhancedPlugin.playerDataManager.findPlayerData(sender) ?: return

        val vanishEntry = playerData.getDataEntry(VANISH_ENTRY_NAME) as BoolPlayerDataEntry
        vanishEntry.toggleValue()

        val isPlayerVanished: Boolean = vanishEntry.getValue();

        if (isPlayerVanished) {
            val quitMessage = ChatMessagesHelper.playerQuitMessageConfig.get().replace("{player}", sender.name)
            messenger.toAllPlayers(quitMessage, false)

            Bukkit.getOnlinePlayers().forEach { onlinePlayer -> onlinePlayer.hidePlayer(enhancedPlugin, sender) }
        } else {
            val joinMessage = ChatMessagesHelper.playerJoinMessageConfig.get().replace("{player}", sender.name)
            messenger.toAllPlayers(joinMessage, false)

            Bukkit.getOnlinePlayers().forEach { onlinePlayer -> onlinePlayer.showPlayer(enhancedPlugin, sender) }
        }
    }
}