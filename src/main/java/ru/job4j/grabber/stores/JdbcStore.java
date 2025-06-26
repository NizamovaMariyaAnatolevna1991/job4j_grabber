package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;
import org.apache.log4j.Logger;
import ru.job4j.grabber.service.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private static final Logger LOG = Logger.getLogger(Config.class);
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO post(title, description, link, time) VALUES (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, new Timestamp(post.getTime()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId((long) generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Ошибка при сохранении вакансии", e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> post = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    post.add(createPost(resultSet));
                }
            }
        } catch (Exception e) {
            LOG.error("Ошибка при получении всех вакансий", e);
        }
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = createPost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(post);
    }

    private Post createPost(ResultSet resultSet) throws SQLException {
        return new Post(
                (long) resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("link"),
                (long) resultSet.getTimestamp("time").getTime()
        );
    }
}
