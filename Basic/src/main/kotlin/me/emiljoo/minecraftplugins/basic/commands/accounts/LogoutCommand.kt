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

class LogoutCommand : EnhancedCommand(
    "logout",
    "basic.player",
    usage = "/logout"
) {
    private val logoutMessageConfigField: ConfigField<String>

    init {
        val configManager: ConfigManager = EnhancedPlugin.getInstance().configManager

        logoutMessageConfigField = ConfigField(configManager, "accounts.logout.logout-message", "You has been successfully logged out.")
    }

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "Only players can use this command.")
            return
        }

        val basicPlugin: BasicPlugin = EnhancedPlugin.getInstance() as BasicPlugin
        val accountController: AccountController = basicPlugin.accountController

        val player: Player = sender

        accountController.logoutPlayer(player)
        player.teleport(AccountController.getVoidLocation())

        messenger.toCommandSender(sender, logoutMessageConfigField.get())
    }
}
