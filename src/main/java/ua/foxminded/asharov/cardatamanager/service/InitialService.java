package ua.foxminded.asharov.cardatamanager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface InitialService {

    @Transactional
    public void cleanDataBase();
    
    @Transactional
    public void loadDataBaseFromFile(String makeName, String modelName, String yearName, String categoryNames, String objectIdName);

}
