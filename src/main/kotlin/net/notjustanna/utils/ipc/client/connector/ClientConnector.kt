package net.notjustanna.utils.ipc.client.connector

import net.notjustanna.utils.io.DataPipe

interface ClientConnector<T> {
    fun createConnection(): ClientConnection<T>
}

data class ClientConnection<T>(val connection: T, val pipe: DataPipe)