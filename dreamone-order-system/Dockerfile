FROM registry.cn-beijing.aliyuncs.com/hub-mirrors/openjdk:11

# 复制OpenTelemetry agent
COPY opentelemetry-javaagent.jar /root/opentelemetry-javaagent.jar

# 复制应用jar包
COPY target/dreamone-order-system.jar /root/dreamone-order-system.jar

# 设置环境变量（你需要替换实际的endpoint地址）
ENV OTEL_TRACES_ENDPOINT="http://your-traces-endpoint:4317"
ENV OTEL_METRICS_ENDPOINT="http://your-metrics-endpoint:4317"

# 启动命令
ENTRYPOINT ["java", \
    "-javaagent:/root/opentelemetry-javaagent.jar", \
    "-Dotel.exporter.otlp.protocol=http/protobuf", \
    "-Dotel.exporter.otlp.traces.endpoint=${OTEL_TRACES_ENDPOINT}", \
    "-Dotel.exporter.otlp.metrics.endpoint=${OTEL_METRICS_ENDPOINT}", \
    "-Dotel.logs.exporter=none", \
    "-jar", \
    "/root/dreamone-order-system.jar"]
