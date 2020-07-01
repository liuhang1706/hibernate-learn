package com.liuhang.hibernate;

import com.liuhang.hibernate.entity.Customer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Two {

    private Session session;
    private Transaction transaction;

    @Before
    public void init() {
        //1.加载hibernate配置文件
        Configuration configuration = new Configuration().configure();
        //2.创建SqlSessionFactory对象
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        //3.获取Session
        session = sessionFactory.openSession();
        //4.手动开启事务
        transaction = session.beginTransaction();
    }

    @After
    public void commit() {
        //6.提交事务
        transaction.commit();
        //7.资源释放
        session.close();
    }

    // ----------------    Query

    /**
     * Query：HQL 简单查询
     * HQL: Hibernate Query Language，一种面向对象的查询语句
     */
    @Test
    public void demo01() {
        //5.编写代码
        String HQL = "from Customer";
        Query query = session.createQuery(HQL);
        List<Customer> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * Query：HQL 条件查询
     */
    @Test
    public void demo02() {
        // cust_name是类Customer中的属性
        String HQL = "from Customer where cust_name like ?1";
        Query query = session.createQuery(HQL);
        query.setParameter(1,"%e%");
        List<Customer> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * Query：HQL 分页查询
     */
    @Test
    public void demo03() {
        String HQL = "from Customer";
        Query query = session.createQuery(HQL);
        query.setFirstResult(0);
        query.setMaxResults(3);
        List<Customer> list = query.list();
        list.forEach(System.out::println);
    }

    // -------------- Criteria:QBC(Query by Criteria)
    /**
     * Criteria
     */
    @Test
    public void demo04() {
        //5.编写代码
        // 推荐使用 JPA Criteria
        Criteria criteria = session.createCriteria(Customer.class);
        List<Customer> list = criteria.list();
        list.forEach(System.out::println);
    }

    /**
     * Criteria：条件查询
     */
    @Test
    public void demo05() {
        //5.编写代码
        // 推荐使用 JPA Criteria
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.add(Restrictions.like("cust_name","%e%"));
        List<Customer> list = criteria.list();
        list.forEach(System.out::println);
    }

    /**
     * Criteria：分页查询
     */
    @Test
    public void demo06() {
        //5.编写代码
        // 推荐使用 JPA Criteria
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.setFirstResult(0);
        criteria.setMaxResults(3);
        List<Customer> list = criteria.list();
        list.forEach(System.out::println);
    }

    //------------   NativeQuery
    @Test
    public void demo07() {
        //5.编写代码
        NativeQuery sqlQuery = session.createSQLQuery("select * from cst_customer");
        List<Object[]> list = sqlQuery.list();
        list.forEach(objects -> System.out.println(Arrays.toString(objects)));
    }
}
