package me.emiljoo.minecraftplugins.basic.commands.accounts

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class RegisterCommand : EnhancedCommand(
    "register",
    "basic.player",
    usage = "/register <password>"
) {
    private val passwordRegexConfigField: ConfigField<String>
    private val passwordFormatInfoConfigField: ConfigField<String>

    private val registrationSuccessConfigField: ConfigField<String>
    private val registrationFailedConfigField: ConfigField<String>

    init {
        val configManager: ConfigManager = EnhancedPlugin.getInstance().configManager

        passwordRegexConfigField = ConfigField(configManager, "accounts.password-regex", ".{3,}")
        passwordFormatInfoConfigField =
            ConfigField(configManager, "accounts.password-format-info", "Password must be at least 3 characters")
        registrationSuccessConfigField =
            ConfigField(configManager, "accounts.register.success-message", "You have successfully registered.")
        registrationFailedConfigField =
            ConfigField(configManager, "accounts.register.failed-message", "Registration failed. You are already registered.")
    }

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
            messenger.toCommandSender(sender, accountController.userAlreadyLoggedInMessageConfigField.get())
            return
        }

        val passwordRegex = Regex(passwordRegexConfigField.get())
        if (!passwordRegex.matches(password)) {
            messenger.toPlayer(player, passwordFormatInfoConfigField.get())
            return
        }

        val registrationSuccess = accountController.registerPlayer(player, password)

        if (registrationSuccess) {
            messenger.toCommandSender(sender, registrationSuccessConfigField.get())

            player.allowFlight = false
            player.isFlying = false
        } else {
            messenger.toCommandSender(sender, registrationFailedConfigField.get())
        }
    }
}

