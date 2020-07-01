package com.liuhang.hibernate;

import com.liuhang.hibernate.entity.CustomerForLinkMan;
import com.liuhang.hibernate.entity.LinkMan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 一对多测试：
 *
 * 核心配置文件配置：<property name="hibernate.hbm2ddl.auto">create-drop</property>
 *              映射配置文件配置的映射。如果对应的表不存在，则先创建表；
 *              如果对应的表存在，则删除表，再重建。
 *
 */
public class Three {

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
     * 保存2个客户和3个联系人，并建立好关系
     * 此时没有配置级联操作，所以需要在双边（CustomerForLinkMam、LinkMan）都建立关系，同时保存save所有对象
     *      关系：一个联系人（LinkMan）对应一个客户
     *           一个客户（CustomerForLinkMan）对应多个联系人
     */
    @Test
    public void demo01() {

        CustomerForLinkMan customer1 = new CustomerForLinkMan();
        customer1.setCust_name("cust1");
        CustomerForLinkMan customer2 = new CustomerForLinkMan();
        customer2.setCust_name("cust2");

        LinkMan linkMan1 = new LinkMan();
        linkMan1.setLkm_name("lin1");
        LinkMan linkMan2 = new LinkMan();
        linkMan2.setLkm_name("lin2");
        LinkMan linkMan3 = new LinkMan();
        linkMan3.setLkm_name("lin3");

        // 建立关系
        linkMan1.setCustomer(customer1);
        linkMan2.setCustomer(customer1);
        linkMan3.setCustomer(customer2);
        customer1.getLinkManSet().add(linkMan1);
        customer1.getLinkManSet().add(linkMan2);
        customer2.getLinkManSet().add(linkMan3);

        session.save(linkMan1);
        session.save(linkMan2);
        session.save(linkMan3);
        session.save(customer1);
        session.save(customer2);
    }

    /**
     * 级联操作：操作一个对象的时候，同时操作其关联对象
     * 级联有方向：操作一的一方的时候，是否操作到多的一方
     *           操作多的一方的时候，是否操作到一的一方
     */

    /**
     * 级联操作：保存客户（CustomerForLinkMan）的同时保存该客户关联的联系人（linkMan1）
     *         此时的级联方向是：操作一的一方（客户），同时操作到多的一方（联系人）
     * 所以需要在 customerFroLinkMan.hbm.xml 配置文件中：
     *         <set name="linkManSet" cascade="save-update">
     */
    @Test
    public void demo02() {

        CustomerForLinkMan customer1 = new CustomerForLinkMan();
        customer1.setCust_name("客户1");

        LinkMan linkMan1 = new LinkMan();
        linkMan1.setLkm_name("link1");
        LinkMan linkMan2 = new LinkMan();
        linkMan2.setLkm_name("link2");

        // 建立关系
        customer1.getLinkManSet().add(linkMan1);
        customer1.getLinkManSet().add(linkMan2);
        linkMan1.setCustomer(customer1);
        linkMan2.setCustomer(customer1);

        // 通过 customer 级联操作 linkman，所以需要在 customerFroLinkMan.hbm.xml 配置文件中
        // <set name="linkManSet" cascade="save-update">
        session.save(customer1);
    }

    /**
     * 级联操作：联系人（linkMan）的同时保存该联系人关联的保存客户（CustomerForLinkMan）
     *         此时的级联方向是：操作多的一方（联系人），同时操作到一的一方（客户）
     * 所以需要在 linkman.hbm.xml 配置文件中：
     *         <many-to-one name="customer" class="com.liuhang.hibernate.entity.CustomerForLinkMan"
     *                      column="lkm_cust_id" cascade="save-update"/>
     *
     */
    @Test
    public void demo03() {

        CustomerForLinkMan customer1 = new CustomerForLinkMan();
        customer1.setCust_name("客户1");

        LinkMan linkMan1 = new LinkMan();
        linkMan1.setLkm_name("link1");
        LinkMan linkMan2 = new LinkMan();
        linkMan2.setLkm_name("link2");

        // 建立关系
        customer1.getLinkManSet().add(linkMan1);
        customer1.getLinkManSet().add(linkMan2);
        linkMan1.setCustomer(customer1);
        linkMan2.setCustomer(customer1);

        session.save(linkMan1);
        session.save(linkMan2);
    }

    /**
     * 级联操作：双向
     * 所以需要在 linkman.hbm.xml 配置文件中：
     *         <many-to-one name="customer" class="com.liuhang.hibernate.entity.CustomerForLinkMan"
     *                      column="lkm_cust_id" cascade="save-update"/>
     * 所以需要在 customerFroLinkMan.hbm.xml 配置文件中：
     *         <set name="linkManSet" cascade="save-update">
     */
    @Test
    public void demo04() {

        CustomerForLinkMan customer1 = new CustomerForLinkMan();
        customer1.setCust_name("客户1");

        LinkMan linkMan1 = new LinkMan();
        linkMan1.setLkm_name("link1");
        LinkMan linkMan2 = new LinkMan();
        linkMan2.setLkm_name("link2");
        LinkMan linkMan3 = new LinkMan();
        linkMan3.setLkm_name("link3");

        // 建立关系
        customer1.getLinkManSet().add(linkMan1);
        linkMan2.setCustomer(customer1);
        linkMan3.setCustomer(customer1);

        /**
         * 情形一：session.save(customer1);
         *      由于双向都设置了级联操作，所以插入 customer 将触发 linkMan1 的操作，所以只有2条insert语句
         */
//        session.save(customer1);

        /**
         * 情形二：session.save(linkMan2);
         *      由于双向都设置了级联操作，所以插入 linkMan2 将触发 customer1 的操作，
         *      customer1 的插入操作又会触发 linkMan1 的 插入操作
         *      所以，一共有3条 insert 语句
         */
        session.save(linkMan2);

    }

