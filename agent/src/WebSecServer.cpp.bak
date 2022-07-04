/**
 * @file 	WebSecServer.cpp
 * @brief 	Servidor Web Seguro
 * 
 * @author 	Fernando González (Fegor)
 * @date 	11/10/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#include <ArduinoJson.h>
#include <Stream.h>
#include <HTTPSServer.hpp>
#include <SSLCert.hpp>
#include <HTTPRequest.hpp>
#include <HTTPResponse.hpp>
#include <functional>

#include "WebSecServer.h"

#define HEADER_USERNAME "X-USERNAME"
#define HEADER_GROUP    "X-GROUP"

using namespace httpsserver;

WebSecServer::WebSecServer() : secureServer() {}

/**
 * Inicio del servicio web
 */
void WebSecServer::initServer()
{
    Serial.println("Creating certificate...");   
    cert = new SSLCert();
    
    int createCertResult = createSelfSignedCert(
        *cert,
        KEYSIZE_1024,
        "CN=fegor.local,O=fegordomo,C=ES",
        "20190101000000",
        "20300101000000");
    
    if (createCertResult != 0) {
        Serial.printf("Error generating certificate");
        return;
    }
    
    Serial.println("Certificate created with success");

    secureServer = new HTTPSServer(cert);

    ResourceNode * nodeRoot = new ResourceNode("/", "GET", [](HTTPRequest * req, HTTPResponse * res){
      res->println("Secure Hello World!!!");
    });

    ResourceNode * resourceHealth = new ResourceNode("/health", "GET", [](HTTPRequest *req, HTTPResponse *res) {
        res->setHeader("Content-Type", "application/json");
        res->println("{\"health\" : \"OK\"}");
    });

    ResourceNode * resourceGPIO = new ResourceNode("/gpio", "POST", [](HTTPRequest *req, HTTPResponse *res) {
        res->setHeader("Content-Type","application/json");

        String bodyContent;
        byte buffer[256];

        while(!(req->requestComplete())) {
            size_t s = req->readBytes(buffer, 256);
            bodyContent.concat(s);
        }

        StaticJsonDocument<200> doc;
        DeserializationError error = deserializeJson(doc, bodyContent);

        if (error)
        {
            req->discardRequestBody();
            res->setStatusCode(400);
            res->setStatusText("Bad Request");
            res->println("{\"status\" : \"ERROR\"}");
            return;
        }

        const byte gpio = doc["gpio"];
        const byte mode = doc["mode"];
        const byte action = doc["action"];

        pinMode(gpio, mode);
        digitalWrite(gpio, action);

        Serial.println(bodyContent);
        res->setStatusCode(200);
        res->println(bodyContent);        
    });

    secureServer->registerNode(nodeRoot);
    secureServer->registerNode(resourceHealth);
    secureServer->registerNode(resourceGPIO);

    //secureServer->addMiddleware(&middlewareAuthentication);
    //secureServer->addMiddleware(&middlewareAuthorization);

    Serial.println("Starting server...");
    secureServer->start();

    if (secureServer->isRunning()) {
        Serial.println("Server ready.");

    } else {
        Serial.println("Server could not be started.");
    }
}

boolean WebSecServer::isRunning() {
  return secureServer->isRunning();
}

void middlewareAuthentication(HTTPRequest * req, HTTPResponse * res, std::function<void()> next) {
  Serial.println("Authentication");
  // Unset both headers to discard any value from the client
  // This prevents authentication bypass by a client that just sets X-USERNAME
  req->setHeader(HEADER_USERNAME, "");
  req->setHeader(HEADER_GROUP, "");

  // Get login information from request
  // If you use HTTP Basic Auth, you can retrieve the values from the request.
  // The return values will be empty strings if the user did not provide any data,
  // or if the format of the Authorization header is invalid (eg. no Basic Method
  // for Authorization, or an invalid Base64 token)
  std::string reqUsername = req->getBasicAuthUser();
  std::string reqPassword = req->getBasicAuthPassword();

  // If the user entered login information, we will check it
  if (reqUsername.length() > 0 && reqPassword.length() > 0) {

    // _Very_ simple hardcoded user database to check credentials and assign the group
    bool authValid = true;
    std::string group = "";
    if (reqUsername == "admin" && reqPassword == "secret") {
      group = "ADMIN";
    } else if (reqUsername == "user" && reqPassword == "test") {
      group = "USER";
    } else {
      authValid = false;
    }

    // If authentication was successful
    if (authValid) {
      Serial.println("Authentication successful!");

      // set custom headers and delegate control
      req->setHeader(HEADER_USERNAME, reqUsername);
      req->setHeader(HEADER_GROUP, group);
      
      // The user tried to authenticate and was successful
      // -> We proceed with this request.
      next();
    } else {
      Serial.println("Authentication fail!");

      // Display error page
      res->setStatusCode(401);
      res->setStatusText("Unauthorized");
      res->setHeader("Content-Type", "text/plain");

      // This should trigger the browser user/password dialog, and it will tell
      // the client how it can authenticate
      res->setHeader("WWW-Authenticate", "Basic realm=\"ESP32 privileged area\"");

      // Small error text on the response document. In a real-world scenario, you
      // shouldn't display the login information on this page, of course ;-)
      res->println("401. Unauthorized (try admin/secret or user/test)");

      // NO CALL TO next() here, as the authentication failed.
      // -> The code above did handle the request already.
    }
  } else {
    Serial.println("Authentication not available");

    // No attempt to authenticate
    // -> Let the request pass through by calling next()
    next();
  }
}

/**
 * This function plays together with the middlewareAuthentication(). While the first function checks the
 * username/password combination and stores it in the request, this function makes use of this information
 * to allow or deny access.
 *
 * This example only prevents unauthorized access to every ResourceNode stored under an /internal/... path.
 */
void middlewareAuthorization(HTTPRequest * req, HTTPResponse * res, std::function<void()> next) {
  // Get the username (if any)
  std::string username = req->getHeader(HEADER_USERNAME);

  // Check that only logged-in users may get to the internal area (All URLs starting with /internal)
  // Only a simple example, more complicated configuration is up to you.
  if (username == "" && req->getRequestString().substr(0,9) == "/internal") {
    Serial.println("Autorization successful!");

    // Same as the deny-part in middlewareAuthentication()
    res->setStatusCode(401);
    res->setStatusText("Unauthorized");
    res->setHeader("Content-Type", "text/plain");
    res->setHeader("WWW-Authenticate", "Basic realm=\"ESP32 privileged area\"");
    res->println("401. Unauthorized (try admin/secret or user/test)");

    // No call denies access to protected handler function.
  } else {
    // Everything else will be allowed, so we call next()
    next();
  }
}

