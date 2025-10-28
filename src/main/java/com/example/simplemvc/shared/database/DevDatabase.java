package com.example.simplemvc.shared.database;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("dev")
public class DevDatabase {

  private final DataSource dataSource;
  private final JdbcTemplate jdbc;

  public DevDatabase(DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbc = new JdbcTemplate(dataSource);
  }

  @PostConstruct
  public void seedIfNeeded() {
    try {
      Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM rol", Integer.class);
      if (count != null && count > 0) {
        return; // ya hay datos: no exe
      }
    } catch (Exception ignored) {
      // si la tabla aún no existe, = intentamos exe
    }

    ResourceDatabasePopulator pop = new ResourceDatabasePopulator();
    pop.setSqlScriptEncoding("UTF-8");
    pop.setContinueOnError(true); // por si algo ya existe, no tumbar el arranque
    pop.addScript(new ClassPathResource("db/dev/seeder.sql"));

    DatabasePopulatorUtils.execute(pop, dataSource);
  }
}
