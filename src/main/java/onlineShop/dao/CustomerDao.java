
package onlineShop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.entity.Authorities;
import onlineShop.entity.Customer;
import onlineShop.entity.User;

// 该DAO将来要被service调用
// Repository 本质上是component
// Spring 启动起来就会创建跟CustomerDao相关的instance
// 然后就可以在CustomService class里引用
@Repository
public class CustomerDao {

    @Autowired // spring将创建好的对象引入
    private SessionFactory sessionFactory;

    public void addCustomer(Customer customer) {
        Authorities authorities = new Authorities();
        authorities.setEmailId(customer.getUser().getEmailId()); // 设置成authorities的主键
        authorities.setAuthorities("ROLE_USER"); // 加上权限

        // 通过session对象 将对象customer 和 authorities 通过Hibernate 添加到数据库
        Session session = null;
        try {
            session = sessionFactory.openSession(); //拿到返回的session对数据库操作
            // call beginTransaction 返回transaction对象
            session.beginTransaction(); // atomic: transact 失败就roll back
            session.save(authorities); // 通过save语句定义需要的transaction操作
            session.save(customer); // Insert 所有与customer相关的列表
            session.getTransaction().commit(); // 执行以上定义的transaction save操作
        } catch (Exception e) {
            e.printStackTrace(); //
            session.getTransaction().rollback(); //如果异常，则回退到beginTransaction之前
        } finally {
            if (session != null) {
                session.close(); // 存储完成没有异常后close，节省IO
            }
        }
    }

    // 登陆时候get Customer
    // username == email （Id）
    public Customer getCustomerByUserName(String userName) {

        // 由于customer里没有Email variable，所以通过query user PK 的时候
        // 通过 eager fetch 得到对应的customer
        User user = null;

        // java 8新特性 try（ Object） 会try里操作后自动close 括号里的
        // 需要session extends 了 Autocloseable后才会自动close
        // interface Session extends SharedSessionContract, EntityManager, HibernateEntityManager, AutoCloseable
        // get 操作不需要transaction，所以在try里执行后自动close
        try (Session session = sessionFactory.openSession()) {

            // criteria 相当于where语句
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
            // criteria.add(Restrictions.eq("emailId", userNmae));
            // user = (user) criteria.uniqueResult():
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) // 说明在DB里找到了
            return user.getCustomer();
        return null; // 没找到
    }
}
