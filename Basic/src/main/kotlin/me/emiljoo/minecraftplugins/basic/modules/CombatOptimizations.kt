package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent


class CombatOptimizations : EnhancedModule() {
    private lateinit var disableKnockbackResistance: ConfigField<Boolean>
    private lateinit var enableSnowballKnockbackConfigField: ConfigField<Boolean>
    private lateinit var attackSpeedConfigField: ConfigField<Double>
    private lateinit var noDamageTicksConfigField: ConfigField<Int>

    override fun onEnable(plugin: EnhancedPlugin) {
        val configManager: ConfigManager = plugin.configManager

        disableKnockbackResistance = ConfigField(configManager, "modules.combat-optimizations.disable-knockback-resistance", true)
        enableSnowballKnockbackConfigField = ConfigField(configManager, "modules.combat-optimizations.enable-snowball-knockback", true)
        attackSpeedConfigField = ConfigField(configManager, "modules.combat-optimizations.attack-speed", 4.0)
        noDamageTicksConfigField = ConfigField(configManager, "modules.combat-optimizations.no-damage-ticks", 10)
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        setAttackSpeed(event.player)
    }

    @EventHandler
    private fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (!disableKnockbackResistance.get()) {
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
        if (!enableSnowballKnockbackConfigField.get()) {
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

    private fun setAttackSpeed(player: Player) {
        val attackSpeedAttribute: AttributeInstance = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return

        player.maximumNoDamageTicks = noDamageTicksConfigField.get()
        attackSpeedAttribute.baseValue = attackSpeedConfigField.get()
    }
}