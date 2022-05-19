@ECHO OFF
SET IP="localhost"
SET SUBJECT_CA="/C=ES/ST=Spain/L=Granada/O=Granada/OU=CA/CN=%IP%"
SET SUBJECT_SERVER="/C=ES/ST=Spain/L=Granada/O=Granada/OU=Server/CN=%IP%"
SET SUBJECT_CLIENT="/C=ES/ST=Spain/L=Granada/O=Granada/OU=Client/CN=%IP%"
echo %SUBJECT_CA%
openssl req -x509 -nodes -sha256 -newkey rsa:2048 -subj %SUBJECT_CA%  -days 3650 -keyout ca.key -out ca.crt
echo %SUBJECT_SERVER%
openssl req -nodes -sha256 -new -subj %SUBJECT_SERVER% -keyout server.key -out server.csr
openssl x509 -req -sha256 -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 3650
echo %SUBJECT_CLIENT%
openssl req -new -nodes -sha256 -subj %SUBJECT_CLIENT% -out client.csr -keyout client.key 
openssl x509 -req -sha256 -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt -days 3650
