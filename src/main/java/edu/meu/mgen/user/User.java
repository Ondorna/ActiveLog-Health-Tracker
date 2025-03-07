package edu.meu.mgen.user;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.meu.mgen.data.Exercise;
import edu.meu.mgen.data.Food;

public class User {
    private String username;
    private String password; // 存儲加密後的密碼
    // private String email;
    private int age;
    private String gender;
    private double height;
    private double weight;
    private double targetWeight;
    private double targetCaloriesBurned;
    private List<Food> foodRecords = new ArrayList<>();
    private List<Exercise> exerciseRecords = new ArrayList<>();


    public User(String username, String password, String email, int age, String gender, double height, double weight, double targetWeight, double targetCaloriesBurned) {
        this.username = username;
        this.password = password;
        // this.email = email;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.targetCaloriesBurned = targetCaloriesBurned;
        
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public double getTargetCaloriesBurned() {
        return targetCaloriesBurned;
    }

    public void setTargetCaloriesBurned(double targetCaloriesBurned) {
        this.targetCaloriesBurned = targetCaloriesBurned;
    }

    public double calculateBMR() {
        if (gender.equalsIgnoreCase("male")) {
            return Math.round(88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age));
        } else {
            return Math.round(447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age));
        }
    }

    public void writeFoodToCsv(Food food, double servingCount) {
        String directoryPath = "data/" + username;
        String filePath = directoryPath + "/food_records.csv";

        // 確保資料夾存在
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(filePath);
        boolean fileExists = file.exists();

        try (FileWriter writer = new FileWriter(filePath, true)) {
            // 如果文件不存在，添加標題行
            if (!fileExists) {
                writer.append("Name,Calories,Protein,Carbs,Fat,Serving Size,Serving Count,Total Calories,Timestamp\n");
            }
            // 添加食物記錄
            double totalCalories = food.calculateTotalCalories(servingCount);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.append(food.getName() != null ? food.getName() : "").append(",")
                    .append(food.getCaloriesPerServing() > 0 ? String.valueOf(food.getCaloriesPerServing()) : "").append(",")
                    .append(food.getProtein() > 0 ? String.valueOf(food.getProtein()) : "").append(",")
                    .append(food.getCarbohydrates() > 0 ? String.valueOf(food.getCarbohydrates()) : "").append(",")
                    .append(food.getFat() > 0 ? String.valueOf(food.getFat()) : "").append(",")
                    .append(food.getServingSize() > 0 ? String.valueOf(food.getServingSize()) : "").append(",")
                    .append(String.valueOf(servingCount)).append(",")
                    .append(totalCalories > 0 ? String.valueOf(totalCalories) : "").append(",")
                    .append(timestamp).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Write exercise record to CSV
    public void writeExerciseToCsv(Exercise exercise) {
        String directoryPath = "data/" + username;
        String filePath = directoryPath + "/exercise_records.csv";

        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directory: " + directoryPath);
            return;
        }

        try (FileWriter writer = new FileWriter(filePath, true)) {
            File file = new File(filePath);
            if (file.length() == 0) { // 检查文件是否为空，写入标题行
                writer.append("Name,Intensity,Calories Burned Per Minute,Duration,Total Calories Burned,Timestamp\n");
            }

            String name = exercise.getName() != null ? exercise.getName() : "Unknown";
            String intensity = exercise.getIntensity() != null ? exercise.getIntensity() : "Unknown";
            double caloriesBurnedPerMinute = exercise.getCaloriesBurnedPerMinute() > 0 ? exercise.getCaloriesBurnedPerMinute() : 0.0;
            double duration = exercise.getDuration() > 0 ? exercise.getDuration() : 0.0;
            double totalCaloriesBurned = caloriesBurnedPerMinute * duration;
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.append(exercise.getName()).append(",")
                    .append(exercise.getIntensity()).append(",")
                    .append(String.valueOf(exercise.getCaloriesBurnedPerMinute())).append(",")
                    .append(String.valueOf(exercise.getDuration())).append(",")
                    .append(String.valueOf(totalCaloriesBurned)).append(",")
                    .append(timestamp).append("\n");

            System.out.println("Successfully wrote exercise to CSV: " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public double calculateTotalCaloriesConsumed() {
        return foodRecords.stream().mapToDouble(food -> food.calculateTotalCalories(1)).sum();
    }

    public double calculateTotalCaloriesBurned() {
        return exerciseRecords.stream().mapToDouble(exercise -> exercise.calculateCaloriesBurned()).sum();
    }

}



