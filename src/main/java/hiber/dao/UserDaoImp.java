package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.List;


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
    public List<User> getUserByCar(String carModel, int carSeries) {

        String hql = "SELECT user FROM Car WHERE model = :car_model and series = :car_series";
        Query userQuery = sessionFactory.getCurrentSession().createQuery(hql);
        userQuery.setParameter("car_model", carModel);
        userQuery.setParameter("car_series", carSeries);
        List listUser = userQuery.list();
        System.out.println();

        if (listUser.size() != 0) {
            for (int i = 0; i < listUser.size(); i++) {
                System.out.println("Юзер владеющий машиной: " + listUser.get(i));
            }

        } else {
            System.out.println("Юзер владеющий машиной не найден!");
        }
        return listUser;
    }
}

