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

// TODO Cambiar por tabla guardada en flash
typedef struct {
        uint8_t pin;
        char* device;
        char* data;
        uint8_t action;
} Message;

const Message message[] = {
    {27, "pool_light", "on", 1},
    {27, "pool_light", "off", 0},
    {32, "swimming_pool_filter", "on", 0},
    {32, "swimming_pool_filter", "off", 1}
};

class MessageManager {
    private:
        void findDeviceData(const char* device, const char* data, uint8_t &pin, uint8_t &action);

    public:
        MessageManager();
        void proccess(char *topic, byte *payload, unsigned int length);
};

#endif