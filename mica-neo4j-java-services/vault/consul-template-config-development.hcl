vault {
  renew_token = false
  vault_agent_token_file = "/home/vault/.vault-token"
  retry {
    backoff = "1s"
  }
}

template {
  destination = "/etc/secrets/env.txt"
  contents = <<EOH
{{- with secret "secret/DEVELOPMENT/JAVA_NEO4J" }}
org.springframework.data.rest.level=error
debug: false

spring.data.neo4j.uri={{ .Data.data.NEO4J_HOST }}
spring.data.neo4j.username={{ .Data.data.NEO4J_USERNAME }}
spring.data.neo4j.password={{ .Data.data.NEO4J_PASSWORD }}


logging.level.org.neo4j.ogm=error
logging.level.org.springframework.data.neo4j=error

spring.mvc.throw-exception-if-no-handler-found=true

mita.rest.url = {{ .Data.data.MITA_REST_URL }}

server.cache = true

server.cache.path = {{ .Data.data.CACHE_PATH }}

mica.illness.validation = true

resilience4j.retry.instances.neo4j.maxRetryAttempts=6
resilience4j.retry.instances.neo4j.waitDuration=1s
resilience4j.retry.instances.neo4j.enableExponentialBackoff=true
resilience4j.retry.instances.neo4j.exponentialBackoffMultiplier=2
resilience4j.retry.instances.neo4j.retryExceptions[0]=org.neo4j.driver.exceptions.SessionExpiredException
resilience4j.retry.instances.neo4j.retryExceptions[1]=org.neo4j.driver.exceptions.ServiceUnavailableException

# Enable response compression
server.compression.enabled=true

# The comma-separated list of mime types that should be compressed
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json

# Compress the response only if the response size is at least 1KB
server.compression.min-response-size=1024
server.http2.enabled=true
{{ end }}
EOH
}
