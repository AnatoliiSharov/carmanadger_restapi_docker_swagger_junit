package ua.foxminded.asharov.cardatamanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.asharov.cardatamanager.dto.YearDto;

@Service
public interface YearService {

    @Transactional(readOnly = true)
    List<YearDto> retrieveAllByModelByManufacturer(String manufacturerName, String modelName);

    @Transactional
    YearDto enterByManufacrureNameByModelNameByYearDto(String manufacturerName, String modelName, YearDto yearDto);

    @Transactional
    void removeYearByModelNameByManufacturerNameByYearName(String manufacturerName, String modelName, String year);

    @Transactional(readOnly = true)
    YearDto retrieveByModelByManufacturerByYearName(String manufacturerName, String modelName, String year);

}
