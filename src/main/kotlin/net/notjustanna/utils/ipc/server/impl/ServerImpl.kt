package net.notjustanna.utils.ipc.server.impl

import net.notjustanna.utils.io.DataPipe
import net.notjustanna.utils.ipc.proto.Protocol
import net.notjustanna.utils.ipc.server.IServer
import net.notjustanna.utils.ipc.server.connector.ServerConnector
import net.notjustanna.utils.ipc.server.handler.ConnectionHandler
import java.net.SocketException
import java.util.concurrent.ExecutorService
import kotlin.concurrent.thread

class ServerImpl<T>(
    private val proto: Protocol,
    private val serverName: String,
    private val connector: ServerConnector<T>,
    private val executor: ExecutorService,
    private val calls: Map<String, T.(DataPipe) -> Unit>,
    private val extensions: Map<Byte, T.(DataPipe) -> Unit>
) : IServer {

    private val thread: Thread = thread(name = "$serverName/ListeningThread") {
        try {
            while (!executor.isShutdown) {
                executor.execute(ConnectionHandler(serverName, connector.awaitNextConnection(), calls, extensions, proto))
            }
        } catch (_: InterruptedException) {
        } catch (_: SocketException) {
        }
    }

    /**
     * Closes the executor, the listening thread and the server socket, freeing the resources.
     */
    override fun close() {
        executor.shutdown()
        thread.interrupt()
    }
}