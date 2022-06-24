Crear con certbot los certificados de prod y desa:

sudo certbot certonly --manual -d des1.fegor.com --agree-tos --manual-public-ip-logging-ok --preferred-challenges dns-01 --server https://acme-v02.api.letsencrypt.org/directory --register-unsafely-without-email --rsa-key-size 4096
sudo certbot certonly --manual -d brokerdes.fegor.com --agree-tos --manual-public-ip-logging-ok --preferred-challenges dns-01 --server https://acme-v02.api.letsencrypt.org/directory --register-unsafely-without-email --rsa-key-size 4096
sudo certbot certonly --standalone -d broker.fegor.com --agree-tos --manual-public-ip-logging-ok --preferred-challenges dns-01 --server https://acme-v02.api.letsencrypt.org/directory --register-unsafely-without-email --rsa-key-size 4096

Letsencrypt to p12 to jks:

openssl pkcs12 -export -in cert1.pem -inkey privkey1.pem -out server.p12 -name server -CAfile chain1.pem -caname root
keytool -importkeystore -deststorepass iottest -destkeypass iottest -destkeystore serverkeystore.jks -srckeystore server.p12 -srcstoretype PKCS12 -srcstorepass iottest -alias server

https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-the-mosquitto-mqtt-messaging-broker-on-ubuntu-18-04-quickstart

TODO:
1. Hacer que cuando se renueve se genere el jks, se copie al sitio correspondiente de fegordomo-manager y se reinicie el servicio
2. Enviar los ficheros de certificados a los dispositivos
