package edu.meu.mgen.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertFoodData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/exercise_database?serverTimezone=UTC";
        String user = "root";
        String password = "";

        Object[][] data = {
                //1.name  2.serving_size 3.calories_per_serving  4.protein  5.fat  6.carbohydrates
                    {"Apple", 100.0, 52.0, 0.3, 0.2, 14.0},
                    {"Banana", 118.0, 105.0, 1.3, 0.3, 27.0},
//                    {"Noodles", 150.0, 284.0, 8.3, 0.7, 61.1},
//                    {"Rice", 150.0, 116.0, 2.6, 0.8, 77.2},
//                    {"Cornmeal", 150.0, 340.0, 8.0, 4.5, 66.9},
//                    {"Potatoes", 150.0, 76.0, 2.0, 0.2, 16.5},
//                    {"Soybeans", 100.0, 359.0, 35.0, 16.0, 18.7},
//                    {"Carrots", 200.0, 20.0, 1.0, 0.1, 3.8},
//                    {"Radishes", 200.0, 20.0, 0.8, 0.0, 4.1},
//                    {"Green Beans", 200.0, 30.0, 2.5, 0.2, 4.6},
//                    {"Eggplant", 200.0, 21.0, 1.1, 0.2, 3.6},
//                    {"Tomatoes", 200.0, 19.0, 0.9, 0.2, 3.5},
//                    {"Peppers", 200.0, 212.0, 15.0, 12.0, 11.0},
//                    {"Cucumbers", 200.0, 15.0, 0.8, 0.2, 2.4},
//                    {"Pumpkin", 200.0, 22.0, 0.7, 0.1, 4.5},
//                    {"Garlic", 200.0, 126.0, 4.5, 0.2, 26.5},
//                    {"Onions", 200.0, 39.0, 1.1, 0.2, 8.1},
//                    {"Cabbage", 200.0, 22.0, 1.5, 0.2, 3.6},
//                    {"Broccoli", 200.0, 33.0, 4.1, 0.6, 2.7},
//                    {"Spinach", 200.0, 24.0, 2.6, 0.3, 2.8},
//                    {"Lettuce", 200.0, 13.0, 1.3, 0.3, 1.3},
//                    {"Mushrooms", 70.0, 19.0, 2.2, 0.3, 1.9},
//                    {"Grapes", 150.0, 43.0, 0.5, 0.2, 9.9},
//                    {"Strawberries", 150.0, 30.0, 1.0, 0.2, 6.0},
//                    {"Pineapple", 150.0, 41.0, 0.5, 0.1, 9.5},
//                    {"Watermelon", 150.0, 25.0, 0.6, 0.1, 5.5},
//                    {"Peanuts", 30.0, 589.0, 21.7, 48.0, 17.5},
//                    {"Pork", 125.0, 395.0, 13.2, 37.0, 2.4},
//                    {"Bacon", 125.0, 181.0, 22.3, 9.0, 2.6},
//                    {"Sausage", 125.0, 508.0, 24.1, 40.7, 11.2},
//                    {"Beef", 125.0, 125.0, 19.9, 4.2, 2.0},
//                    {"Lamb", 125.0, 203.0, 19.0, 14.1, 0.0},
//                    {"Chicken", 125.0, 167.0, 19.3, 9.4, 1.3},
//                    {"Milk", 240.0, 54.0, 3.0, 3.4, 3.2},
//                    {"Yogurt", 200.0, 72.0, 2.5, 2.7, 9.3},
//                    {"Butter", 5.0, 888.0, 1.4, 98.0, 0.0},
//                    {"Eggs", 125.0, 144.0, 13.3, 8.8, 2.8},
//                    {"Shrimp", 125.0, 87.0, 16.4, 2.4, 0.0},
//                    {"Fish", 125.0, 113.0, 16.6, 5.2, 0.0},
//                    {"Cake", 30.0, 347.0, 8.6, 5.1, 66.7},
//                    {"Bread", 60.0, 312.0, 8.3, 5.1, 58.1},
//                    {"Biscuits", 40.0, 433.0, 9.0, 12.7, 70.6},
//                    {"Cola", 240.0, 45.0, 0.0, 0.0, 11.2},
//                    {"Juice", 240.0, 30.0, 0.1, 0.0, 5.1},
//                    {"Tea", 240.0, 294.0, 26.7, 1.1, 44.4},
//                    {"Beer", 335.0, 16.0, 0.4, 0.0, 3.7},
//                    {"Sugar", 25.0, 400.0, 0.0, 0.0, 99.9},
//                    {"Chocolate", 20.0, 586.0, 4.3, 40.1, 51.9},
//                    {"Oil", 15.0, 899.0, 0.0, 99.9, 0.0},
//                    {"Soy Sauce", 15.0, 63.0, 5.6, 0.1, 9.9},
//                    {"Vinegar", 25.0, 31.0, 2.1, 0.3, 4.9}
        };

        // insert
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for (Object[] row : data) {
                String checkQuery = "SELECT COUNT(*) FROM food_data WHERE name = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, (String) row[0]); // 检查 name
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) { // 如果不存，直接insert
                            String insertQuery = "INSERT INTO food_data (name, serving_size, calories_per_serving, protein, fat, carbohydrates) VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, (String) row[0]);
                                insertStmt.setDouble(2, (Double) row[1]);
                                insertStmt.setDouble(3, (Double) row[2]);
                                insertStmt.setDouble(4, (Double) row[3]);
                                insertStmt.setDouble(5, (Double) row[4]);
                                insertStmt.setDouble(6, (Double) row[5]);
                                insertStmt.executeUpdate();
                                System.out.println("Inserted: " + row[0]);
                            }
                        } else {// 存在--update
                            String updateQuery = "UPDATE food_data SET serving_size = ?, calories_per_serving = ?, protein = ?, fat = ?, carbohydrates = ? WHERE name = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setDouble(1, (Double) row[1]);
                                updateStmt.setDouble(2, (Double) row[2]);
                                updateStmt.setDouble(3, (Double) row[3]);
                                updateStmt.setDouble(4, (Double) row[4]);
                                updateStmt.setDouble(5, (Double) row[5]);
                                updateStmt.setString(6, (String) row[0]);
                                updateStmt.executeUpdate();
                                System.out.println("Updated: " + row[0]);
                            }
                        }
                    }
                }
            }
            System.out.println("Insert successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}