    /**
     * 级联操作：删除，删除客户（一的一方）的时候，同时删除对应的联系人（多的一方）
     * 记得注释掉核心配置文件中：<property name="hibernate.hbm2ddl.auto">create-drop</property>
     * 情形一：默认情况：没有配置级联操作
     *  linkman.hbm.xml 配置文件中：
     *         <many-to-one name="customer" class="com.liuhang.hibernate.entity.CustomerForLinkMan"
     *                      column="lkm_cust_id"/>
     *  customerFroLinkMan.hbm.xml 配置文件中：
     *         <set name="linkManSet">
     *  由于没有设置级联操作，所以默认情况下，hibernate会将多的一方（联系人）的表中的外键设置为null，然后删除掉一的一方（客户）
     *
     *  情形二：配置了级联删除
     *      linkman.hbm.xml 配置文件中：
     *          <many-to-one name="customer" class="com.liuhang.hibernate.entity.CustomerForLinkMan"
     *                      column="lkm_cust_id" cascade="save-update"/>
     *      customerFroLinkMan.hbm.xml 配置文件中：
     *          <set name="linkManSet" cascade="save-update,delete">
     *  由于设置了级联删除，所以，hibernate会将多的一方（联系人）的表中的外键设置为null，然后删除多的一方（联系人）中对应的记录，
     *  最后删除一的一方（客户）对应的记录
     */
    @Test
    public void demo05() {
        CustomerForLinkMan customerForLinkMan = session.get(CustomerForLinkMan.class, 1L);
        session.delete(customerForLinkMan);
    }

    /**
     * 级联操作：删除，删除联系人（多的一方）的时候，同时删除对应的客户（一的一方）
     *          这种配置基本不用。多的一方对应的表中通常保留了外键字段用于引用一的一方
     *          比如：客户和订单是一对多的关系，订单表中引用客户表中的字段。
     *          删除多的一方（订单）同时删除一的一方（客户），这是不符合事实的。
     */
    @Test
    public void demo06() {
        LinkMan linkMan = session.get(LinkMan.class, 1L);
        session.delete(linkMan);
    }

    /**
     * 一对多设置了双向关联，可能产生多余的SQL
     *      解决方法：一的一方（客户）放弃维护外键
     *              <set name="linkManSet" cascade="save-update,delete" inverse="true">
     */
    @Test
    public void demo07() {
        LinkMan linkMan = session.get(LinkMan.class, 1L);
        CustomerForLinkMan customerForLinkMan = session.get(CustomerForLinkMan.class, 2L);
        //  ------------- 情形一：只设置持久态对象 linkMan
        // 让id为2 的 linkMan 改为引用 id为 2的customer
//        linkMan.setCustomer(customerForLinkMan);
        // 由于持久态对象linkMan与缓存相比，发生了变化，所以当事务提交时，持久态对象将会改变数据库

        //  ------------- 情形二：只设置持久态对象 customerForLinkMan
//        customerForLinkMan.getLinkManSet().add(linkMan);
        // 由于持久态对象customerForLinkMan与缓存相比，发生了变化，所以当事务提交时，持久态对象将会改变数据库

        //  ------------- 情形三：同时设置持久态对象 linkMan和customerForLinkMan
        linkMan.setCustomer(customerForLinkMan);
        customerForLinkMan.getLinkManSet().add(linkMan);
        // 由于持久态对象customerForLinkMan、linkMan与缓存相比，都发生了变化，所以当事务提交时，两个持久态对象
        // 将会改变数据库，此时会发现产生了一条多余的SQL。这是因为两边都在维护外键，导致外键被更新了两次
        // 可以通过使一方放弃对外键的维护来避免这种情况，通常选择一的一方（客户），因为一个客户对应多个联系人。
    }

    /**
     * 区分 cascade 和 inverse
     *
     */
    @Test
    public void demo08() {
        CustomerForLinkMan customerForLinkMan = new CustomerForLinkMan();
        customerForLinkMan.setCust_name("cust11");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_name("link11");

        customerForLinkMan.getLinkManSet().add(linkMan);

        /**
         * 情况一：
         *      customerFroLinkMan.hbm.xml配置文件，设置了级联，但是放弃了外键维护权
         *          <set name="linkManSet" cascade="save-update,delete" inverse="true">
         *      linkman.hbm.xml配置文件，设置了级联，但是没有放弃外键维护权
         *             <many-to-one name="customer" class="com.liuhang.hibernate.entity.CustomerForLinkMan"
         *                      column="lkm_cust_id" cascade="save-update"/>
         *      session.save(customerForLinkMan)：因为customer配置了级联，所以customer插入将触发linkman的插入，
         *                                        但是由于customer放弃了外键维护权，导致linkman中的外键字段值为null
         */
        session.save(customerForLinkMan);
    }
}
