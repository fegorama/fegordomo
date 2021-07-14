#ifndef DEVICEEXTERNAL_H
#define DEVICEEXTERNAL_H

class DeviceExternal {
    private:
        String id;
        String name;
        byte gpio;
        byte type;
        String schedule;
    
    public:
        DeviceExternal(String id, String name, byte gpio, byte type, String schedule);
        void switchOn();
        void switchOff();
};

#endif