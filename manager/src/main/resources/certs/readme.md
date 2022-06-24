sudo openssl pkcs12 -export -in client.crt -inkey client.key -out fegordomo.p12 -name fegordomo_client -CAfile ca.crt -caname root
sudo keytool -importkeystore -deststorepass fegordomocerts -destkeypass fegordomocerts -destkeystore fegordomo.jks -srckeystore fegordomo.p12 -srcstoretype PKCS12 -srcstorepass fegordomocerts -alias fegordomo_client


Pass: iottest

