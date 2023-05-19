package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.dto.YearDto;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLink;
import ua.foxminded.asharov.cardatamanager.entity.Year;
import ua.foxminded.asharov.cardatamanager.exception.ResourceIsStillUsedException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.CarRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelAndYearLinkRepository;
import ua.foxminded.asharov.cardatamanager.repository.YearRepository;
import ua.foxminded.asharov.cardatamanager.service.ModelService;
import ua.foxminded.asharov.cardatamanager.service.YearService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class YearServiceImpl implements YearService {
    private static final String YEAR_SERVICE_IMPL = "YearServiceImpl";

    private final ModelAndYearLinkRepository modelAndYearRep;
    private final YearRepository yearRep;
    private final CarRepository carRep;
    private final ModelService modelServ;

    @Override
    public List<YearDto> retrieveAllByModelByManufacturer(String manufacturerName, String modelName) {
        log.debug("YearServiceImpl.retrieveAllByModelByManufacturer started with manufacturerName = {}, modelName = {}",
                manufacturerName, modelName);
        List<ModelAndYearLink> modelAndYearLinkList = modelAndYearRep
                .findByModel(modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName));

        if (!modelAndYearLinkList.isEmpty()) {
            return toDtoList(modelAndYearLinkList.stream().map(ModelAndYearLink::getYear).collect(Collectors.toList()));
        } else {
            throw new ResourceNotFoundException(YEAR_SERVICE_IMPL,
                    "model = " + modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName));
        }
    }

    @Override
    public YearDto retrieveByModelByManufacturerByYearName(String manufacturerName, String modelName, String yearName) {
        log.debug(
                "YearServiceImpl.retrieveByModelByManufacturerByYearName started with manufacturerName = {}, modelName = {}, yearName = {}",
                manufacturerName, modelName, yearName);
        Model model = modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName);
        List<Year> interimYear = yearRep.findByYear(Integer.parseInt(yearName));
        if (interimYear.isEmpty()) {
            throw new ResourceNotFoundException(YEAR_SERVICE_IMPL, "year=" + yearName);
        }
        return toDto(modelAndYearRep.findById(new ModelAndYearLink(model, interimYear.get(0)).getId())
                .orElseThrow(() -> new ResourceNotFoundException(YEAR_SERVICE_IMPL,
                        "ModelAndYearLink = " + new ModelAndYearLink(model, interimYear.get(0))))
                .getYear());
    }

    @Override
    public YearDto enterByManufacrureNameByModelNameByYearDto(String manufacturerName, String modelName,
            YearDto yearDto) {
        log.debug(
                "YearServiceImpl.enterByManufacrureNameByModelNameByYearDto started with manufacturerName = {}, modelName = {}, yearDto = {}",
                manufacturerName, modelName, yearDto);
        Model model = modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName);
        List<Year> interimYear = yearRep.findByYear(yearDto.getYear());
        Year year;

        if (!interimYear.isEmpty()) {
            year = interimYear.get(0);
        } else {
            year = yearRep.save(toEntity(yearDto));
        }
        ModelAndYearLink result = modelAndYearRep.findById(new ModelAndYearLink(model, year).getId())
                .orElse(modelAndYearRep.save(new ModelAndYearLink(model, year)));
        return toDto(result.getYear());
    }

    @Override
    public void removeYearByModelNameByManufacturerNameByYearName(String manufacturerName, String modelName,
            String yearName) {
        log.debug(
                "YearServiceImpl.removeYearByModelNameByManufacturerNameByYearName started with manufacturerName = {}, modelName = {}, yearName = {}",
                manufacturerName, modelName, yearName);
        Model model = modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName);
        List<Year> interimYear = yearRep.findByYear(Integer.parseInt(yearName));

        if (interimYear.isEmpty()) {
            throw new ResourceNotFoundException(YEAR_SERVICE_IMPL, "year=" + yearName);
        }
        ModelAndYearLink modelAndYearLink = new ModelAndYearLink(model, interimYear.get(0));

        if (modelAndYearRep.existsById(modelAndYearLink.getId())) {

            if (carRep.findAllByYear(interimYear.get(0)).isEmpty()) {
                modelAndYearRep.delete(modelAndYearLink);

                if (modelAndYearRep.findAllByYear(interimYear.get(0)).isEmpty()) {
                    yearRep.delete(interimYear.get(0));
                }
            } else {
                throw new ResourceIsStillUsedException(YEAR_SERVICE_IMPL,
                        "cars =" + carRep.findAllByYear(interimYear.get(0)));
            }
        } else {
            throw new ResourceNotFoundException(YEAR_SERVICE_IMPL, "ModelAndYearLink=" + modelAndYearLink);
        }
    }

    private Year toEntity(YearDto dto) {
        Year result = new Year();

        result.setId(dto.getId());
        result.setYear(dto.getYear());
        return result;
    }

    private YearDto toDto(Year entity) {
        YearDto result = new YearDto();

        result.setId(entity.getId());
        result.setYear(entity.getYear());
        return result;
    }

    private List<YearDto> toDtoList(List<Year> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

}
