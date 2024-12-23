package me.emiljoo.minecraftplugins.basic.commands.accounts

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.basic.helpers.AccountHelper
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LogoutCommand : EnhancedCommand(
    "logout",
    "basic.player",
    usage = "/logout"
) {
    private val accountController: AccountController = (EnhancedPlugin.getInstance() as BasicPlugin).accountController

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "Only players can use this command.")
            return
        }

        accountController.logoutPlayer(sender)
        sender.teleport(AccountHelper.getVoidLocation())

        messenger.toCommandSender(sender, AccountHelper.logoutMessageConfig.get())
    }
}
