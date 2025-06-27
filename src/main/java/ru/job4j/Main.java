package ru.job4j;

import org.apache.log4j.Logger;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.HabrCareerParse;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.ErrorManager;

public class Main {
    private static final Logger LOG = Logger.getLogger(Config.class);

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        LOG.info("Приложение запущено!");
        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"), config.get("db.password"));
             var scheduler = new SchedulerManager()) {
            var store = new JdbcStore(connection);
//            var post = new Post();
//            post.setTitle("Super Java Job");
//            post.setTime(System.currentTimeMillis());
//            post.setLink("//http:");
//            store.save(post);
//            store.getAll();
//            scheduler.init();
//            scheduler.load(
//                    Integer.parseInt(config.get("rabbit.interval")),
//                    SuperJobGrab.class,
//                    store);
//            Thread.sleep(10000);
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        }
    }
}
