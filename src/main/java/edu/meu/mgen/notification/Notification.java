package edu.meu.mgen.notification;

import edu.meu.mgen.tracking.Tracker;
import edu.meu.mgen.user.User;
import java.util.List;

public class Notification {

    private double targetNetCalories;
    private double targetWeight;
    private List<String> notificationMessages;

    // Constructor
    public Notification(double targetNetCalories, double targetWeight, List<String> notificationMessages) {
        this.targetNetCalories = targetNetCalories;
        this.targetWeight = targetWeight;
        this.notificationMessages = notificationMessages;
    }

    /**
     * Sends a notification based on the net calories for the day.
     * Uses Tracker's real-time data to avoid duplicate file operations.
     */
    public void sendNetCaloriesNotification(Tracker tracker, User user) {
        double netCalories = tracker.calculateNetCalories();

        if (netCalories > targetNetCalories) {
            String message = "----------------------------------<br>" + "<strong>Alert:</strong><br>" + user.getUsername() +
                    ", your net calorie intake is " + netCalories +
                    " kcal today, exceeding " + targetNetCalories +
                    " kcal.<br>Remember to stay within your target.<br>";
            System.out.println(message);
            notificationMessages.add(0, message);
        } else {
            String message = "----------------------------------<br>" +"<strong>Updating! </strong><br>" + user.getUsername() +
                    "Your net calorie intake is " + netCalories +
                    " kcal today.<br>";
                    //  meeting your target of " + targetNetCalories + " kcal.<br>Keep it up!<br>";
            System.out.println(message);
            notificationMessages.add(0, message);
        }
    }

    /**
     * Registers a listener for real-time net calorie changes.
     * When net calories exceed the target, it triggers a notification automatically.
     */
    public void registerNetCaloriesListener(Tracker tracker, User user) {
        tracker.setNetCaloriesListener(netCalories -> {
            if (netCalories > targetNetCalories) {
                String message = "----------------------------------<br>" +"<strong>Real-time Alert:</strong><br>" + user.getUsername() +
                        ", your net calorie intake is " + netCalories +
                        " kcal, exceeding your daily target of " + targetNetCalories + " kcal.<br>";
                System.out.println(message);
                notificationMessages.add(0, message);
            }
        });
    }

    /**
     * Sends a weekly progress update based on the user's current and target weight.
     */
    public void sendProgressUpdate(User user, double currentWeight) {
        double initialWeight = user.getWeight(); // Assuming initial weight is stored in User
        double progress = (initialWeight - currentWeight) / (initialWeight - targetWeight) * 100;

        String message = "----------------------------------<br>" +"Progress Update: " + user.getUsername() +
                ", you have achieved " + String.format("%.2f", progress) +
                "% of your weight loss goal. Keep going!";
        System.out.println(message);
        notificationMessages.add(0, message);
    }

    /**
     * Sends a daily reminder to log food and exercise.
     */
    public void sendDailyReminder(User user) {
        String message = "----------------------------------<br>" +"Hi " + user.getUsername() +
                "! Don't forget to log your meals and exercises today.<br>";
        System.out.println(message);
        notificationMessages.add(0, message);
    }

    /**
     * Sends a notification for daily exercise progress based on the user's target calories burned.
     */
    public void sendDailyExerciseNotification(Tracker tracker, User user, double targetCaloriesBurned) {
        double totalCaloriesBurned = tracker.getTrackingSummary().get("Total Calories Burned");

        if (totalCaloriesBurned >= targetCaloriesBurned) {
            String message = "----------------------------------<br>" +"<strong>Congratulations</strong><br>" + user.getUsername() +
                    "! You've burned " + totalCaloriesBurned +
                    " kcal today, exceeding your goal of " + targetCaloriesBurned + " kcal!<br>Keep it up!<br>";
            System.out.println(message);
            notificationMessages.add(0, message);
        } else {
            String message = "----------------------------------<br>" +"Hey " + user.getUsername() +
                    ", you've burned " + totalCaloriesBurned +
                    " kcal today. Your goal is " + targetCaloriesBurned + " kcal. Keep going!<br>";
            System.out.println(message);
            notificationMessages.add(0, message);
        }
    }
}
