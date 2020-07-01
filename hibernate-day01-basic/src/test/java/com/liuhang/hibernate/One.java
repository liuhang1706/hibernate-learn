package com.liuhang.hibernate;

import com.liuhang.hibernate.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 基础测试
 */
public class One {


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

    /**
     * save方法
     */
    @Test
    public void demo01() {
        //5.编写代码
        Customer customer = new Customer();
        customer.setCust_name("张三");
        session.save(customer);
    }

    /**
     * get方法：采用立即加载，即执行到get方法时，发送SQL语句进行查询
     * load方法：采用延时加载，即当使用了查询结果时，发送SQL语句进行查询
     */
    @Test
    public void demo02() {
//        Customer customer = session.get(Customer.class, 1L);
        Customer customer = session.load(Customer.class, 100L);
        System.out.println(customer);
    }

    /**
     * update方法：建议先查询对象，再在对象上进行修改，最后执行update方法，防止其他字段被null覆盖
     */
    @Test
    public void demo03() {
        Customer customer = session.get(Customer.class, 1L);
        customer.setCust_level("VIP222");
        session.update(customer);
    }

    /**
     * delete方法：hibernate的级联删除特性需要先执行get方法，再执行delete方法。
     */
    @Test
    public void demo04() {
        Customer customer = session.get(Customer.class, 1L);
        session.delete(customer);
    }

    /**
     * saveOrUpdate方法：有主键，就是update方法，没有主键就是insert方法
     */
    @Test
    public void demo05() {
        Customer customer1 = new Customer();
        customer1.setCust_name("wangwu");
        customer1.setCust_id(2L);
        session.saveOrUpdate(customer1);
    }

    /**
     * createQuery方法：接收HQL（Hibernate Query Language，面向对象的查询语言）或者SQL
     */
    @Test
    public void demo06() {
        // 接收HQL
//        Query from_customer = session.createQuery("from Customer");
//        List<Customer> list = from_customer.list();
        // 接收SQL
        NativeQuery sqlQuery = session.createSQLQuery("select * from cst_customer");
        List<Object[]> list = sqlQuery.list();
        list.forEach(obj -> System.out.println(Arrays.toString(obj)));
    }

    /**
     * 持久化类的三种状态
     */
    @Test
    public void demo07() {
        // 瞬时态对象，此时对象刚刚创建，没有唯一标识oid，没有被session管理
        Customer customer1 = new Customer();
        customer1.setCust_name("顶顶顶顶顶");
        // 持久态对象，此时对象调用了session方法，有唯一标识oid，被session管理
        session.save(customer1);
        // 当session关闭后，该对象就变成了托管态对象，有唯一标识oid，没有被session管理
    }

    /**
     * 获取持久态对象自动更新数据库
     * @After执行了session.commit()方法。此时hibernate判断持久态对象的属性是否发生变化，如果发生变化，将导致持久化对象自动更新数据库
     */
    @Test
    public void demo08() {
        // 获取持久态对象
        Customer customer = session.get(Customer.class, 2L);
        customer.setCust_name("sssssssssss");
        // @After执行了session.commit()方法。此时hibernate判断持久态对象的属性是否发生变化，如果发生变化，将导致持久化对象自动更新数据库
    }

    // ==================================          一级缓存    ==================

    /**
     *
     */
    @Test
    public void demo09() {
        // 发送SQL
        Customer customer = session.get(Customer.class, 2L);
        System.out.println(customer);
        // 不发送SQL，从一级缓存中获取
        Customer customer2 = session.get(Customer.class, 2L);
        System.out.println(customer2);
        // 返回true，表明两次获取到的是同一个对象
        System.out.println(customer == customer2);

        Customer customer1 = new Customer();
        customer1.setCust_name("cehis");
        // 发送SQL，进行存储操作
        Serializable id = session.save(customer1);
        // 不发送SQL，而是直接从缓存中获取
        Customer customer3 = session.get(Customer.class, id);
        System.out.println(customer3 == customer1);
    }

    @Test
    public void demo10() {
        // 发送SQL，获取持久态对象，并放入到一级缓存中
        Customer customer = session.get(Customer.class, 2L);
        // 在@After注解提交事务时，判断持久态对象和一级缓存中对象的属性是否相同，相同则不执行更新操作；否则，执行update语句
        customer.setCust_name("wangwu");
    }
}
