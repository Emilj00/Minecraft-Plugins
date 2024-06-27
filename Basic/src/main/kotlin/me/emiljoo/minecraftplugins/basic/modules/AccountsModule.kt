package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.CommandManager
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*


class AccountsModule : EnhancedModule(), Listener {
    private lateinit var accountController: AccountController
    private lateinit var messenger: Messenger

    override fun onEnable(plugin: EnhancedPlugin) {
        accountController = (plugin as BasicPlugin).accountController
        messenger = EnhancedPlugin.getMessenger()

        if (!plugin.server.onlineMode) {
            return
        }

        val commandManager: CommandManager = plugin.commandManager

        commandManager.getCommand("register")!!.disableCommand()
        commandManager.getCommand("login")!!.disableCommand()

        HandlerList.unregisterAll(this)
    }

    override fun onDisable(plugin: EnhancedPlugin) {
        Bukkit.getOnlinePlayers().forEach { player -> handlePlayerDisconnect(player) }
    }

    @EventHandler
    private fun onPlayerMove(event: PlayerMoveEvent) {
        val player: Player = event.player
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player: Player = event.player
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    private fun isCommandAllowed(message: String): Boolean {
        return message.startsWith("/r") || message.startsWith("/l")
    }

    @EventHandler
    private fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        val player: Player = event.player
        val message: String = event.message.trim()

        if (!isCommandAllowed(message) && !accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
            messenger.toPlayer(player, "You are not allowed to use this command.")
        }
    }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) {
        val player: Player = event.player
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onBlockBreak(event: BlockBreakEvent) {
        val player: Player = event.player
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onBlockPlace(event: BlockPlaceEvent) {
        val player: Player = event.player
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player: Player = event.player
        if (!accountController.isUserAuthenticated(player.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onPlayerPickupItem(event: EntityPickupItemEvent) {
        val entity = event.entity
        if (entity is Player && !accountController.isUserAuthenticated(entity.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onPlayerDamage(event: EntityDamageEvent) {
        val entity: Entity = event.entity
        if (entity is Player && !accountController.isUserAuthenticated(entity.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onPlayerDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager: Entity = event.damager
        if (damager is Player && !accountController.isUserAuthenticated(damager.name)) {
            event.isCancelled = true
        }

        val entity: Entity = event.entity
        if (entity is Player && !accountController.isUserAuthenticated(entity.name)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player

        if (!accountController.isUserRegistered(player.name)) {
            val startingWorld: World = Bukkit.getWorlds()[0]
            player.teleport(startingWorld.spawnLocation)

            messenger.toPlayer(player, "/register <password>")
            return
        }

        messenger.toPlayer(player, "/login <password>")
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player
        handlePlayerDisconnect(player)
    }

    private fun handlePlayerDisconnect(player: Player) {
        if (!accountController.isUserAuthenticated(player.name)) {
            return
        }

        accountController.logoutPlayer(player)
        player.teleport(AccountController.getVoidLocation())
    }
}


