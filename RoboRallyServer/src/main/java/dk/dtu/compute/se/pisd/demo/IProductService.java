package dk.dtu.compute.se.pisd.demo;

import java.util.List;
public interface IProductService
{
    List<Product> findAll();
    public Product getProductById(int id);
    boolean addProduct(Product p);
    public boolean updateProduct(int id, Product p);
    public boolean deleteProductById(int id);
}