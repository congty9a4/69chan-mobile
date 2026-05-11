package com.mycom.chan.data.local.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import com.mycom.chan.domain.repository.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManagerImple @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {

    companion object {
        const val ACCOUNT_TYPE = "com.mycom.chan" //To recognize between app, different fromt facebook, X,etc...
        const val REFRESH_TOKEN = "refresh_token"
        const val KEY_USER = "user_data"
        const val AUTH_TOKEN_TYPE = "Bearer"
    }

    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    private val accountManager: AccountManager = AccountManager.get(context)
    @Volatile private var cachedRefreshToken: String? = null

    init {
        // Load initial state from AccountManager (sync, ok ở init)
        _accessTokenFlow.value = readAccessTokenFromAccountManager()
    }
    private fun readAccessTokenFromAccountManager(): String? {
        val account = accountManager.getAccountsByType(ACCOUNT_TYPE).firstOrNull() ?: return null
        return accountManager.peekAuthToken(account, AUTH_TOKEN_TYPE)
    }
    /**
     * find first (firstornull) account in android's account manager that has com.mycom.chan type
     **/
    private suspend fun getCurrentAccount(): Account? = withContext(Dispatchers.IO) {
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)
        accounts.firstOrNull()
    }

    /**
     * create new account for account manager
     * account manager need an object to save authtoken
     **/
    private suspend fun createAccount(userName:String): Account = withContext(Dispatchers.IO) {
        Account(userName, ACCOUNT_TYPE).also { newAccount ->
            accountManager.addAccountExplicitly(newAccount, null, null)
        }
    }

    /**
     * if no account has been created (first time use app), we will create a new account.
     This account is named "69chan-user"
     * if account has been created before, we will replace (invalidate) the older token with a new token.
    Token must have tagged as "Bearer"
     **/
    override suspend fun saveAccessToken(accessToken: String): Unit = withContext(Dispatchers.IO) {
        saveAccessTokenBlocking(accessToken)
    }
    /**
     * save refresh token into user's data
     **/
    override suspend fun saveRefreshToken(refreshToken: String) {
        saveRefreshTokenBlocking(refreshToken)
    }

    /**
     * get access token, return null if no account founded
     **/
    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        _accessTokenFlow.value
    }

    /**
     * get refresh token, return null if no account founded
     **/
    override suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        getRefreshTokenBlocking()
    }

    override suspend fun observeAccessToken(): Flow<String?> = _accessTokenFlow.asStateFlow()

    /**
     * We have to invalidate the token of the account before removing it
     * First of all, we have to find the account with our package name "com.mycom.chan"
     * After that, we use peekAuthToken to see if the old access token still be in the account manager
     * IF yes, we call invalidateAuthToken to "delete" the token
     * Then we use removeAccountExplicitly to remove the account.
     **/
    override suspend fun clearToken() = withContext(Dispatchers.IO) {
        clearTokenBlocking()
    }

    /**
     * **/
    override suspend fun saveUser(user: String) {
        withContext(Dispatchers.IO) {
            val account = getCurrentAccount() ?: run {
                val newAccount = Account("69chan-user", ACCOUNT_TYPE)
                accountManager.addAccountExplicitly(newAccount, null, null)
                newAccount
            }
            accountManager.setUserData(account, KEY_USER, user)
        }
    }

    /**
     * **/
    override suspend fun getUser(): String? = withContext(Dispatchers.IO) {
        val account = getCurrentAccount() ?: return@withContext null
        accountManager.getUserData(account, KEY_USER)
    }

    private fun getCurrentAccountSync(): Account? =
        accountManager.getAccountsByType(ACCOUNT_TYPE).firstOrNull()

    override fun getAccessTokenBlocking(): String? {
        return _accessTokenFlow.value
    }

    override fun getRefreshTokenBlocking(): String? {
        cachedRefreshToken?.let { return it }
        val account = getCurrentAccountSync() ?: return null
        return accountManager.getUserData(account, REFRESH_TOKEN)
            ?.also { cachedRefreshToken = it }
    }

    override fun saveAccessTokenBlocking(accessToken: String) {
        // Lấy account hiện tại sync, hoặc tạo mới nếu chưa có
        val account = getCurrentAccountSync() ?: run {
            val newAccount = Account("69chan-user", ACCOUNT_TYPE)
            accountManager.addAccountExplicitly(newAccount, null, null)
            newAccount
        }
        accountManager.invalidateAuthToken(ACCOUNT_TYPE, accessToken)
        accountManager.setAuthToken(account, AUTH_TOKEN_TYPE, accessToken)
        _accessTokenFlow.value = accessToken                    // emit cho observer
    }

    override fun saveRefreshTokenBlocking(refreshToken: String) {
        val account = getCurrentAccountSync() ?: run {
            val newAccount = Account("69chan-user", ACCOUNT_TYPE)
            accountManager.addAccountExplicitly(newAccount, null, null)
            newAccount
        }
        accountManager.setUserData(account, REFRESH_TOKEN, refreshToken)
        cachedRefreshToken = refreshToken
    }

    override fun clearTokenBlocking() {
        val account = getCurrentAccountSync() ?: return
        runCatching {
            accountManager.peekAuthToken(account, AUTH_TOKEN_TYPE)?.let { token ->
                accountManager.invalidateAuthToken(ACCOUNT_TYPE, token)
            }
            accountManager.removeAccountExplicitly(account)
        }
        _accessTokenFlow.value = null
        cachedRefreshToken = null
    }
}