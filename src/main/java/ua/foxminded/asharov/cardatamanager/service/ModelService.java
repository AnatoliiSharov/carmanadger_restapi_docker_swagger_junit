package ua.foxminded.asharov.cardatamanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.asharov.cardatamanager.dto.ModelDto;
import ua.foxminded.asharov.cardatamanager.entity.Model;

@Service
public interface ModelService {

    @Transactional(readOnly = true)
    List<ModelDto> retrieveByManufacturerName(String manufacturerName);

    @Transactional
    ModelDto enter(String manufacturerName, ModelDto modelDto);

    @Transactional
    void removeByManufacturerNameByModelName(String manufacturerName, String modelName);
    
    @Transactional(readOnly = true)
    ModelDto retrieveByManufacturerNameByModelName(String manufacturerName, String modelName);
    
    @Transactional(readOnly = true)
    Model retrieveEntityByManufacturerNameByModelName(String manufacturerName, String modelName);

}
