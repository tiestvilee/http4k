package org.reekwest.http.core

import java.nio.ByteBuffer

typealias HttpHandler = (Request) -> Response

typealias HttpClient = HttpHandler

typealias Headers = Parameters

typealias Body = ByteBuffer

fun String.toBody(): Body = ByteBuffer.wrap(toByteArray())

interface Filter : (HttpHandler) -> HttpHandler {
    companion object {
        operator fun invoke(fn: (HttpHandler) -> HttpHandler): Filter = object : Filter {
            operator override fun invoke(next: HttpHandler): HttpHandler = fn(next)
        }
    }
}

fun Filter.then(next: Filter): Filter = Filter { this(next(it)) }

fun Filter.then(next: HttpHandler): HttpHandler = { this(next)(it) }

