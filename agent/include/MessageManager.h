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
        String device;
        String data;
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
        void findDeviceData(const String device, const String data, uint8_t &pin, uint8_t &action);

    public:
        MessageManager();
        void proccess(const char *topic, const byte *payload, const unsigned int length);
        void execute(String device, String data);
};

#endif