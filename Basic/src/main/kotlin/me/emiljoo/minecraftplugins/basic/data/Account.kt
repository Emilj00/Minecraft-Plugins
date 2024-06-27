package me.emiljoo.minecraftplugins.basic.data

data class Account(
    val username: String,
    val password: String,

    var worldUuid: String,
    var xPosition: Double,
    var yPosition: Double,
    var zPosition: Double,
)