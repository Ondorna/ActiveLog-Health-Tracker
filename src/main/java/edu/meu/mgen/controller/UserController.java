package edu.meu.mgen.controller;

import edu.meu.mgen.data.Exercise;
import edu.meu.mgen.data.ExerciseData;
import edu.meu.mgen.data.Food;
import edu.meu.mgen.data.FoodData;
import edu.meu.mgen.notification.Notification;
import edu.meu.mgen.tracking.Tracker;
import edu.meu.mgen.registration.UserRegistration;
import edu.meu.mgen.user.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {

    private List<String> notifications = new ArrayList<>();

    @Autowired
    private FoodData foodData;

    @Autowired
    private ExerciseData exerciseData;

    private User currentUser = null;
    private final UserRegistration userRegistration = new UserRegistration();

    private Notification notification;
    private Tracker tracker;


    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn")) {
            model.addAttribute("username", currentUser.getUsername());
            model.addAttribute("age", currentUser.getAge());
            model.addAttribute("gender", currentUser.getGender());
            model.addAttribute("height", currentUser.getHeight());
            model.addAttribute("weight", currentUser.getWeight());
            model.addAttribute("targetWeight", currentUser.getTargetWeight());

            double bmr = currentUser.calculateBMR(); // cal BMR
            model.addAttribute("user", currentUser);
            model.addAttribute("bmr", bmr);

            Map<String, Double> trackingSummary = tracker.getTrackingSummary();
            model.addAttribute("totalCaloriesIntake", trackingSummary.get("Total Calories Intake"));
            model.addAttribute("totalCaloriesBurned", trackingSummary.get("Total Calories Burned"));
            model.addAttribute("netCalories", trackingSummary.get("Net Calories"));

            model.addAttribute("notifications", notifications);
            return "index"; // return to index page
        }
        return "login"; // if not logged in, return to login page
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // 返回註冊頁面
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam int age,
            @RequestParam String gender,
            @RequestParam double height,
            @RequestParam double weight,
            @RequestParam double targetWeight,
            @RequestParam double targetCaloriesBurned,
            Model model) {

        boolean isRegistered = userRegistration.registerUser(username, password, email, age, gender, height, weight, targetWeight, targetCaloriesBurned);

        if (isRegistered) {
            model.addAttribute("message", "Registration successful! Please log in.");
            return "login";
        } else {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        boolean isAuthenticated = userRegistration.authenticateUser(username, password);

        if (isAuthenticated) {
            currentUser = userRegistration.getUserDetails(username);
            tracker = new Tracker(username, currentUser);
            tracker.loadTodayData();

            double targetNetCalories = currentUser.calculateBMR(); 
            notification = new Notification(targetNetCalories, currentUser.getTargetWeight(), notifications);

            // Send initial notifications
            notification.sendDailyReminder(currentUser);
            notification.registerNetCaloriesListener(tracker, currentUser);
            
            session.setAttribute("loggedIn", true);
            session.setAttribute("username", currentUser.getUsername());
            session.setAttribute("age", currentUser.getAge());
            session.setAttribute("gender", currentUser.getGender());
            session.setAttribute("height", currentUser.getHeight());
            session.setAttribute("weight", currentUser.getWeight());
            session.setAttribute("targetWeight", currentUser.getTargetWeight());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/selectFood")
    public String selectFood(Model model) {
        model.addAttribute("foodList", foodData.getFoodList());
        return "select_food";
    }

    @PostMapping("/addFoodRecord")
    public String addFoodRecord(
            @RequestParam String food,
            @RequestParam double servingCount,
            @RequestParam(value = "customFood", required = false) String customFood
    ) {
        Food selectedFood;

        if ("other".equalsIgnoreCase(food)) {
            if (customFood != null && !customFood.trim().isEmpty()) {
                selectedFood = new Food();
                selectedFood.setName(customFood.trim());
                selectedFood.setCaloriesPerServing(0.0); // 设置默认值为空或0
                selectedFood.setProtein(0.0);
                selectedFood.setCarbohydrates(0.0);
                selectedFood.setFat(0.0);
                selectedFood.setServingSize(0.0);
            } else {
                return "redirect:/error"; // 如果用户未填写名称，重定向到错误页面
            }
        } else {
            selectedFood = foodData.getFoodList().stream()
                    .filter(f -> f.getName().equals(food))
                    .findFirst()
                    .orElse(null);
        }

        // if (selectedFood != null && currentUser != null) {
        //     currentUser.writeFoodToCsv(selectedFood, servingCount);
        // }
        if (selectedFood != null && currentUser != null) {
            tracker.trackFood(selectedFood, servingCount); 
            notification.sendNetCaloriesNotification(tracker, currentUser);
        }
        return "redirect:/";
    }


    @GetMapping("/selectExercise")
    public String selectExercise(Model model) {
        model.addAttribute("exerciseList", exerciseData.getExerciseList());
        return "select_exercise";
    }

    @PostMapping("/addExerciseRecord")

    public String addExerciseRecord(
            @RequestParam String exercise,
            @RequestParam String intensity,
            @RequestParam double duration,
            @RequestParam(value = "customExercise", required = false) String customExercise,
            @RequestParam(value = "customCalories", required = false) Double customCalories
    ) {
        Exercise selectedExercise;

        if ("other".equalsIgnoreCase(exercise)) {
            if (customExercise != null && !customExercise.trim().isEmpty() && customCalories != null) {
                selectedExercise = new Exercise();
                selectedExercise.setName(customExercise.trim());
                selectedExercise.setCaloriesBurnedPerMinute(customCalories);
            } else {
                return "redirect:/error"; // 如果输入无效，重定向到错误页面
            }
        } else {
            selectedExercise = exerciseData.getExerciseList().stream()
                    .filter(e -> e.getName().equals(exercise))
                    .findFirst()
                    .orElse(null);
        }

        if (selectedExercise != null && currentUser != null) {
            selectedExercise.setIntensity(intensity);
            selectedExercise.setDuration(duration);
            tracker.trackExercise(selectedExercise); 
        }
        if (notification != null) {
            notification.sendDailyExerciseNotification(tracker, currentUser, 500.0); // 假设目标是500大卡
        }
        return "redirect:/";
    }

    @GetMapping("/userRecord")
    public String userRecord(Model model) {
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            List<Map<String, String>> foodRecords = readCsvRecords(currentUser.getUsername(), "food_records.csv");
            List<Map<String, String>> exerciseRecords = readCsvRecords(currentUser.getUsername(), "exercise_records.csv");
            model.addAttribute("foodRecords", foodRecords);
            model.addAttribute("exerciseRecords", exerciseRecords);
        }
        return "user_record";
    }


    private List<Map<String, String>> readCsvRecords(String username, String fileName) {
        List<Map<String, String>> records = new ArrayList<>();
        String filePath = "data/" + username + "/" + fileName;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine != null) {
                String[] headers = headerLine.split(",");
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    Map<String, String> record = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        record.put(headers[i], i < values.length ? values[i] : "Unknown");
                    }
                    records.add(record);
                    //System.out.println("Read record: " + record);  调试输出
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }


    @GetMapping("/logout")
    public String logout() {
        currentUser = null;
        return "redirect:/login";
    }
}
