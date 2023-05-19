package ua.foxminded.asharov.cardatamanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.asharov.cardatamanager.dto.CarDto;
import ua.foxminded.asharov.cardatamanager.dto.SearchObject;

@Service
public interface CarService {
    
    @Transactional
    CarDto enter(CarDto build);

    @Transactional(readOnly = true)
    Page<CarDto> retrieveAll(Pageable pageable, SearchObject searchObject);

    
}
