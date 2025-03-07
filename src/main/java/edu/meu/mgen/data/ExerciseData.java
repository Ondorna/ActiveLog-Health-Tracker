package edu.meu.mgen.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ExerciseData {
    private List<Exercise> exerciseList;

    public ExerciseData() {
        exerciseList = new ArrayList<>();
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://127.0.0.1:3306/exercise_database?serverTimezone=UTC";
        String user = "root";
        String password = "";

        String query = "SELECT name, calories FROM exercise_data";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double calory = rs.getDouble("calories");
                exerciseList.add(new Exercise(name, calory));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading data from database.");
        }
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public Exercise getExerciseByName(String name) {
        for (Exercise exercise : exerciseList) {
            if (exercise.getName().equalsIgnoreCase(name)) {
                return exercise;
            }
        }
        return null;
    }
//    public static void main(String[] args) {
//        ExerciseData exerciseData = new ExerciseData();
//        for (Exercise exercise : exerciseData.getExerciseList()) {
//            System.out.println("Name: " + exercise.getName() + ", Calories: " + exercise.getCaloriesBurnedPerMinute());
//        }
//    }
}