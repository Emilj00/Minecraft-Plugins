package me.emiljoo.minecraftplugins.utilities.extensions

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player

fun Player.isPremium(): Boolean {
    return false;
}

fun Player.isGrounded(): Boolean {
    val loc: Location = this.location
    val blockAtFeet: Block = loc.block

    val blockBelowFeet: Block = blockAtFeet.getRelative(0, -1, 0)

    return blockBelowFeet.type.isSolid
}
