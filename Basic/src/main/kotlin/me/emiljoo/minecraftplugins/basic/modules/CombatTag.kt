package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.controllers.TimerController
import me.emiljoo.minecraftplugins.utilities.data.PlayerData
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
        const val COMBAT_TAG_TIME: Int = 15
        const val COMBAT_TIMER_KEY: String = "pvp-timer"

        val timerController: TimerController = TimerController()
    }

    private var playerDataManager: PlayerDataManager? = null
    private val messenger: Messenger = EnhancedPlugin.getMessenger();

    private fun onTimersTick(timer: TimerPlayerDataEntry) {
        if (timer.getValue() <= 0) {
            return;
        }

        val playerData: PlayerData = timer.getOwnerPlayerData()
        val player: Player = playerData.getOwner();

        val bracketColor: String =
            if (timer.getValue() % 2 == 0) ChatColor.GRAY.toString() else ChatColor.WHITE.toString()

        val message: String = Messenger.colorize("&c&l> > > ${bracketColor}&lANTILOGOUT &c&l< < <")
        val textComponent = TextComponent(message);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent)

        EnhancedPlugin.getMessenger().toAllPlayers("Timer: ${timer.getValue()}")
    }

    override fun onEnable(plugin: EnhancedPlugin) {
        playerDataManager = plugin.playerDataManager
        timerController.startTimers(plugin)

        timerController.onTimersTickEvent += ::onTimersTick
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player || event.damager !is Player) {
            return
        }

        val victim: Player = event.entity as Player
        val victimPlayerData: PlayerData? = playerDataManager?.findPlayerData(victim)
        resetTimer(victimPlayerData)

        val attacker: Player = event.damager as Player
        val attackerPlayerData: PlayerData? = playerDataManager?.findPlayerData(attacker)
        resetTimer(attackerPlayerData)
    }

    private fun resetTimer(playerData: PlayerData?) {
        playerData ?: return

        if (playerData.hasEntry(COMBAT_TIMER_KEY)) {
            val victimCombatTimer = playerData.getDataEntry(COMBAT_TIMER_KEY) as TimerPlayerDataEntry
            victimCombatTimer.resetTimer()
        } else {
            val timer = createCombatTimer(playerData)

            timerController.addTimer(timer)
            playerData.addDataEntry(COMBAT_TIMER_KEY, timer)
        }
    }

    private fun createCombatTimer(playerData: PlayerData): TimerPlayerDataEntry {
        return TimerPlayerDataEntry(COMBAT_TAG_TIME, playerData)
    }

    @EventHandler
    private fun onPlayerDeath(event: PlayerDeathEvent) {
        val player: Player = event.entity.player!!
        val playerData: PlayerData? = playerDataManager!!.findPlayerData(player)

        val timerEntry = playerData!!.getDataEntry(COMBAT_TIMER_KEY) as TimerPlayerDataEntry
        timerEntry.setTimerFinished()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player

        val playerData: PlayerData = playerDataManager!!.findPlayerData(player) ?: return
        val timerEntry = playerData.getDataEntry(COMBAT_TIMER_KEY) as TimerPlayerDataEntry

        if (!timerEntry.isTimerFinished()) {
            player.health = 0.0
            timerEntry.setTimerFinished()
        }
    }
}