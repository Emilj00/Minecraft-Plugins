package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent


class CombatOptimizations : EnhancedModule() {
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
        val damagerEntity = e.damager.type

        when (damagerEntity) {
            EntityType.SNOWBALL, EntityType.EGG, EntityType.ENDER_PEARL -> {
                if (e.damage == 0.0) {
                    if (e.isApplicable(EntityDamageEvent.DamageModifier.ABSORPTION)) {
                        e.setDamage(EntityDamageEvent.DamageModifier.ABSORPTION, 0.0)
                    }
                }
            }

            else -> {}
        }
    }

    private fun setAttackSpeed(player: Player) {
        player.maximumNoDamageTicks = 16

        val attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return
        attribute.baseValue = 6.5
    }
}