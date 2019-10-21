package ru.naumen.javka.components;

import org.hibernate.ejb.HibernatePersistence;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import javax.persistence.EntityManager;

public class DbComponent {
    private EntityManager em;

    public DbComponent(String host, String user, String password) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(host);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        LocalContainerEntityManagerFactoryBean builder = new LocalContainerEntityManagerFactoryBean();
        builder.setDataSource(dataSource);
        builder.setPersistenceUnitName("fuckXML");
        builder.setPersistenceProviderClass(HibernatePersistence.class);
        builder.afterPropertiesSet();

        this.em = builder.getObject().createEntityManager();
    }

    public EntityManager getEm() {
        return em;
    }
}
