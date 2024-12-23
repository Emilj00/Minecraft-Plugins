package me.emiljoo.minecraftplugins.utilities.extensions

import org.bukkit.block.Block
import org.bukkit.util.Vector


fun Block.getPositionAsVector(): Vector {
    return this.location.toVector()
}