package me.emiljoo.minecraftplugins.basic.commands.accounts

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.basic.enums.RegistrationResult
import me.emiljoo.minecraftplugins.basic.helpers.AccountHelper
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class RegisterCommand : EnhancedCommand(
    "register",
    "basic.player",
    usage = "/register <password>"
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

        if (accountController.isUserAuthenticated(sender.name)) {
            messenger.toCommandSender(sender, AccountHelper.userAlreadyLoggedInMessageConfig.get())
            return
        }

        if (checkPassword(password)) {
            messenger.toCommandSender(sender, AccountHelper.passwordFormatInfoConfig.get())
            return
        }

        val registrationResult: RegistrationResult = accountController.registerPlayer(sender, password)
        when (registrationResult) {
            RegistrationResult.REGISTRATION_SUCCESS -> handleSuccessfulRegistration(messenger, sender)
            RegistrationResult.REGISTRATION_FAILED -> messenger.toCommandSender(sender, AccountHelper.registrationFailedConfig.get())
        }
    }

    private fun checkPassword(password: String): Boolean {
        val passwordRegex: Regex = AccountHelper.passwordRegexConfig.get().toRegex()
        return passwordRegex.matches(password)
    }

    private fun handleSuccessfulRegistration(messenger: Messenger, sender: Player) {
        messenger.toCommandSender(sender, AccountHelper.registrationSuccessConfig.get())

        sender.allowFlight = false
        sender.isFlying = false
    }
}

