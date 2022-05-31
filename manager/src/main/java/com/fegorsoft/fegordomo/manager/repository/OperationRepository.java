package com.fegorsoft.fegordomo.manager.repository;

import java.util.List;

import com.fegorsoft.fegordomo.manager.dto.OperationDTO;
import com.fegorsoft.fegordomo.manager.model.Operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    
    @Query("SELECT o FROM Operation AS o")
    List<Operation> findAll();

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.OperationDTO(o.id, o.data, o.mode, o.status, o.cronTriggerOn, o.cronTriggerOff) FROM Operation o WHERE o.id = :id")
    OperationDTO findByIdtoDTO(Long id);

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.OperationDTO(o.id, o.data, o.mode, o.status, o.cronTriggerOn, o.cronTriggerOff) FROM Operation o WHERE o.device.id = :deviceId")
    List<OperationDTO> findByDeviceIdtoDTO(Long deviceId);

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.OperationDTO(o.id, o.data, o.mode, o.status, o.cronTriggerOn, o.cronTriggerOff) FROM Operation o")
    List<OperationDTO> findAlltoDTO();
}
