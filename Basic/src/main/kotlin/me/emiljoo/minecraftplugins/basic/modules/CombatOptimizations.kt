package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
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
    private lateinit var disableKnockbackResistanceConfig: ConfigField<Boolean>
    private lateinit var enableSnowballKnockbackConfig: ConfigField<Boolean>
    private lateinit var attackSpeedConfig: ConfigField<Double>
    private lateinit var noDamageTicksConfig: ConfigField<Int>

    override fun onEnable(plugin: EnhancedPlugin) {
        val configManager: ConfigManager = plugin.configManager

        disableKnockbackResistanceConfig = ConfigField(configManager, "combat-optimizations.disable-knockback-resistance", true)
        enableSnowballKnockbackConfig = ConfigField(configManager, "combat-optimizations.enable-snowball-knockback", true)
        attackSpeedConfig = ConfigField(configManager, "combat-optimizations.attack-speed", 4.0)
        noDamageTicksConfig = ConfigField(configManager, "combat-optimizations.no-damage-ticks", 15)

        configManager.onConfigChangedEvent += ::onConfigChanged
    }

    private fun onConfigChanged(configManager: ConfigManager) {
        Bukkit.getOnlinePlayers().forEach { p -> setAttributes(p) }
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        setAttributes(event.player)
    }

    @EventHandler
    private fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (!disableKnockbackResistanceConfig.get()) {
            return
        }

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
        if (!enableSnowballKnockbackConfig.get()) {
            return
        }

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

    private fun setAttributes(player: Player) {
        val attackSpeedAttribute: AttributeInstance = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return
        attackSpeedAttribute.baseValue = attackSpeedConfig.get()

        player.maximumNoDamageTicks = noDamageTicksConfig.get()
    }
}