package edu.meu.mgen.tracking;

import edu.meu.mgen.data.Food;
import edu.meu.mgen.data.Exercise;
import edu.meu.mgen.user.User;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Tracker {

    private double totalCaloriesIntake;
    private double totalCaloriesBurned;
    private String userDirectoryPath;
    private User user; // Reference to the User object

    // Listener for net calorie changes
    public interface NetCaloriesListener {
        void onNetCaloriesUpdate(double netCalories);
    }

    private NetCaloriesListener netCaloriesListener;

    public Tracker(String username, User user) {
        this.user = user;
        totalCaloriesIntake = 0;
        totalCaloriesBurned = 0;
        userDirectoryPath = "data/" + username;

        ensureDirectoryExists();
        loadTodayData(); // Load today's data only
    }

    // Ensure the user directory exists
    private void ensureDirectoryExists() {
        File userDir = new File(userDirectoryPath);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }

    // Load data for today only
    public void loadTodayData() {
        loadTodayFoodData();
        loadTodayExerciseData();
    }

    private void loadTodayFoodData() {
        String filePath = userDirectoryPath + "/food_records.csv";
        File foodFile = new File(filePath);
        if (!foodFile.exists()) return;

        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(foodFile))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String recordDate = fields[8].split(" ")[0]; // Extract date part
                if (recordDate.equals(today.format(dateFormatter))) {
                    double totalCalories = Double.parseDouble(fields[7]); // Total Calories field
                    totalCaloriesIntake += totalCalories;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void loadTodayExerciseData() {
        String filePath = userDirectoryPath + "/exercise_records.csv";
        File exerciseFile = new File(filePath);
        if (!exerciseFile.exists()) return;

        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(exerciseFile))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String recordDate = fields[5].split(" ")[0]; // Extract date part
                if (recordDate.equals(today.format(dateFormatter))) {
                    double totalCaloriesBurned = Double.parseDouble(fields[4]); // Total Calories Burned field
                    this.totalCaloriesBurned += totalCaloriesBurned;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Track food: update totalCaloriesIntake and write to CSV
    public void trackFood(Food food, double servingCount) {
        double calories = food.getCaloriesPerServing() * servingCount;
        totalCaloriesIntake += calories;
        notifyNetCaloriesChange(); // Trigger listener on net calorie change
        writeFoodToCsv(food, servingCount, calories);
    }

    // Track exercise: update totalCaloriesBurned and write to CSV
    public void trackExercise(Exercise exercise) {
        double caloriesBurned = exercise.calculateCaloriesBurned();
        totalCaloriesBurned += caloriesBurned;
        notifyNetCaloriesChange(); // Trigger listener on net calorie change
        writeExerciseToCsv(exercise, caloriesBurned);
    }

    // Write food entry to CSV
    private void writeFoodToCsv(Food food, double servingCount, double totalCalories) {
        String filePath = userDirectoryPath + "/food_records.csv";
        boolean fileExists = new File(filePath).exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (!fileExists) {
                writer.write("Name,Calories,Protein,Carbs,Fat,Serving Size,Serving Count,Total Calories,Timestamp\n");
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));; // Current date
            writer.write(String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%s\n",
                    food.getName(), food.getCaloriesPerServing(), food.getProtein(),
                    food.getCarbohydrates(), food.getFat(), food.getServingSize(),
                    servingCount, totalCalories, timestamp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write exercise entry to CSV
    private void writeExerciseToCsv(Exercise exercise, double totalCaloriesBurned) {
        String filePath = userDirectoryPath + "/exercise_records.csv";
        boolean fileExists = new File(filePath).exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (!fileExists) {
                writer.write("Name,Intensity,Calories Burned Per Minute,Duration,Total Calories Burned,Timestamp\n");
            }
            String timestamp = LocalDate.now() + " 00:00:00"; // Current date
            writer.write(String.format("%s,%s,%.2f,%.2f,%.2f,%s\n",
                    exercise.getName(), exercise.getIntensity(), exercise.getCaloriesBurnedPerMinute(),
                    exercise.getDuration(), totalCaloriesBurned, timestamp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set listener for net calorie changes
    public void setNetCaloriesListener(NetCaloriesListener listener) {
        this.netCaloriesListener = listener;
    }

    // Notify listener of net calorie change
    private void notifyNetCaloriesChange() {
        if (netCaloriesListener != null) {
            netCaloriesListener.onNetCaloriesUpdate(calculateNetCalories());
        }
    }

    // Calculate net calories (intake minus burned)
    public double calculateNetCalories() {
        return totalCaloriesIntake - totalCaloriesBurned;
    }

    // Get tracking summary for front-end display
    public Map<String, Double> getTrackingSummary() {
        Map<String, Double> summary = new HashMap<>();
        summary.put("Total Calories Intake", totalCaloriesIntake);
        summary.put("Total Calories Burned", totalCaloriesBurned);
        summary.put("Net Calories", calculateNetCalories());
        return summary;
    }
}
