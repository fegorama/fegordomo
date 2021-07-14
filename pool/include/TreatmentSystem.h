#ifndef TREATMENTSYSTEM_H
#define TREATMENTSYSTEM_H

const byte TREATMENT_SYSTEM_GPIO = 27;

class TreatmentSystem {
    int gpio;

    public:
        TreatmentSystem();

        int  getGPIOConfig();
        void setGPIOConfig(int gpio);
        void treatmentSystemOn();
        void treatmentSystemOff();
};

#endif