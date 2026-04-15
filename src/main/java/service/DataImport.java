package service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Product;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class DataImport {

    @Transactional
    public void loadInitialData(@Observes StartupEvent ev){
        if(Product.count() > 0){
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Product>> typeReference = new TypeReference<List<Product>>(){};

            // Datei aus resources laden
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("products.json");
            List<Product> products = mapper.readValue(inputStream, typeReference);

            for (Product p : products) {
                p.id = null; // Zwingt Hibernate, eine neue ID zu vergeben
            }

            // In die DB speichern
            Product.persist(products);
        } catch (Exception e) {
            System.out.println("Fehler beim JSON-Import: " + e.getMessage());
        }
    }
}
