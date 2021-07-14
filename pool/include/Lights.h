#ifndef LIGHTS_H
#define LIGHTS_H

const byte LIGTHS_GPIO = 26;

class Lights {
    int gpio;

    public:
        Lights();

        int  getGPIOConfig();
        void setGPIOConfig(int gpio);
        void lightsOn();
        void lightsOff();
};

#endif