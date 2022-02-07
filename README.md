## How to run

1. Generate a certificate: `keytool -genkeypair -alias oauth -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore oauth.p12 -validity 3650` and place it under classpath i.e. `src/main/resources`
2. Register an application in Slack.
3. Whitelist `https://localhost:8080/oauth2/slack/callback` as a redirect URL.
4. Run `SLACK_CLIENT_ID=xxx SLACK_CLIENT_SECRET=yyy ./gradlew bootRun`
5. Surf `https://localhost:8080/oauth2/authorization/slack` and follow the OAuth2 flow.
