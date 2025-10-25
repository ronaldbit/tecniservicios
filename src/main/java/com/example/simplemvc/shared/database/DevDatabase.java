package com.example.simplemvc.shared.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Configuration
public class DevDatabase {
  @Value("classpath:db/dev/seeder.sql")
  private Resource scriptSql;

  @Bean
  DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
    log.info("Cargando datos de desarrollo desde: {}", scriptSql.getFilename());

    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

    databasePopulator.addScript(scriptSql);

    DataSourceInitializer initializer = new DataSourceInitializer();

    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(databasePopulator);

    return initializer;
  }
}
