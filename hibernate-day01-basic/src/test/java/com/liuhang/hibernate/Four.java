package com.liuhang.hibernate;

import com.liuhang.hibernate.entity.CustomerForLinkMan;
import com.liuhang.hibernate.entity.LinkMan;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * hibernate的检索方式
 */
public class Four {

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

    @Test
    public void initData() {
        CustomerForLinkMan customerForLinkMan = new CustomerForLinkMan();
        customerForLinkMan.setCust_name("cust3");
        for (int i = 0; i < 9; i++) {
            LinkMan linkMan = new LinkMan();
            linkMan.setLkm_name("linkman3" +i);
            customerForLinkMan.getLinkManSet().add(linkMan);
            session.save(linkMan);
        }
        session.save(customerForLinkMan);
    }

    /**
     * HQL：简单查询
     */
    @Test
    public void demo01() {
        Query query = session.createQuery("from CustomerForLinkMan");
        List<CustomerForLinkMan> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * HQL：排序查询
     */
    @Test
    public void demo02() {
        Query query = session.createQuery("from CustomerForLinkMan order by cust_id desc ");
        List<CustomerForLinkMan> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * HQL：条件查询
     *      按位置设置参数
     *      按名称设置参数
     */
    @Test
    public void demo03() {
        // 按位置设置参数
//        Query query = session.createQuery("from CustomerForLinkMan where cust_name = ?0");
//        query.setParameter(0,"cust2");
        // 按名称设置参数
        Query query = session.createQuery("from CustomerForLinkMan where cust_name = :custName");
        query.setParameter("custName","cust2");
        List<CustomerForLinkMan> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * HQL：投影查询
     *      查询部分字段
     */
    @Test
    public void demo04() {
        // 单个属性
        Query query = session.createQuery("select c.cust_name from CustomerForLinkMan c");
        List<Object> list = query.list();
        list.forEach(System.out::println);
        // 多个属性
        List<Object[]> list1 = session.createQuery("select c.cust_id, c.cust_name from CustomerForLinkMan c").list();
        list1.forEach(obj -> System.out.println(Arrays.toString(obj)));
        // 多个属性，同时封装到对象中
        List<CustomerForLinkMan> list2 = session.createQuery("select new CustomerForLinkMan(cust_id,cust_name) from CustomerForLinkMan").list();
        list2.forEach(System.out::println);

    }

    /**
     * HQL：分页查询
     */
    @Test
    public void demo05() {
        Query qu = session.createQuery("from LinkMan");
        qu.setFirstResult(0);
        qu.setMaxResults(8);
        List<LinkMan> list = qu.list();
        list.forEach(System.out::println);
    }

    /**
     * HQL：分组查询
     */
    @Test
    public void demo06() {
        Object object = session.createQuery("select count(*) from LinkMan").uniqueResult();
        System.out.println(object);
    }

    // ----------------- QBC检索
    /**
     * QBC：简单查询
     *   use the JPA Criteria
     */
    @Test
    public void demo07() {
        Criteria criteria = session.createCriteria(CustomerForLinkMan.class);
        List<CustomerForLinkMan> list = criteria.list();
        list.forEach(System.out::println);
    }

    /**
     * QBC：排序
     *   use the JPA Criteria
     */
    @Test
    public void demo08() {
        Criteria criteria = session.createCriteria(CustomerForLinkMan.class);
        criteria.addOrder(Order.desc("cust_id"));
        List<CustomerForLinkMan> list = criteria.list();
        list.forEach(System.out::println);
    }

    /**
     * QBC：分页
     *   use the JPA Criteria
     */
    @Test
    public void demo09() {
        Criteria criteria = session.createCriteria(CustomerForLinkMan.class);
        criteria.setFirstResult(0);
        criteria.setMaxResults(2);
        List<CustomerForLinkMan> list = criteria.list();
        list.forEach(System.out::println);
    }

    /**
     * QBC：条件查询
     *   use the JPA Criteria
     */
    @Test
    public void demo10() {
        Criteria criteria = session.createCriteria(CustomerForLinkMan.class);
        criteria.add(Restrictions.eq("cust_name","cust2"));
        List<CustomerForLinkMan> list = criteria.list();
        list.forEach(System.out::println);
    }

    /**
     * QBC：统计查询
     *   use the JPA Criteria
     */
    @Test
    public void demo11() {
        Criteria criteria = session.createCriteria(CustomerForLinkMan.class);
        criteria.setProjection(Projections.rowCount());
        Object o = criteria.uniqueResult();
        System.out.println(o);
    }

    /**
     * QBC：离线条件查询
     *   use the JPA Criteria
     */
    @Test
    public void demo12() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomerForLinkMan.class);
        detachedCriteria.add(Restrictions.eq("cust_name","cust2"));

        Criteria executableCriteria = detachedCriteria.getExecutableCriteria(session);
        List<CustomerForLinkMan> list = executableCriteria.list();
        list.forEach(System.out::println);
    }


    // ----------------- HQL的多表查询
    @Test
    public void demo13() {
        // 普通内连接
        List<Object[]> list = session.createQuery("from CustomerForLinkMan c inner join c.linkManSet").list();
        list.forEach(obj -> System.out.println(Arrays.toString(obj)));
        // 迫切内连接,将查询结果封装到对象中（CustomerForLinkMan）
        // distinct 关键字是为了解决重复封装的问题
        List<CustomerForLinkMan> list1 = session.createQuery("select distinct c from CustomerForLinkMan c inner join fetch c.linkManSet").list();
        list1.forEach(System.out::println);
    }

    // ----------------- SQL查询方式
    /**
     * SQL查询方式
     */
    @Test
    public void demo14() {
        NativeQuery sqlQuery = session.createSQLQuery("select * from cst_customer");
        sqlQuery.addEntity(CustomerForLinkMan.class);
        List<CustomerForLinkMan> list = sqlQuery.list();
        list.forEach(System.out::println);
    }
}
