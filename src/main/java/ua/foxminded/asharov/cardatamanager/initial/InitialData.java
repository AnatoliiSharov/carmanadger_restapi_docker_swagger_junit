package ua.foxminded.asharov.cardatamanager.initial;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitialData {
    public static final String UPLOADING_FILE = "file.csv";
    public static final String WRONG_DATA_FILE = "response.csv";

    private final InitialUtil initUtil;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        String filePlacePath = UPLOADING_FILE;
        List<String[]> inData = initUtil.loadResource(filePlacePath);
        
        for (String[] row : inData) {
            initUtil.printOut(initUtil.dataTreatment(row), WRONG_DATA_FILE);
        }
    }   
        
}
