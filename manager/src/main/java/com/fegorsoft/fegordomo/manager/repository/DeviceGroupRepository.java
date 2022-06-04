package com.fegorsoft.fegordomo.manager.repository;

import java.util.List;

import com.fegorsoft.fegordomo.manager.dto.DeviceGroupDTO;
import com.fegorsoft.fegordomo.manager.model.DeviceGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.DeviceGroupDTO(dg.id, dg.name) FROM Device dg")
    List<DeviceGroupDTO> findAlltoDTO();

    @Query("SELECT new com.fegorsoft.fegordomo.manager.dto.DeviceGroupDTO(dg.id, dg.name) FROM Device dg WHERE dg.id = :deviceGroupId")
    DeviceGroupDTO findByIdtoDTO(Long deviceGroupId);
}
