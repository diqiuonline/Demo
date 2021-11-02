package com.roncoo.eshop.inventory;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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


        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.2.208", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.2.206", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.2.205", 7001));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes,10000, 10000, 100, "redis-pass",config);
        //jedisCluster.auth("redis-pass");
        return jedisCluster;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
