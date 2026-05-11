package com.mycom.chan.data.remote.websocket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.mycom.chan.di.MainClient
import com.mycom.chan.domain.repository.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

// ── WebSocket payload models ──────────────────────────────────────────────────

data class WsIncomingMessage(
    @SerializedName("id") val id: Long,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String
)

data class WsAck(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("status") val status: String      // "SENT"
)

data class WsSendPayload(
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("content") val content: String,
    @SerializedName("type") val type: String = "TEXT"
)

// ----------------- StompClient ----------------

private const val WS_URL = "wss://six9chan-2.onrender.com/ws-69chan"
private const val TAG = "StompClient"

@Singleton
class StompClient @Inject constructor(
    @MainClient private val okHttpClient: OkHttpClient,
    private val tokenManager: TokenManager,
    private val gson: Gson
) {
    private var webSocket: WebSocket? = null
    private var subId = 0

    private val _messages = MutableSharedFlow<WsIncomingMessage>(extraBufferCapacity = 64)
    val messages: SharedFlow<WsIncomingMessage> = _messages.asSharedFlow()

    private val _acks = MutableSharedFlow<WsAck>(extraBufferCapacity = 64)
    val acks: SharedFlow<WsAck> = _acks.asSharedFlow()

    val isConnected: Boolean get() = webSocket != null

    // Gọi từ coroutine — suspend để lấy token trước khi mở socket
    suspend fun connect() {
        if (webSocket != null) return
        val token = tokenManager.getAccessToken() ?: run {
            Log.e(TAG, "No access token, cannot connect")
            return
        }
        val request = Request.Builder().url(WS_URL).build()
        webSocket = okHttpClient.newWebSocket(request, Listener(token))
    }

    fun sendMessage(receiverId: String, content: String) {
        val ws = webSocket ?: run { Log.w(TAG, "Not connected"); return }
        val payload = gson.toJson(WsSendPayload(receiverId, content))
        ws.send(stompSend("/app/chat.send", payload))
    }

    fun disconnect() {
        webSocket?.send("DISCONNECT\n\n\u0000")
        webSocket?.close(1000, "Disconnected")
        webSocket = null
        subId = 0
    }

    // -------------------- STOMP frame builders ------------------

    private fun stompConnect(token: String) =
        "CONNECT\nAccept-Version:1.2\nAuthorization:Bearer $token\n\n\u0000"

    private fun stompSubscribe(destination: String) =
        "SUBSCRIBE\nid:sub-${subId++}\ndestination:$destination\n\n\u0000"

    private fun stompSend(destination: String, body: String) =
        "SEND\ndestination:$destination\ncontent-type:application/json\n\n$body\u0000"

    // ------- WebSocket listener ----------------

    private inner class Listener(private val token: String) : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "WebSocket opened")
            webSocket.send(stompConnect(token))
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val command = text.substringBefore("\n").trim()
            when (command) {
                "CONNECTED" -> {
                    Log.d(TAG, "STOMP connected")
                    webSocket.send(stompSubscribe("/user/queue/messages"))
                    webSocket.send(stompSubscribe("/user/queue/acks"))
                }
                "MESSAGE" -> handleMessage(text)
                "ERROR"   -> Log.e(TAG, "STOMP error: $text")
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "WebSocket failure: ${t.message}")
            this@StompClient.webSocket = null
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WebSocket closed: $reason")
            this@StompClient.webSocket = null
        }
    }

    private fun handleMessage(frame: String) {
        val destination = frame.lines()
            .find { it.startsWith("destination:") }
            ?.removePrefix("destination:")
            ?.trim() ?: return

        val body = frame.substringAfter("\n\n").trimEnd('\u0000')

        when {
            destination.contains("messages") -> runCatching {
                _messages.tryEmit(gson.fromJson(body, WsIncomingMessage::class.java))
            }
            destination.contains("acks") -> runCatching {
                _acks.tryEmit(gson.fromJson(body, WsAck::class.java))
            }
        }
    }
}