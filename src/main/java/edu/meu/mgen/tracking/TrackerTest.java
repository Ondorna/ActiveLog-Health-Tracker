// import edu.meu.mgen.data.Food;
// import edu.meu.mgen.data.Exercise;
// import edu.meu.mgen.user.User;
// import edu.meu.mgen.tracking.Tracker;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;

// import static org.junit.jupiter.api.Assertions.*;

// class TrackerTest {

//     private static final String TEST_USER = "test_user";
//     private static final String TEST_DATA_DIR = "data/" + TEST_USER;
//     private Tracker tracker;
//     private User testUser;

//     @BeforeEach
//     void setUp() {
//         // Create a test user and initialize Tracker
//         testUser = new User(TEST_USER, "password123", "test@example.com", 30, "male", 175, 80, 70);
//         tracker = new Tracker(TEST_USER, testUser);

//         cleanDirectory();
//         createTestFiles();
//     }

//     @AfterEach
//     void tearDown() {
//         // Clean up test files and directory
//         cleanDirectory();
//     }

//     @Test
//     void testNetCaloriesCalculation() {
//         // Test that net calories are calculated correctly
//         assertEquals(300.0, tracker.calculateNetCalories(), 0.01, "Net calories should be 300 kcal");
//     }

//     @Test
//     void testTrackFood() {
//         // Test that tracking food updates totalCaloriesIntake
//         Food food = new Food("Pizza", 200, 500, 20, 10, 50);
//         tracker.trackFood(food, 2); // 2 servings
//         assertEquals(1300.0, tracker.calculateNetCalories(), 0.01, "Net calories should increase by 1000 kcal");
//     }

//     @Test
//     void testTrackExercise() {
//         // Test that tracking exercise updates totalCaloriesBurned
//         Exercise exercise = new Exercise("Running", 15);
//         exercise.setDuration(60); // 60 minutes
//         tracker.trackExercise(exercise);
//         assertEquals(-600.0, tracker.calculateNetCalories(), 0.01, "Net calories should decrease by 900 kcal");
//     }

//     @Test
//     void testNetCaloriesListener() {
//         // Test that the net calorie listener is triggered correctly
//         tracker.setNetCaloriesListener(netCalories -> {
//             assertEquals(400.0, netCalories, 0.01, "Listener should detect net calories as 400 kcal");
//         });

//         Food food = new Food("Salad", 100, 100, 5, 2, 10);
//         tracker.trackFood(food, 1); // Add 100 kcal
//     }

//     private void createTestFiles() {
//         // Create test food and exercise records for today's data
//         File foodFile = new File(TEST_DATA_DIR + "/food_records.csv");
//         File exerciseFile = new File(TEST_DATA_DIR + "/exercise_records.csv");

//         try {
//             if (!foodFile.exists()) {
//                 foodFile.getParentFile().mkdirs();
//                 foodFile.createNewFile();
//                 try (BufferedWriter writer = new BufferedWriter(new FileWriter(foodFile))) {
//                     writer.write("Name,Calories,Protein,Carbs,Fat,Serving Size,Serving Count,Total Calories,Timestamp\n");
//                     writer.write("Apple,50,0.3,14,0.2,100,1,50," + java.time.LocalDate.now() + " 00:00:00\n");
//                     writer.write("Banana,100,1.3,27,0.3,118,1,100," + java.time.LocalDate.now() + " 00:00:00\n");
//                 }
//             }
//             if (!exerciseFile.exists()) {
//                 exerciseFile.createNewFile();
//                 try (BufferedWriter writer = new BufferedWriter(new FileWriter(exerciseFile))) {
//                     writer.write("Name,Intensity,Calories Burned Per Minute,Duration,Total Calories Burned,Timestamp\n");
//                     writer.write("Walking,Moderate,5,30,150," + java.time.LocalDate.now() + " 00:00:00\n");
//                 }
//             }
//         } catch (IOException e) {
//             fail("Failed to create test files: " + e.getMessage());
//         }
//     }

//     private void cleanDirectory() {
//         // Delete all files and directories in the test data folder
//         File testDir = new File(TEST_DATA_DIR);
//         if (testDir.exists()) {
//             for (File file : testDir.listFiles()) {
//                 file.delete();
//             }
//             testDir.delete();
//         }
//     }
// }
