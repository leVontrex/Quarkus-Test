package ressource;

import model.Product;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductRessource {
    @GET
    public List<Product> list(){
        return Product.listAll();
    }

    @POST
    @jakarta.transaction.Transactional
    public Product add(Product product) {
        product.persist();
        return product;
    }
}
