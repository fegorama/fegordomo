/**
 * @file 	HttpsServer.h
 * @brief 	Servidor Web Seguro
 * 
 * @author 	Fernando González (Fegor)
 * @date 	11/10/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef WEBSECSERVER_H
#define WEBSECSERVER_H

#include <HTTPSServer.hpp>
#include <SSLCert.hpp>
#include <HTTPRequest.hpp>
#include <HTTPResponse.hpp>

using namespace httpsserver;

// TODO Include to WebSecServer class...
void middlewareAuthentication(HTTPRequest * req, HTTPResponse * res, std::function<void()> next);
void middlewareAuthorization(HTTPRequest * req, HTTPResponse * res, std::function<void()> next);

class WebSecServer
{
private:
    SSLCert *cert;

public:
    WebSecServer();
    void initServer();
    HTTPSServer *secureServer;
};

#endif
