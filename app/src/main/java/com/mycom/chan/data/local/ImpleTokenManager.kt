package com.mycom.chan.data.local

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.content.Context
import com.mycom.chan.domain.repository.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImpleTokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {

    companion object {
        const val ACCOUNT_TYPE = "com.mycom.chan" //To recognize between app, different fromt facebook, X,etc...
        const val REFRESH_TOKEN = "refresh_token"
        const val KEY_USER = "user_data"
        const val AUTH_TOKEN_TYPE = "Bearer"
    }

    private val accountManager: AccountManager = AccountManager.get(context)

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
        val account = getCurrentAccount() ?: createAccount("69chan-user")
        accountManager.invalidateAuthToken(ACCOUNT_TYPE, accessToken)
        accountManager.setAuthToken(account,AUTH_TOKEN_TYPE,accessToken)
    }
    /**
     * save refresh token into user's data
     **/
    override suspend fun saveRefreshToken(refreshToken: String) {
        withContext(Dispatchers.IO) {
            getCurrentAccount()?.let { account ->
                accountManager.setUserData(account, REFRESH_TOKEN, refreshToken)
            }
        }
    }

    /**
     * get access token, return null if no account founded
     **/
    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        val account = getCurrentAccount() ?: return@withContext null
        accountManager.peekAuthToken(account,ACCOUNT_TYPE)
    }

    /**
     * get refresh token, return null if no account founded
     **/
    override suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        val account = getCurrentAccount() ?: return@withContext null
        accountManager.getUserData(account, REFRESH_TOKEN)
    }

    /**
     * whenever account had a change (add,update,add,delete), OnAccountUpdateListener will
     tell (callback, asynch) us when that happen
     * and when that happened, we create a coroutine to get access token from our func. After that,
     we use trySend to push it into the flow so UI can know
     * after all of that, we call accountManager to "register" an actual listener and close it when we close the app
     **/
    override suspend fun observeAccessToken(): Flow<String?> = callbackFlow {
        val listener = OnAccountsUpdateListener { accounts->
            launch {
                val accessToken = getAccessToken()
                trySend(accessToken)
            }
        }
        accountManager.addOnAccountsUpdatedListener(listener, null, true)
        awaitClose {
            accountManager.removeOnAccountsUpdatedListener(listener)
        }
    }

    /**
     * We have to invalidate the token of the account before removing it
     * First of all, we have to find the account with our package name "com.mycom.chan"
     * After that, we use peekAuthToken to see if the old access token still be in the account manager
     * IF yes, we call invalidateAuthToken to "delete" the token
     * Then we use removeAccountExplicitly to remove the account.
     **/
    override suspend fun clearToken() = withContext(Dispatchers.IO) {
        val account = getCurrentAccount() ?: return@withContext
        runCatching { //crashing avoidance
            accountManager.peekAuthToken(account, AUTH_TOKEN_TYPE)?.let { token->
                accountManager.invalidateAuthToken(ACCOUNT_TYPE,token)
            }
            val isRemoved = accountManager.removeAccountExplicitly(account)

            if (!isRemoved) {
                println("Could not remove account explicitly.")
            }

        }.onFailure { e->
            e.printStackTrace()
        }
    }

    /**
     * **/
    override suspend fun saveUser(user: String) {
        withContext(Dispatchers.IO) {
            val account = getCurrentAccount()
            account?.let {
                accountManager.setUserData(it, KEY_USER, user)
            }
        }
    }

    /**
     * **/
    override suspend fun getUser(): String? = withContext(Dispatchers.IO) {
        val account = getCurrentAccount() ?: return@withContext null
        accountManager.getUserData(account, KEY_USER)
    }

}

