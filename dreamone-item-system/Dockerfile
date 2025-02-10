from registry.cn-beijing.aliyuncs.com/hub-mirrors/openjdk:11

# 复制OpenTelemetry agent
COPY opentelemetry-javaagent.jar /root/opentelemetry-javaagent.jar

COPY target/dreamone-item-system.jar /root/dreamone-item-system.jar

# 启动命令
ENTRYPOINT ["java", \
    "-javaagent:/root/opentelemetry-javaagent.jar", \
    "-Dotel.resource.attributes=service.name=dreamone-order-system,service.version=v1.0.0,deployment.environment=prod", \
    "-Dotel.exporter.otlp.protocol=http/protobuf", \
    "-Dotel.exporter.otlp.traces.endpoint=${OTEL_TRACES_ENDPOINT}", \
    "-Dotel.exporter.otlp.metrics.endpoint=${OTEL_METRICS_ENDPOINT}", \
    "-Dotel.logs.exporter=none", \
    "-jar", \
    "/root/dreamone-item-system.jar"]