package edu.meu.mgen.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertExerciseData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/exercise_database?serverTimezone=UTC";
        String user = "root";
        String password = "";


        Object[][] data = {
                {"Jump Rope", 12.0},
//                {"Climbing", 10.0},
//                {"Extreme Skateboarding", 10.0},
//                {"Running", 10.0},
//                {"Dancing", 9.0},
//                {"Jump Training", 9.0},
//                {"Sprinting", 9.0},
//                {"Skiing", 9.0},
//                {"Boxing Training", 9.0},
//                {"Rowing", 8.0},
//                {"Swimming", 8.0},
//                {"Basketball", 8.0},
//                {"Tennis", 8.0},
//                {"Badminton", 8.0},
//                {"Squash", 8.0},
//                {"Volleyball", 8.0},
//                {"Soccer", 8.0},
//                {"Elliptical Machine", 8.0},
//                {"Boxing Aerobics", 8.0},
//                {"Beach Volleyball", 8.0},
//                {"Cycling", 7.5},
//                {"Jogging", 7.0},
//                {"High-Intensity Walking", 7.0},
//                {"Aerobics", 7.0},
//                {"Dance Aerobics", 7.0},
//                {"Hiking", 7.0},
//                {"Kayaking", 7.0},
//                {"Martial Arts Training", 6.0},
//                {"Dance Machine", 6.0},
//                {"Skating", 6.0},
//                {"Aerobics", 6.0},
//                {"Golf", 5.0},
//                {"Billiards", 5.0},
//                {"Strength Training", 5.0},
//                {"Judo", 5.0},
//                {"Stretching Exercises", 4.0},
//                {"Stationary Biking", 4.0},
//                {"Casual Walking", 4.0},
//                {"Yoga", 3.0},
//                {"Table Tennis", 3.0},
//                {"Bowling", 3.0},
//                {"Rowing (light intensity)", 3.0},
//                {"Skiing (casual)", 3.0},
//                {"Casual Cycling", 3.0},
//                {"Casual Dancing", 3.0},
//                {"Horse Riding", 2.0},
//                {"Health Walking", 2.0},
//                {"Climbing Stairs", 2.0}

        };
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for (Object[] row : data) {
                String checkQuery = "SELECT COUNT(*) FROM exercise_data WHERE name = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, (String) row[0]);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            String insertQuery = "INSERT INTO exercise_data (name, calories) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, (String) row[0]);
                                insertStmt.setDouble(2, (Double) row[1]);
                                insertStmt.executeUpdate();
                                System.out.println("Inserted: " + row[0]);
                            }
                        } else { // if记录存在，update
                            String updateQuery = "UPDATE exercise_data SET calories = ? WHERE name = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setDouble(1, (Double) row[1]); // 更新 calories
                                updateStmt.setString(2, (String) row[0]);
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