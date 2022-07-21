import entity.Category;
import entity.Discount;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("main");

    public static void main(String[] args) {
        //необходимо отразить все категории и все скидки в сумме, которые на них действуют
        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            TypedQuery<Category> categoryTypedQuery = manager.createQuery(
                    "select c from Category c", Category.class
            );
            List<Category> categoryList = categoryTypedQuery.getResultList();
            Integer sum = 0;
            for (Category category : categoryList) {
                sum = 0;
                for (Discount discount : category.getDiscounts()) {
                    sum = sum + discount.getPercent();
                }
                System.out.println(category.getName() + "-" + sum);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}

