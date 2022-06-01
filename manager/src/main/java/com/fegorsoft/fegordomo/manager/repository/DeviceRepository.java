package com.fegorsoft.fegordomo.manager.repository;

import java.util.List;

import com.fegorsoft.fegordomo.manager.dto.DeviceDTO;
import com.fegorsoft.fegordomo.manager.model.Device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.DeviceDTO(d.id, d.name, d.type, d.description, d.enable) FROM Device d")
    List<DeviceDTO> findAlltoDTO();
}
