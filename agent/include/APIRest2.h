/**
 * @file 	APIRest.h
 * @brief 	API de llamadas a RESTful
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef APIREST2_H
#define APIREST2_H

#include <HTTPSServer.hpp>
#include <HTTPRequest.hpp>
#include <HTTPResponse.hpp>

class APIRest2
{
private:
    String getBodyContent(uint8_t *data, size_t len);

public:
    APIRest2();
    void health(HTTPRequest * req, HTTPResponse * res);
    void gpio(HTTPRequest *request, HTTPResponse *res, uint8_t *data, size_t len, size_t index, size_t total);
    void notFound(HTTPRequest *req, HTTPResponse *res);
};

#endif