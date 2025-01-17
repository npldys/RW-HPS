package cn.rwhps.server.plugin.beta.httpapi.handlers

import cn.rwhps.server.net.handler.tcp.GamePortWebSocket
import cn.rwhps.server.net.http.WebSocket
import cn.rwhps.server.plugin.beta.httpapi.ConfigHelper.config
import io.netty.channel.Channel
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.util.AttributeKey

abstract class BaseWsHandler : WebSocket() {
    override fun ws(ws: GamePortWebSocket, channel: Channel, msg: String) {
        val key = channel.attr(WS_KEY)
        var type = key.get()

        // 新的连接
        if (type == null) {
            type = Data()
            key.setIfAbsent(type)
            if (msg == config.token) {
                type.canAccess = true
            } else {
                channel.writeAndFlush(TextWebSocketFrame("invalid token"))
                channel.close()
            }
        } else if(type.canAccess) {
            run(msg, channel)
        }
    }

    abstract fun run(msg: String, channel: Channel)

    companion object {
        val WS_KEY = AttributeKey.valueOf<Data>("Wss-Key")!!
    }

    class Data {
        var canAccess = false
    }
}