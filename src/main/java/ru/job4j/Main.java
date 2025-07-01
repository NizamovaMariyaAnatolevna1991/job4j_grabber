package ru.job4j;

import org.apache.log4j.Logger;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.Store;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static final Logger LOG = Logger.getLogger(Config.class);

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        LOG.info("Приложение запущено!");
        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"), config.get("db.password"))) {

            Store store = new JdbcStore(connection);

            HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
            List<Post> fetch = habrCareerParse.fetch();
            for (Post post : fetch) {
                store.save(post);
            }

            Thread.sleep(150_000);

            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store
            );
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
