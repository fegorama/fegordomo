#include <Arduino.h>
#include <unity.h>

const byte LED_BUILTIN_TREATMENTSYSTEM = 27;
const byte LED_BUILTIN_LIGHS = 26;

void testGPIOsNumber(void) {
    TEST_ASSERT_EQUAL(27, LED_BUILTIN_TREATMENTSYSTEM);
    TEST_ASSERT_EQUAL(26, LED_BUILTIN_LIGHS);
}

void testTreatmentSystemStateOn(void) {
    digitalWrite(27, HIGH);
    TEST_ASSERT_EQUAL(HIGH, digitalRead(27));
}

void testLightsOn(void) {
    digitalWrite(26, HIGH);
    TEST_ASSERT_EQUAL(HIGH, digitalRead(26));
}

void testTreatmentSystemStateOff(void) {
    digitalWrite(27, LOW);
    TEST_ASSERT_EQUAL(LOW, digitalRead(27));
}

void testLightsOff(void) {
    digitalWrite(26, LOW);
    TEST_ASSERT_EQUAL(LOW, digitalRead(26));
}

void setup() {
    delay(2000); // if board doesn't support software reset via Serial.DTR/RTS

    UNITY_BEGIN();
    RUN_TEST(testGPIOsNumber);

    pinMode(LED_BUILTIN_TREATMENTSYSTEM, OUTPUT);
    pinMode(LED_BUILTIN_LIGHS, OUTPUT);

}

void loop() {
    RUN_TEST(testTreatmentSystemStateOn);
    delay(1000);
    RUN_TEST(testLightsOn);
    delay(1000);
    RUN_TEST(testTreatmentSystemStateOff);
    delay(1000);
    RUN_TEST(testLightsOff);    
    delay(1000);
    UNITY_END();
}