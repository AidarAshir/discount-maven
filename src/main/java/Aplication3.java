import entity.Category;
import entity.Discount;
import entity.Order;
import entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class Aplication3 {
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("main");

    //вывести все заказы с общей стоимостью с учетом всех скидок
    public static void main(String[] args) {
        EntityManager manager = FACTORY.createEntityManager();
        try {
            manager.getTransaction().begin();
            TypedQuery<Order> orderTypedQuery = manager.createQuery(
                    "select o from Order o", Order.class
            );
            List<Order> ordersList = orderTypedQuery.getResultList();

            for (Order order : ordersList) {
                Double orderSum = 0d;
                double productWDis = 0;
                for (Product product : order.getProducts()) {

                    double discountsC = 0;
                    for (Discount discount : product.getCategory().getDiscounts()) {
                        discountsC = discountsC + discount.getPercent();
                    }

                    double discountsP = 0;
                    for (Discount discount : product.getDiscounts()) {
                        discountsP = discountsP + discount.getPercent();
                    }

                    double discountsSum = discountsP + discountsC;
                    productWDis = productWDis + (product.getPrice() - (product.getPrice() / 100 * discountsSum));
                }
                orderSum = orderSum + productWDis;
                System.out.println(order.getId() + " - " + orderSum);
            }
            manager.getTransaction().commit();
        } catch (
                Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
        //*
    }
}