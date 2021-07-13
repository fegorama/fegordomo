#ifndef TREATMENTSYSTEM_H
#define TREATMENTSYSTEM_H

class TreatmentSystem {
    int gpio;

    public:
        TreatmentSystem();

        int  getGPIOConfig();
        void setGPIOConfig(int gpio);
        void treatmentSystemOn();
        void treatmentSystemOff();
};

#define TREATMENT_SYSTEM_GPIO   27

#endif