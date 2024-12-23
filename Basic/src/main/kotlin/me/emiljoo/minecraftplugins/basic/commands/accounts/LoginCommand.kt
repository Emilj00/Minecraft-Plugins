package me.emiljoo.minecraftplugins.basic.commands.accounts

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.basic.enums.LoginFailureReason
import me.emiljoo.minecraftplugins.basic.helpers.AccountHelper
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.Result
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LoginCommand : EnhancedCommand(
    "login",
    "basic.player",
    aliases = listOf("l"),
    usage = "/login <password>"
) {
    private val accountController: AccountController = (EnhancedPlugin.getInstance() as BasicPlugin).accountController

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "Only players can use this command.")
            return
        }

        if (args.isEmpty()) {
            messenger.toCommandSender(sender, usage)
            return
        }

        val password = args.joinToString(" ") { it.toString() }

        when (val loginResult: Result<Location, LoginFailureReason> = accountController.loginPlayer(sender, password)) {
            is Result.Success -> handleSuccessfulLogin(sender, loginResult.value, messenger)
            is Result.Failure -> messenger.toCommandSender(sender, loginResult.error.getMessage())
        }
    }

    private fun handleSuccessfulLogin(player: Player, location: Location?, messenger: Messenger) {
        messenger.toPlayer(player, AccountHelper.successfulLoginMessageConfig.get())

        player.teleport(location!!)
        player.allowFlight = false
        player.isFlying = false
    }
}
