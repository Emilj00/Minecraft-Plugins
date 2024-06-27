package me.emiljoo.minecraftplugins.basic.commands.accounts

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LoginCommand : EnhancedCommand(
    "login",
    "basic.player",
    aliases = listOf("l"),
    usage = "/login <password>"
) {
    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "Only players can use this command.")
            return
        }

        if (args.isEmpty()) {
            messenger.toCommandSender(sender, usage)
            return
        }

        val basicPlugin: BasicPlugin = EnhancedPlugin.getInstance() as BasicPlugin
        val accountController: AccountController = basicPlugin.accountController

        val password = args.joinToString(" ") { it.toString() }
        val player: Player = sender

        if (accountController.isUserAuthenticated(player.name)) {
            messenger.toCommandSender(sender, "You are already logged in.")
            return
        }

        val playerLoginResult: AccountController.LoginResult = accountController.loginPlayer(player, password)

        if (playerLoginResult.success) {
            messenger.toCommandSender(sender, "You have successfully logged in.")

            player.teleport(playerLoginResult.location!!)
            player.allowFlight = false
            player.isFlying = false
        } else {
            messenger.toCommandSender(sender, playerLoginResult.failureReason!!.getMessage())
        }
    }
}
