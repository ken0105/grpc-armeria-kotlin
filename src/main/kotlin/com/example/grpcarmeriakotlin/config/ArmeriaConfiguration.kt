package com.example.grpcarmeriakotlin.config

import com.linecorp.armeria.common.grpc.GrpcMeterIdPrefixFunction
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.TransientHttpService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.healthcheck.HealthCheckService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.server.metric.MetricCollectingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.protobuf.services.ProtoReflectionService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaConfiguration(val services: BindableService) {
    private var meterRegistry: MeterRegistry? = null

    fun ArmeriaConfig(meterRegistry: MeterRegistry?) {
        this.meterRegistry = meterRegistry
    }

    @Bean
    fun armeriaServerConfigurator(): ArmeriaServerConfigurator? {
        return ArmeriaServerConfigurator { builder: ServerBuilder ->
            builder.service(
                "/health",
                HealthCheckService.of()
                    .decorate(
                        TransientHttpService.newDecorator()
                    )
            )
            builder.serviceUnder("/docs", DocService())
            builder.decorator(LoggingService.newDecorator())
            builder.decorator(MetricCollectingService.newDecorator(GrpcMeterIdPrefixFunction.of("grpc.service")))
            builder.accessLogWriter(AccessLogWriter.combined(), false)
            builder.service(
                GrpcService.builder()
                    .addServices(services)
                    .addService(ProtoReflectionService.newInstance())
                    .supportedSerializationFormats(GrpcSerializationFormats.values())
                    .enableUnframedRequests(true)
                    .useBlockingTaskExecutor(true)
                    .build()
            )
//            builder.meterRegistry(meterRegistry!!)
        }
    }
}