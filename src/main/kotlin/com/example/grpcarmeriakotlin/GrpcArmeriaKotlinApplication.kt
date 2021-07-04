package com.example.grpcarmeriakotlin

import com.example.grpcarmeriakotlin.config.ArmeriaConfiguration
import com.linecorp.armeria.common.SessionProtocol
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcArmeriaKotlinApplication

fun main(args: Array<String>) {
    runApplication<GrpcArmeriaKotlinApplication>(*args)
}

