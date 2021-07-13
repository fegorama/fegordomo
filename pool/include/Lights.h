#ifndef LIGHTS_H
#define LIGHTS_H

class Lights {
    int gpio;

    public:
        Lights();

        int  getGPIOConfig();
        void setGPIOConfig(int gpio);
        void lightsOn();
        void lightsOff();
};

#define LIGTHS_GPIO             28

#endif