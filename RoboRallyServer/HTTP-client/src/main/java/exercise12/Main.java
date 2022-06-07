package exercise12;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            int valg = 0;
            Scanner scan = new Scanner(System.in);
            IProductService ps = new ProductClient();

            while (valg != 6) {
                System.out.println("MENU TEST RESTFull API");
                System.out.println("\t1. Get all products");
                System.out.println("\t2. Get product");
                System.out.println("\t3. Add product");
                System.out.println("\t4. Update product");
                System.out.println("\t5. Delete product");
                System.out.println("\t6. EXIT");
                System.out.print("Enter option:");
                valg = scan.nextInt();
                switch (valg) {
                    case 1: {
                        String result = ProductClient.getProducts();
                        System.out.println(result);
                    }
                    break;
                    case 2: {
                        // String result = ProductClient.getProduct();
                        // System.out.println(result);
                        Product p = ps.getProductById(100);
                        System.out.println(p);

                    }
                    break;
                    case 3: {
                        Product p = new Product(200, "Micro Owen", 350.0);
                        boolean success = ps.addProduct(p);
                        System.out.println(success ? "Added product " + p : "Failed to add!");
                    }
                    break;
                    case 4: {
                        Product p = new Product(200, "Micro Owen", 350.0);
                        boolean success = ps.updateProduct(100, p);
                        System.out.println(success ? "Updated product 100" : "Failed to update!");
                    }
                    break;
                    case 5: {
                        boolean success = ps.deleteProductById(200);
                        System.out.println(success ? "Deleted product 200" : "Failed to delete!");
                    }
                    break;
                    case 6:
                        System.out.println("Bye");
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
            scan.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}