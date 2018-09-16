@file:JvmName("IPC")
@file:JvmMultifileClass

package net.notjustanna.utils.ipc.server

import net.notjustanna.utils.ipc.proto.DefaultProtocol
import net.notjustanna.utils.ipc.proto.Protocol
import net.notjustanna.utils.ipc.server.connector.ServerConnectorFactory
import net.notjustanna.utils.ipc.server.dsl.ServerBuilder
import net.notjustanna.utils.ipc.server.impl.ServerImpl
import java.util.concurrent.ExecutorService

/**
 * Creates and configures an [IServer].
 *
 * @param serverName The server's name, sent to the client on the handshake.
 * @param port The server's port.
 * @param executor (Optional) The [ExecutorService] used to handle the clients asynchronously.
 * @param block The configuration of the server.
 * @return the configurated and running [IServer].
 */
fun <B : ServerBuilder<T>, T> ipcServer(
    factory: ServerConnectorFactory<B, T>, protocol: Protocol = DefaultProtocol, block: B.() -> Unit
): IServer {
    val builder = factory.makeBuilder(protocol).apply(block)
    val connector = factory.buildConnector(builder)

    return ServerImpl(
        protocol,
        builder.serverName,
        connector,
        builder.executor(),
        builder.calls,
        builder.extensions
    )
}