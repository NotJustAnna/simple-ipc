package net.notjustanna.utils.ipc.client

import net.notjustanna.utils.ipc.proto.ConnectionState

class ClientException(val state: ConnectionState, message: String?, cause: Throwable?) : Exception(message, cause) {
    constructor(state: ConnectionState, message: String?) : this(state, message, null)

    constructor(state: ConnectionState, cause: Throwable?) : this(state, cause?.toString(), cause)

    constructor(state: ConnectionState) : this(state, null, null)
}