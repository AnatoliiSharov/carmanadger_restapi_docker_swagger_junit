package ua.foxminded.asharov.cardatamanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.asharov.cardatamanager.dto.ManufacturerDto;

@Service
public interface ManufacturerService {

    @Transactional(readOnly = true)
    List<ManufacturerDto> retrieveAll();

    @Transactional(readOnly = true)
    ManufacturerDto retrieveByName(String nameMaker);

    @Transactional
    ManufacturerDto enter(ManufacturerDto manufacturerDto);

    @Transactional
    void removeByName(String makerName);

}
