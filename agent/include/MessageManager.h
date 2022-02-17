/**
 * @file 	MessageManager.h
 * @brief 	Gestión de mensajes
 * 
 * @author 	Fernando González (Fegor)
 * @date 	17/02/2022
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef MESSAGEMANAGER_H
#define MESSAGEMANAGER_H
 
using namespace std;

class MessageManager {
    private:

    public:
        MessageManager();
        void proccess(char *topic, byte *payload, unsigned int length);
};

#endif