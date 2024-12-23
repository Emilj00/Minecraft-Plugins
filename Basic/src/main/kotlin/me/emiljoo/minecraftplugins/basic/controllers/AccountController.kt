package me.emiljoo.minecraftplugins.basic.controllers

import me.emiljoo.minecraftplugins.basic.data.Account
import me.emiljoo.minecraftplugins.basic.enums.LoginFailureReason
import me.emiljoo.minecraftplugins.basic.enums.RegistrationResult
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Result
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class AccountController(plugin: EnhancedPlugin) {
    private val usersConfigManager: ConfigManager = ConfigManager(plugin, "usersdb.yml")
    private val authenticatedPlayers: MutableList<Player> = mutableListOf()

    private fun getUser(username: String): Account? {
        val userSectionPath = "users.$username"
        val passwordField = ConfigField(usersConfigManager, "$userSectionPath.password", "")

        val password: String = passwordField.get()
        if (password.isEmpty()) {
            return null
        }

        val worldUuid: String = ConfigField(usersConfigManager, "$userSectionPath.worldUuid", "").get()
        val posX: Double = ConfigField(usersConfigManager, "$userSectionPath.xPosition", 0.0).get()
        val posY: Double = ConfigField(usersConfigManager, "$userSectionPath.yPosition", 0.0).get()
        val posZ: Double = ConfigField(usersConfigManager, "$userSectionPath.zPosition", 0.0).get()

        return Account(username, password, worldUuid, posX, posY, posZ)
    }

    private fun savePlayer(player: Player) {
        val account: Account = getUser(player.name)!!
        val playerLocation: Location = player.location

        account.apply {
            worldUuid = playerLocation.world!!.uid.toString()
            playerLocation.x
            playerLocation.y
            playerLocation.z
        }

        saveAccount(account)
    }

    private fun saveAccount(account: Account) {
        val userSectionPath = "users.${account.username}"

        with(usersConfigManager) {
            ConfigField(this, "$userSectionPath.password", "").set(account.password)
            ConfigField(this, "$userSectionPath.worldUuid", "").set(account.worldUuid)
            ConfigField(this, "$userSectionPath.xPosition", 0.0).set(account.xPosition)
            ConfigField(this, "$userSectionPath.yPosition", 0.0).set(account.yPosition)
            ConfigField(this, "$userSectionPath.zPosition", 0.0).set(account.zPosition)

            saveConfig()
        }
    }

    fun registerPlayer(player: Player, password: String): RegistrationResult {
        val username = player.name

        if (isUserRegistered(username)) {
            return RegistrationResult.REGISTRATION_FAILED
        }

        authenticatedPlayers.add(player)

        val playerLocation: Location = player.location
        val worldUuid: String = playerLocation.world!!.uid.toString()

        val account = Account(username, password, worldUuid, playerLocation.x, playerLocation.y, playerLocation.z)
        saveAccount(account)

        return RegistrationResult.REGISTRATION_SUCCESS
    }

    fun loginPlayer(player: Player, password: String): Result<Location, LoginFailureReason> {
        val loginResult: Result<Location, LoginFailureReason> = performLogin(player, password)

        if (loginResult is Result.Success) {
            authenticatedPlayers.add(player)
        }

        return loginResult
    }

    fun loginPremiumPlayer(player: Player): Result<Location, LoginFailureReason> {
        authenticatedPlayers.add(player)

        val account = getUser(player.name) ?: return Result.Failure(LoginFailureReason.USER_NOT_REGISTERED)
        return performLogin(player, account.password)
    }

    private fun performLogin(player: Player, password: String): Result<Location, LoginFailureReason> {
        val account: Account = getUser(player.name) ?: return Result.Failure(LoginFailureReason.USER_NOT_REGISTERED)

        return validateLogin(player, account, password)
    }

    private fun validateLogin(player: Player, account: Account, password: String): Result<Location, LoginFailureReason> {
        if (authenticatedPlayers.contains(player)) {
            return Result.Failure(LoginFailureReason.USER_ALREADY_LOGGED_IN)
        }

        if (password.isNotEmpty() && account.password != password) {
            return Result.Failure(LoginFailureReason.INCORRECT_PASSWORD)
        }

        return Result.Success(account.getLocation())
    }

    fun logoutPlayer(player: Player) {
        authenticatedPlayers.remove(player)
        savePlayer(player)
    }

    fun isUserAuthenticated(username: String): Boolean {
        val player: Player = Bukkit.getPlayerExact(username) ?: return false
        return authenticatedPlayers.contains(player)
    }

    fun isUserRegistered(username: String): Boolean {
        return getUser(username) != null
    }

    fun reloadUsersDatabase() {
        usersConfigManager.reloadConfig()
    }
}
