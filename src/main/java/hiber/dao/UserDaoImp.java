package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;


@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }


    @Override
    public void getUserByCar(String carModel, int carSeries) {

        String hql = "from Car where model = :car_model and series = :car_series";
        Query<Car> carQuery = sessionFactory.getCurrentSession().createQuery(hql);
        carQuery.setParameter("car_model", carModel);
        carQuery.setParameter("car_series", carSeries);
        List<Car> listCar = carQuery.list();

        if (!listCar.isEmpty()) {
            Car finedCar = listCar.get(0);
            User findUser = listUsers().stream()
                    .filter(Objects::nonNull)
                    .filter(user -> finedCar.equals(user.getCar()))
                    .findAny()
                    .orElse(null);

            if (findUser != null) {
                System.out.println("---------------------------------------------");
                System.out.println("Юзер, владеющий машиной - " + findUser.getCar().getModel() + " " + findUser.getCar().getSeries() + ":");
                System.out.println();
                System.out.println("User Id = " + findUser.getId());
                System.out.println("First Name = " + findUser.getFirstName());
                System.out.println("Last Name = " + findUser.getLastName());
                System.out.println("Email = " + findUser.getEmail());
                System.out.println("Car = " + findUser.getCar().getModel() + " " + findUser.getCar().getSeries());
                System.out.println("---------------------------------------------");
            } else {
                logIfNotExist();
            }

        } else {
            logIfNotExist();
        }
    }

    private static void logIfNotExist() {

        System.out.println("---------------------");
        System.out.println("Юзер, владеющий такой машиной не найден!");
    }
}

