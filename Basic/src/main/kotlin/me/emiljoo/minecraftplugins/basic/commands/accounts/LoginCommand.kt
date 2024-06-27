package me.emiljoo.minecraftplugins.basic.commands.accounts

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.commands.arguments.StringArgument
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LoginCommand : EnhancedCommand(
    "login",
    "basic.player",
    aliases = listOf("l"),
    usage = "/login <password>"
) {
    init {
        addArgument(StringArgument())
    }

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "Only players can use this command.")
            return
        }

        if (args.isEmpty() || args.size != 1) {
            messenger.toCommandSender(sender, usage)
            return
        }

        val basicPlugin: BasicPlugin = EnhancedPlugin.getInstance() as BasicPlugin
        val accountController: AccountController = basicPlugin.accountController

        val password = args[0] as String
        val player: Player = sender

        if (accountController.isUserAuthenticated(player.name)) {
            messenger.toCommandSender(sender, "You are already logged in.")
            return
        }

        val playerLoginResult: AccountController.LoginResult = accountController.loginPlayer(player, password)

        if (playerLoginResult.success) {
            messenger.toCommandSender(sender, "You have successfully logged in.")
            player.teleport(playerLoginResult.location!!)
        } else {
            messenger.toCommandSender(sender, playerLoginResult.failureReason!!.getMessage())
        }
    }
}
