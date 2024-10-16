package com.roncoo.eshop.inventory;

import com.roncoo.eshop.inventory.listener.InitListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@MapperScan("com.roncoo.eshop.inventory.mapper")
public class Application {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new DataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JedisCluster JedisClusterFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config .setMaxTotal(500);
        config .setMinIdle(2);
        config .setMaxIdle(500);
        config .setMaxWaitMillis(10000);
        config .setTestOnBorrow(true);
        config .setTestOnReturn(true);
        //config.set


        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6379));
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6380));
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6381));
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6382));
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6383));
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 6384));


        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes,10000, 10000, 100, "ZL%pt&20",config);
        return jedisCluster;
    }


    /**
     * 注册监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new InitListener());
        return servletListenerRegistrationBean;
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
