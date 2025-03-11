package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing main.CarsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("Finding cars by manufacturer: {}", manufacturerN);
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM cars WHERE manufacturer = ?")) {
            preparedStatement.setString(1, manufacturerN);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car(
                        resultSet.getString("manufacturer"),
                        resultSet.getString("model"),
                        resultSet.getInt("year")
                );
                car.setId(resultSet.getInt("id"));
                cars.add(car);
            }
            logger.traceExit("Found {} cars", cars.size());
        } catch (SQLException e) {
            logger.error("Error finding cars by manufacturer", e);
        }
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("Finding cars between years {} - {}", min, max);
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM cars WHERE year BETWEEN ? AND ?")) {
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car(
                        resultSet.getString("manufacturer"),
                        resultSet.getString("model"),
                        resultSet.getInt("year")
                );
                car.setId(resultSet.getInt("id"));
                cars.add(car);
            }
            logger.traceExit("Found {} cars", cars.size());
        } catch (SQLException e) {
            logger.error("Error finding cars between years", e);
        }
        return cars;
    }


    @Override
    public void add(Car elem) {
        logger.traceEntry("Saving task{} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("insert into cars(manufacturer,model,year) values (?,?,?)")) {
            preparedStatement.setString(1, elem.getManufacturer());
            preparedStatement.setString(2, elem.getModel());
            preparedStatement.setInt(3, elem.getYear());
            preparedStatement.executeUpdate();
            logger.traceExit("Task {} saved", elem);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error saving task " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Car elem) {
        logger.traceEntry("Updating car with ID {} to {}", id, elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("UPDATE cars SET manufacturer = ?, model = ?, year = ? WHERE id = ?")) {
            preparedStatement.setString(1, elem.getManufacturer());
            preparedStatement.setString(2, elem.getModel());
            preparedStatement.setInt(3, elem.getYear());
            preparedStatement.setInt(4, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.traceExit("Car with ID {} updated", id);
            } else {
                logger.warn("No car found with ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error updating car", e);
        }
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry("Finding all cars");
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM cars")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car(
                        resultSet.getString("manufacturer"),
                        resultSet.getString("model"),
                        resultSet.getInt("year")
                );
                car.setId(resultSet.getInt("id"));
                cars.add(car);
            }
            logger.traceExit("Found {} cars", cars.size());
        } catch (SQLException e) {
            logger.error("Error finding all cars", e);
        }
        return cars;
    }
}

