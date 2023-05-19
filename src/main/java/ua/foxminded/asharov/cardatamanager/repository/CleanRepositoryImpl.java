package ua.foxminded.asharov.cardatamanager.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CleanRepositoryImpl {

    @Autowired
    EntityManagerFactory factory;

    @PersistenceContext
    EntityManager em;
    
    @Transactional
    public void deleteCar() {
        em.createNativeQuery("DELETE FROM car").executeUpdate();
    }
    
    @Transactional
    public void deleteModelAndYear() {
        em.createNativeQuery("DELETE FROM model_year").executeUpdate();
    }
    
    @Transactional
    public void deleteModelAndCategory() {
        em.createNativeQuery("DELETE FROM model_category").executeUpdate();
    }
    
    @Transactional
    public void deleteModel() {
        em.createNativeQuery("DELETE FROM model").executeUpdate();
    }
    
    @Transactional
    public void deleteManufacturer() {
        em.createNativeQuery("DELETE FROM manufacturer").executeUpdate();
    }
    

    
    @Transactional
    public void deleteYear() {
        em.createNativeQuery("DELETE FROM year").executeUpdate();
    }
    

    
    @Transactional
    public void deleteCategory() {
        em.createNativeQuery("DELETE FROM category").executeUpdate();
    }
    

}
