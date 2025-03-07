package edu.meu.mgen.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FoodData {
    private List<Food> foodList;

    public FoodData() {
        foodList = new ArrayList<>();
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://127.0.0.1:3306/exercise_database?serverTimezone=UTC";
        String user = "root";
        String password = "";

        String query = "SELECT name, serving_size, calories_per_serving, protein, fat, carbohydrates FROM food_data";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                int servingSize = rs.getInt("serving_size");
                int caloriesPerServing = rs.getInt("calories_per_serving");
                double protein = rs.getDouble("protein");
                double fat = rs.getDouble("fat");
                double carbohydrates = rs.getDouble("carbohydrates");

                foodList.add(new Food(name, servingSize, caloriesPerServing, protein, fat, carbohydrates));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading food data from database.");
        }
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public Food getFoodByName(String name) {
        for (Food food : foodList) {
            if (food.getName().equalsIgnoreCase(name)) {
                return food;
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        FoodData foodData = new FoodData();
//        for (Food food : foodData.getFoodList()) {
//            System.out.println("Name: " + food.getName() +
//                               ", Serving Size: " + food.getServingSize() +
//                               ", Calories: " + food.getCaloriesPerServing() +
//                               ", Protein: " + food.getProtein() +
//                               ", Fat: " + food.getFat() +
//                               ", Carbohydrates: " + food.getCarbohydrates());
//        }
//    }
}
