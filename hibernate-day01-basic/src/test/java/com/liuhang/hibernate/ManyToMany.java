package com.liuhang.hibernate;

import com.liuhang.hibernate.entity.manyToMany.Role;
import com.liuhang.hibernate.entity.manyToMany.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 多对多的测试
 */
public class ManyToMany {

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
     * 保存2个User和3个Role，并建立好关系
     * 此时没有配置级联操作，所以需要在双边（User、Role）都建立关系，同时保存save所有对象
     * 多对多操作，必须有一方放弃外键管理权，否则导致中间插入同一个记录而报错
     *      关系：多对多
     */
    @Test
    public void demo01() {
        User user1 = new User();
        user1.setUser_name("user1");
        User user2 = new User();
        user2.setUser_name("user2");

        Role role3 = new Role();
        role3.setRole_name("role3");
        Role role1 = new Role();
        role1.setRole_name("role1");
        Role role2 = new Role();
        role2.setRole_name("role2");

        // 建立关系
        user1.getRoles().add(role1);
        user1.getRoles().add(role2);
        user2.getRoles().add(role2);
        user2.getRoles().add(role3);

        role1.getUsers().add(user1);
        role2.getUsers().add(user1);
        role2.getUsers().add(user2);
        role3.getUsers().add(user2);

        // 保存操作：多对多操作，必须有一方放弃外键管理权，否则导致中间插入同一个记录而报错
        // 通常情况下，被动的一方放弃外键管理权。比如：角色被用户选择，所以外键由用户管理，角色放弃外键管理权
        session.save(user1);
        session.save(user2);
        session.save(role1);
        session.save(role2);
        session.save(role3);

    }

    /**
     * 保存2个User和3个Role，并建立好关系
     * 情况一：配置级联保存：保存用户的时候，保存角色
     *      user.hbm.xml 配置文件
     *          <set name="roles" table="sys_user_role" cascade="save-update">
     *      role.hbm.xml 配置文件
     *          <set name="users" table="sys_user_role" inverse="true">
     * 情况二：配置级联保存：保存角色的时候，保存用户
     *      user.hbm.xml 配置文件
     *          <set name="roles" table="sys_user_role" inverse="true">
     *      role.hbm.xml 配置文件
     *          <set name="users" table="sys_user_role" cascade="save-update">
     *
     */
    @Test
    public void demo02() {
        User user1 = new User();
        user1.setUser_name("user1");
        User user2 = new User();
        user2.setUser_name("user2");

        Role role3 = new Role();
        role3.setRole_name("role3");
        Role role1 = new Role();
        role1.setRole_name("role1");
        Role role2 = new Role();
        role2.setRole_name("role2");

        // 建立关系
        user1.getRoles().add(role1);
        user1.getRoles().add(role2);
        user2.getRoles().add(role2);
        user2.getRoles().add(role3);

        role1.getUsers().add(user1);
        role2.getUsers().add(user1);
        role2.getUsers().add(user2);
        role3.getUsers().add(user2);

        // 情况一：只需保存用户
        session.save(user1);
        session.save(user2);
        // 情况二：只需保存角色
//        session.save(role1);
//        session.save(role2);
//        session.save(role3);
    }

    /**
     * 多对多的级联删除：删除用户的时候，同时删除用户对应的角色
     *      user.hbm.xml配置文件
     *          <set name="roles" table="sys_user_role" cascade="save-update,delete">
     * 级联删除通常不使用，因为不符合实际。同时级联删除容易发生错误。如果要被级联删除的角色正在被其他用户引用，将导致
     * 删除失败。
     */
    @Test
    public void demo03() {
        User user = session.get(User.class, 1L);
        session.delete(user);
    }

    /**
     * 多对多的级联删除：删除角色的时候，同时删除角色对应的用户
     *      role.hbm.xml配置文件
     *          <set name="users" table="sys_user_role" cascade="save-update,delete" inverse="true">
     * 级联删除通常不使用，因为不符合实际。同时级联删除容易发生错误。如果要被级联删除的用户正在被其他角色引用，将导致
     * 删除失败。
     */
    @Test
    public void demo04() {
        Role role = session.get(Role.class, 3L);
        session.delete(role);
    }

    /**
     * 多对多的级联修改：为用户添加角色
     */
    @Test
    public void demo05() {
        // 获取持久态对象
        User user = session.get(User.class, 1L);
        Role role = session.get(Role.class, 3L);
        // 修改持久态对象，当事务提交时，将导致数据库的更新
        user.getRoles().add(role);
    }

    /**
     * 多对多的级联修改：为用户修改角色
     *      用户2 的角色2 修改为 角色1
     */
    @Test
    public void demo06() {
        // 获取持久态对象
        User user = session.get(User.class, 2L);
        Role role2 = session.get(Role.class, 2L);
        Role role1 = session.get(Role.class, 1L);
        // 修改持久态对象，当事务提交时，将导致数据库的更新
        user.getRoles().remove(role2);
        user.getRoles().add(role1);
    }

    /**
     * 多对多的级联修改：为用户删除角色
     *      用户1 的角色2 删除
     */
    @Test
    public void demo07() {
        // 获取持久态对象
        User user = session.get(User.class, 1L);
        Role role3 = session.get(Role.class, 3L);
        // 修改持久态对象，当事务提交时，将导致数据库的更新
        user.getRoles().remove(role3);
    }
}
