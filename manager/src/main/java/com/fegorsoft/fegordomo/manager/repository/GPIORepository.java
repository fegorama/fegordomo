package com.fegorsoft.fegordomo.manager.repository;

import java.util.List;

import com.fegorsoft.fegordomo.manager.dto.GPIODTO;
import com.fegorsoft.fegordomo.manager.model.GPIO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GPIORepository extends JpaRepository<GPIO, Long> {
    
    @Query("SELECT g FROM GPIO AS g")
    List<GPIO> findAll();

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.GPIODTO(g.id, g.gpio, g.mode, g.status, g.cronTriggerOn, g.cronTriggerOff) FROM GPIO g WHERE g.id = :id")
    GPIODTO findByIdtoDTO(Long id);

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.GPIODTO(g.id, g.gpio, g.mode, g.status, g.cronTriggerOn, g.cronTriggerOff) FROM GPIO g WHERE g.device.id = :deviceId")
    List<GPIODTO> findByDeviceIdtoDTO(Long deviceId);

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.GPIODTO(g.id, g.gpio, g.mode, g.status, g.cronTriggerOn, g.cronTriggerOff) FROM GPIO g")
    List<GPIODTO> findAlltoDTO();
}
