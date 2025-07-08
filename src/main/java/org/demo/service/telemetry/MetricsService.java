package org.demo.service.telemetry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Span;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MetricsService {

    private final Tracer tracer;

    // Contadores para operaciones CRUD
    private final Counter spaceShipCreatedCounter;
    private final Counter spaceShipUpdatedCounter;
    private final Counter spaceShipDeletedCounter;
    private final Counter spaceShipReadCounter;

    // Contadores para mensajes de auditoría
    private final Counter auditMessagesSentCounter;
    private final Counter kafkaMessagesSentCounter;

    // Timers para medir duración de operaciones
    private final Timer databaseOperationTimer;
    private final Timer externalServiceTimer;

    // Constructor explícito para inyección de dependencias
    public MetricsService(MeterRegistry meterRegistry, Tracer tracer) {
        this.tracer = tracer;

        // Inicializar contadores Micrometer
        this.spaceShipCreatedCounter = Counter.builder("spaceship.created")
                .description("Number of spaceships created")
                .register(meterRegistry);

        this.spaceShipUpdatedCounter = Counter.builder("spaceship.updated")
                .description("Number of spaceships updated")
                .register(meterRegistry);

        this.spaceShipDeletedCounter = Counter.builder("spaceship.deleted")
                .description("Number of spaceships deleted")
                .register(meterRegistry);

        this.spaceShipReadCounter = Counter.builder("spaceship.read")
                .description("Number of spaceship read operations")
                .register(meterRegistry);

        this.auditMessagesSentCounter = Counter.builder("audit.messages.sent")
                .description("Number of audit messages sent to RabbitMQ")
                .register(meterRegistry);

        this.kafkaMessagesSentCounter = Counter.builder("kafka.messages.sent")
                .description("Number of messages sent to Kafka")
                .register(meterRegistry);

        // Inicializar timers
        this.databaseOperationTimer = Timer.builder("database.operation.duration")
                .description("Duration of database operations")
                .register(meterRegistry);

        this.externalServiceTimer = Timer.builder("external.service.duration")
                .description("Duration of external service calls")
                .register(meterRegistry);
    }

    // Métodos para incrementar contadores
    public void incrementSpaceShipCreated() {
        spaceShipCreatedCounter.increment();
        log.debug("SpaceShip created counter incremented");
    }

    public void incrementSpaceShipUpdated() {
        spaceShipUpdatedCounter.increment();
        log.debug("SpaceShip updated counter incremented");
    }

    public void incrementSpaceShipDeleted() {
        spaceShipDeletedCounter.increment();
        log.debug("SpaceShip deleted counter incremented");
    }

    public void incrementSpaceShipRead() {
        spaceShipReadCounter.increment();
        log.debug("SpaceShip read counter incremented");
    }

    public void incrementAuditMessagesSent() {
        auditMessagesSentCounter.increment();
        log.debug("Audit message sent counter incremented");
    }

    public void incrementKafkaMessagesSent() {
        kafkaMessagesSentCounter.increment();
        log.debug("Kafka message sent counter incremented");
    }

    // Métodos para medir duración con Timer
    public void recordDatabaseOperationTime(long duration, TimeUnit timeUnit) {
        databaseOperationTimer.record(duration, timeUnit);
        log.debug("Database operation duration recorded: {} {}", duration, timeUnit);
    }

    public void recordExternalServiceTime(long duration, TimeUnit timeUnit) {
        externalServiceTimer.record(duration, timeUnit);
        log.debug("External service duration recorded: {} {}", duration, timeUnit);
    }

    public <T> T executeWithTracing(String operationName, java.util.function.Supplier<T> operation) {
        Span span = tracer.nextSpan().name(operationName).start();
        long startTime = System.nanoTime();

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
            T result = operation.get();
            span.tag("status", "success");
            return result;
        } catch (Exception e) {
            span.tag("status", "error");
            span.tag("error.message", e.getMessage());
            throw e;
        } finally {
            long duration = System.nanoTime() - startTime;
            recordDatabaseOperationTime(duration, TimeUnit.NANOSECONDS);
            span.end();
        }
    }

    // Método para operaciones void con tracing
    public void executeWithTracing(String operationName, Runnable operation) {
        executeWithTracing(operationName, () -> {
            operation.run();
            return null;
        });
    }
}
