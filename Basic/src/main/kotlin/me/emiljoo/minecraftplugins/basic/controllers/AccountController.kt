package me.emiljoo.minecraftplugins.basic.controllers

import me.emiljoo.minecraftplugins.basic.data.Account
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

class AccountController(plugin: EnhancedPlugin) {
    companion object {
        fun getVoidLocation(): Location {
            val startingWorld: World = Bukkit.getWorlds()[0]
            return Location(startingWorld, 0.0, -80.0, 0.0)
        }
    }

    enum class LoginFailureReason {
        USER_NOT_REGISTERED,
        INCORRECT_PASSWORD,
        USER_ALREADY_LOGGED_IN;

        companion object {
            private var userNotRegisteredMessage: String = "You are not registered!"
            private var incorrectPasswordMessage: String = "Incorrect password!"
            private var userAlreadyLoggedInMessage: String = "You are already logged in!"

            fun initializeMessages(
                userNotRegistered: String,
                incorrectPassword: String,
                userAlreadyLoggedIn: String
            ) {
                userNotRegisteredMessage = userNotRegistered
                incorrectPasswordMessage = incorrectPassword
                userAlreadyLoggedInMessage = userAlreadyLoggedIn
            }
        }

        fun getMessage(): String {
            return when (this) {
                USER_NOT_REGISTERED -> userNotRegisteredMessage
                INCORRECT_PASSWORD -> incorrectPasswordMessage
                USER_ALREADY_LOGGED_IN -> userAlreadyLoggedInMessage
            }
        }
    }

    data class LoginResult(
        val success: Boolean,
        val location: Location?,
        val failureReason: LoginFailureReason?
    )


    private val usersConfigManager: ConfigManager = ConfigManager(plugin, "usersdb.yml")
    private val authenticatedPlayers: MutableList<Player> = mutableListOf()

    private val userNotRegisteredMessageConfigField: ConfigField<String> =
        ConfigField(usersConfigManager, "accounts.login.user-not-registered-message", "You are not registered!")
    private val incorrectPasswordMessageConfigField: ConfigField<String> =
        ConfigField(usersConfigManager, "accounts.login.incorrect-password-message", "Incorrect password!")
    val userAlreadyLoggedInMessageConfigField: ConfigField<String> =
        ConfigField(usersConfigManager, "accounts.login.user-already-logged-in-message", "You are already logged in!")

    init {
        LoginFailureReason.initializeMessages(
            userNotRegisteredMessageConfigField.get(),
            incorrectPasswordMessageConfigField.get(),
            userAlreadyLoggedInMessageConfigField.get()
        )
    }


    private fun getUser(username: String): Account? {
        val userSectionPath = "users.$username"
        val passwordField = ConfigField(usersConfigManager, "$userSectionPath.password", "")
        val worldUuidField = ConfigField(usersConfigManager, "$userSectionPath.worldUuid", "")
        val posXField = ConfigField(usersConfigManager, "$userSectionPath.xPosition", 0.0)
        val posYField = ConfigField(usersConfigManager, "$userSectionPath.yPosition", 0.0)
        val posZField = ConfigField(usersConfigManager, "$userSectionPath.zPosition", 0.0)

        val password = passwordField.get()
        if (password.isEmpty()) return null

        val worldUuid = worldUuidField.get()
        val posX = posXField.get()
        val posY = posYField.get()
        val posZ = posZField.get()

        return Account(username, password, worldUuid, posX, posY, posZ)
    }

    private fun saveUser(account: Account) {
        val userSectionPath = "users.${account.username}"
        ConfigField(usersConfigManager, "$userSectionPath.password", "").set(account.password)
        ConfigField(usersConfigManager, "$userSectionPath.worldUuid", "").set(account.worldUuid)
        ConfigField(usersConfigManager, "$userSectionPath.xPosition", 0.0).set(account.xPosition)
        ConfigField(usersConfigManager, "$userSectionPath.yPosition", 0.0).set(account.yPosition)
        ConfigField(usersConfigManager, "$userSectionPath.zPosition", 0.0).set(account.zPosition)
        usersConfigManager.saveConfig()
    }

    fun registerPlayer(player: Player, password: String): Boolean {
        val username = player.name

        if (isUserRegistered(username)) return false

        val playerLocation: Location = player.location
        val worldUuid: String = playerLocation.world!!.uid.toString()

        val account = Account(username, password, worldUuid, playerLocation.x, playerLocation.y, playerLocation.z)
        saveUser(account)

        authenticatedPlayers.add(player)
        return true
    }

    fun loginPlayer(player: Player, password: String): LoginResult {
        val username: String = player.name
        val account: Account = getUser(username) ?: return LoginResult(false, null, LoginFailureReason.USER_NOT_REGISTERED)

        if (account.password != password) {
            return LoginResult(false, null, LoginFailureReason.INCORRECT_PASSWORD)
        }

        if (authenticatedPlayers.contains(player)) {
            return LoginResult(false, null, LoginFailureReason.USER_ALREADY_LOGGED_IN)
        }

        authenticatedPlayers.add(player)

        val world: World = Bukkit.getWorld(UUID.fromString(account.worldUuid)) ?: Bukkit.getWorlds()[0]
        val playerLocation = Location(world, account.xPosition, account.yPosition, account.zPosition)

        return LoginResult(true, playerLocation, null)
    }

    fun logoutPlayer(player: Player) {
        authenticatedPlayers.remove(player)

        val playerLocation: Location = player.location
        val account: Account = getUser(player.name)!!

        account.worldUuid = playerLocation.world!!.uid.toString()
        account.xPosition = playerLocation.x
        account.yPosition = playerLocation.y
        account.zPosition = playerLocation.z

        saveUser(account)
    }


    fun isUserAuthenticated(username: String): Boolean {
        val player: Player = Bukkit.getPlayerExact(username) ?: return false
        return authenticatedPlayers.contains(player)
    }

    fun isUserRegistered(username: String): Boolean {
        return getUser(username) != null
    }

    fun reloadConfig() {
        usersConfigManager.reloadConfig()
    }
}
