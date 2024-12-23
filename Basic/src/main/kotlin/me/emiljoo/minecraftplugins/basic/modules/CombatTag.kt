package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.controllers.TimerController
import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataEntry
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataManager
import me.emiljoo.minecraftplugins.utilities.data.types.TimerPlayerDataEntry
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent


class CombatTag : EnhancedModule() {
    companion object {
        const val COMBAT_TIMER_KEY: String = "pvp-timer"
    }

    private val accountController: AccountController = (EnhancedPlugin.getInstance() as BasicPlugin).accountController
    private lateinit var playerDataManager: PlayerDataManager
    private val timerController: TimerController = TimerController()

    private lateinit var combatTagTimeConfigField: ConfigField<Int>
    private var combatTagEnabled: Boolean = true

    private var isSpigotServer: Boolean = false

    private fun onTimerTick(timer: TimerPlayerDataEntry) {
        if (timer.getValue() <= 0) {
            return
        }

        val playerData: PlayerData = timer.getOwnerPlayerData()
        val player: Player = playerData.getOwner()

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, getCombatTagTextComponent(timer))
    }

    private fun getCombatTagTextComponent(timer: TimerPlayerDataEntry): TextComponent {
        val textColor: String = (if (timer.getValue() % 2 == 0) ChatColor.GRAY else ChatColor.WHITE).toString()
        val message: String = Messenger.colorize("&c&l> > > ${textColor}&lANTILOGOUT &c&l< < <")

        return TextComponent(message)
    }

    override fun onEnable(plugin: EnhancedPlugin) {
        val configManager = plugin.configManager

        combatTagTimeConfigField = ConfigField(configManager, "combat-tag.combat-tag-time", 15)
        combatTagEnabled = combatTagTimeConfigField.get() > 0

        if (!combatTagEnabled) {
            return
        }

        playerDataManager = plugin.playerDataManager
        timerController.startTimers(plugin)

        timerController.onTimerTick += ::onTimerTick

        isSpigotServer = plugin.server.version.contains("Spigot")
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (!combatTagEnabled) {
            return
        }

        if (event.entity !is Player || event.damager !is Player) {
            return
        }


        val victim: Player = event.entity as Player
        val damager: Player = event.damager as Player

        if (!accountController.isUserAuthenticated(victim.name) || !accountController.isUserAuthenticated(damager.name)) {
            return
        }

        handleCombatTagTimer(victim)
        handleCombatTagTimer(damager)
    }

    private fun handleCombatTagTimer(player: Player) {
        val playerData: PlayerData = playerDataManager.findPlayerData(player) ?: return

        val timerEntry = playerData.getDataEntry(COMBAT_TIMER_KEY) as? TimerPlayerDataEntry

        if (timerEntry != null) {
            timerEntry.resetTimer()
            return
        }

        val timer: TimerPlayerDataEntry = createCombatTimer(playerData)
        timerController.addTimer(timer)
        playerData.setDataEntry(COMBAT_TIMER_KEY, timer)
    }


    private fun createCombatTimer(playerData: PlayerData): TimerPlayerDataEntry {
        return TimerPlayerDataEntry(combatTagTimeConfigField.get(), playerData)
    }

    @EventHandler
    private fun onPlayerDeath(event: PlayerDeathEvent) {
        if (!combatTagEnabled) {
            return
        }

        val player: Player = event.entity.player ?: return
        val playerData: PlayerData = playerDataManager.findPlayerData(player) ?: return
        val playerDataEntry: PlayerDataEntry<*> = playerData.getDataEntry(COMBAT_TIMER_KEY) ?: return

        val timerEntry: TimerPlayerDataEntry = playerDataEntry as TimerPlayerDataEntry
        timerEntry.setTimerFinished()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        if (!combatTagEnabled) {
            return
        }

        val player: Player = event.player

        val playerData: PlayerData = playerDataManager.findPlayerData(player) ?: return
        val timerEntry: PlayerDataEntry<*> = playerData.getDataEntry(COMBAT_TIMER_KEY) ?: return

        val combatTimer: TimerPlayerDataEntry = timerEntry as TimerPlayerDataEntry
        if (!combatTimer.isTimerFinished()) {
            player.health = 0.0
            combatTimer.setTimerFinished()
        }
    }
}