#!/usr/bin/env bash
# Simple script to import realm.json via Keycloak Admin REST (requires kcadmin or direct REST; this is a lightweight placeholder)
# Ensure KEYCLOAK_HOST (default http://localhost:8180) and realm.json path
KC_HOST=${KC_HOST:-http://localhost:8180}
REALM_FILE=${REALM_FILE:-./keycloak-config/realm.json}

if [ ! -f "$REALM_FILE" ]; then
  echo "Realm file not found: $REALM_FILE"
  exit 1
fi

# Use Keycloak 'start-dev --import-realm' or import via Admin console; here we just show how to call Admin API if admin credentials known
echo "If Keycloak is in dev mode with --import-realm, the realm should be imported automatically."

echo "To import manually, open Admin Console: $KC_HOST/auth -> Realms -> Import"

echo "Placeholder script finished."
