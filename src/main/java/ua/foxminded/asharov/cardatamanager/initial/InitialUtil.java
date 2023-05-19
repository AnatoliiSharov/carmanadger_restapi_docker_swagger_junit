package ua.foxminded.asharov.cardatamanager.initial;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.exception.ApiException;
import ua.foxminded.asharov.cardatamanager.service.InitialService;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialUtil {
    private static final String OBJECT_ID_RULE = "[a-zA-Z1-9]{10}";
    private static final String NAME_RULE = "^[a-zA-Z1-9][a-zA-Z0-9_\\. \\-]{1,100}$";
    private static final String YEAR_NAME_RULE = "[12][890][0-9][0-9]";

    private final InitialService initialServ;

    public List<String[]> loadResource(String filePlasePath) {
        List<String[]> results = new ArrayList<>();
        log.debug("InitialUtil.loadResource started with filePlasePath = {}", filePlasePath);
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Reader reader = new FileReader(classLoader.getResource(filePlasePath).getFile());
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(HeadColomn.HEADER_SET_IN).setSkipHeaderRecord(true).build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            results = StreamSupport.stream(records.spliterator(), false)
                    .map(result -> new String[] { ResultColomn.PROCESS.name(), Long.toString(result.getRecordNumber()),
                            result.get(HeadColomn.objectId), result.get(HeadColomn.Make), result.get(HeadColomn.Year), result.get(HeadColomn.Model),
                            result.get(HeadColomn.Category) })
                    .collect(Collectors.toList());
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    public String[] dataTreatment(String[] row) throws ApiException {
        log.debug("InitialUtil.dataTreatment started with row = {}", row);
        String[] result = row;
        String report = row[0];
        String position = row[1];
        String objectIdName = row[2];
        String makeName = row[3];
        String yearName = row[4];
        String modelName = row[5];
        String categoryNames = row[6];

        if (isOkManufacturerName(makeName) && isOkModelName(modelName) && isOkYearName(yearName)
                && isOkCategoryName(categoryNames) && isOkObjectIdName(objectIdName)) {
            try {
                initialServ.loadDataBaseFromFile(makeName, modelName, yearName, categoryNames, objectIdName);
                row[0] = ResultColomn.APPROVAL.name();
            } finally {
                
                if(result[0] != null  && !result[0].equals(ResultColomn.APPROVAL.name())) {
                    result[0] = ResultColomn.DENIED.name();
                }
            }

        } else {
            result[0] = ResultColomn.DENIED.toString();
        }
        return result;
    }

    

    public void printOut(String[] row, String filePlacePath) {
        log.debug("InitialUtil.printOut started with row = {}, filePlacePath = {}", row, filePlacePath);
        
        if(row[0] !=null && row[0].equals(ResultColomn.DENIED.name())) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePlacePath));
                CSVPrinter csvPrinter = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.builder().setHeader(HeadColomn.HEADER_SET_OUT).build());) {
            csvPrinter.printRecord(row);
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    public boolean isOkObjectIdName(String string) {
        return string != null && Pattern.matches(OBJECT_ID_RULE, string);
    }

    public boolean isOkCategoryName(String string) {
        return string != null && Pattern.matches(NAME_RULE, string);
    }

    public boolean isOkYearName(String string) {
        return string != null && Pattern.matches(YEAR_NAME_RULE, string);
    }

    public boolean isOkModelName(String string) {
        return string != null && Pattern.matches(NAME_RULE, string);
    }

    public boolean isOkManufacturerName(String string) {
        return string != null && Pattern.matches(NAME_RULE, string);
    }

}
