package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.dto.ManufacturerDto;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.exception.ResourceAlreadyExistsException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.service.ManufacturerService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {
    private static final String MANUFACTURER_SERVICE_IMPL = "ManufacturerServiceImpl";

    private final ManufacturerRepository manufacturerRep;

    @Override
    public List<ManufacturerDto> retrieveAll() {
        log.debug("ManufacturerRepository.retrieveAll started");
        return toDtoList(manufacturerRep.findAll());
    }

    @Override
    public ManufacturerDto retrieveByName(String manufacturerName) {
        log.debug("ManufacturerRepository.retrieveByName started with manufacturerName = {}", manufacturerName);
        List<Manufacturer> result = manufacturerRep.findByName(manufacturerName);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException(MANUFACTURER_SERVICE_IMPL, "manufacturerName =" + manufacturerName);
        } else {
            return toDto(result.get(0));
        }
    }

    @Override
    public ManufacturerDto enter(ManufacturerDto manufacturerDto) {
        log.debug("ManufacturerRepository.enter started with manufacturerName = {}", manufacturerDto);
        
        if (manufacturerDto.getId() != null) {
            Manufacturer result = manufacturerRep.findById(manufacturerDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(MANUFACTURER_SERVICE_IMPL,
                            "manufacturerDto =" + manufacturerDto));

            if (manufacturerDto.getName()!=null && !manufacturerDto.getName().equals(result.getName())) {

                if (!manufacturerRep.existsByName(manufacturerDto.getName())) {
                    return toDto(manufacturerRep.save(toEntity(manufacturerDto)));
                } else {
                    throw new ResourceAlreadyExistsException(MANUFACTURER_SERVICE_IMPL,
                            "manufacturerDto.name =" + manufacturerDto);
                }
            } else {
                return manufacturerDto;
            }
        } else {

            if (!manufacturerRep.existsByName(manufacturerDto.getName())) {
                return toDto(manufacturerRep.save(toEntity(manufacturerDto)));
            } else {
                throw new ResourceAlreadyExistsException(MANUFACTURER_SERVICE_IMPL,
                        "manufacturerDto.name =" + manufacturerDto);
            }
        }
    }

    @Override
    public void removeByName(String manufacturerName) {
        log.debug("ManufacturerRepository.removeByName started with manufacturerName = {}", manufacturerName);
        List<Manufacturer> result = manufacturerRep.findByName(manufacturerName);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException(MANUFACTURER_SERVICE_IMPL, "manufacturerName =" + manufacturerName);
        } else {
            manufacturerRep.delete(result.get(0));
        }
    }

    public ManufacturerDto toDto(Manufacturer entity) {
        return new ManufacturerDto(entity.getId(), entity.getName()); 
    }

    public Manufacturer toEntity(ManufacturerDto dto) {
        return new Manufacturer(dto.getId(), dto.getName());
    }

    public List<ManufacturerDto> toDtoList(List<Manufacturer> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ManufacturerDto> toDtoList(Iterable<Manufacturer> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).map(this::toDto).collect(Collectors.toList());
    }

}
