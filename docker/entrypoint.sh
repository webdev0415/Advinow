#!/bin/bash

echo "Loading configuration from Vault"
cat /usr/share/env/env.txt > ./2070Services/WEB-INF/classes/application.properties
cat /usr/share/env/env.txt > ./classes/application.properties

exec "$@"
