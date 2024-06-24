package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent


class CombatOptimizations : EnhancedModule() {
    companion object {
        private const val DEFAULT_ATTACK_SPEED: Double = 4.0
        private const val DEFAULT_NO_DAMAGE_TICKS: Int = 10
    }

    override fun onEnable(plugin: EnhancedPlugin) {
        for (player in Bukkit.getOnlinePlayers()) {
            setAttackSpeed(player)
        }
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        setAttackSpeed(event.player)
    }

    @EventHandler
    private fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player) {
            return
        }

        val victim = event.entity as Player

        for (modifier in victim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.modifiers) {
            victim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.removeModifier(modifier)
        }
    }

    @EventHandler
    private fun onEntityHit(e: EntityDamageByEntityEvent) {
        val damagingEntityType = e.damager.type

        when (damagingEntityType) {
            EntityType.SNOWBALL, EntityType.EGG, EntityType.ENDER_PEARL -> {
                if (e.damage == 0.0) {
                    if (e.isApplicable(EntityDamageEvent.DamageModifier.ABSORPTION)) {
                        e.setDamage(EntityDamageEvent.DamageModifier.ABSORPTION, 0.0)
                    }
                }
            }

            else -> return
        }
    }

    private fun setAttackSpeed(player: Player) {
        val attackSpeedAttribute: AttributeInstance = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return

        player.maximumNoDamageTicks = if (isCombatSpeedFixEnabled()) 16 else DEFAULT_NO_DAMAGE_TICKS
        attackSpeedAttribute.baseValue = if (isCombatSpeedFixEnabled()) 6.5 else DEFAULT_ATTACK_SPEED
    }

    private fun isCombatSpeedFixEnabled(): Boolean {
        return true
    }
}