INSERT INTO fegordomo.devices
(id_pk, name, `type`)
VALUES(0, 'Depuradora', 'ESP32');

INSERT INTO fegordomo.gpios
(id_pk, mode, schedule, status, id_fk)
VALUES(0, 0, '0 0 3 * * *', 0, 0);
