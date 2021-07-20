package com.fegorsoft.fegordomo.manager.repository;

import org.springframework.data.repository.CrudRepository;
import com.fegorsoft.fegordomo.manager.model.Device;

public interface DeviceRepository extends CrudRepository<Device, Integer> {
    
}
