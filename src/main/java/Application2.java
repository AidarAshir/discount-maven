import entity.Category;
import entity.Discount;
import entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Application2 {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("main");

    public static void main(String[] args) {
        //необходимо отразить все товары и все скидки в сумме, которые на них действуют, включая скидку на категорию.
        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            TypedQuery<Product> productTypedQuery = manager.createQuery(
                    "select p from Product p", Product.class
            );
            List<Product> productsList = productTypedQuery.getResultList();

            TypedQuery<Category> categoryTypedQuery = manager.createQuery(
                    "select c from Category c", Category.class
            );
            List<Category> categoriesList = categoryTypedQuery.getResultList();

            Integer sumCDiscounts = 0;
            Integer sumPDiscounts = 0;

            for (Product product : productsList) {

                for (Discount discount : product.getDiscounts()) {
                    sumPDiscounts = sumPDiscounts + discount.getPercent();
                }

                for (Discount discountCategory : product.getCategory().getDiscounts()) {
                    sumCDiscounts = sumCDiscounts + discountCategory.getPercent();
                }
                System.out.println(product.getName() + " - " + (sumPDiscounts + sumCDiscounts));
                sumPDiscounts = 0;
                sumCDiscounts = 0;
            }
            manager.getTransaction().commit();
        } catch (
                Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
