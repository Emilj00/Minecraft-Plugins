package me.emiljoo.minecraftplugins.utilities

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.util.Vector

fun Player.isGrounded(): Boolean {
    val loc = this.location
    val blockAtFeet = loc.block
    val blockBelowFeet = blockAtFeet.getRelative(0, -1, 0)

    return blockBelowFeet.type.isSolid
}

fun Block.getPositionAsVector(): Vector {
    return this.location.toVector()
}