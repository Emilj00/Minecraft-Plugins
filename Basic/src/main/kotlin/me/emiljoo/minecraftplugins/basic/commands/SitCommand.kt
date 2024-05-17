package me.emiljoo.minecraftplugins.basic.commands

import me.emiljoo.minecraftplugins.basic.controllers.SitController
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class SitCommand : EnhancedCommand("sit", "basic.player", isCommandEnabled = false) {
    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: Array<out String>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "You can only use this command as Player")
        }

        val player: Player = sender as Player
        SitController.togglePlayerSit(player)
    }
}