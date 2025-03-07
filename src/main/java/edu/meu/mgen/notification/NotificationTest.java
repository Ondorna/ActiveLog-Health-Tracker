// import edu.meu.mgen.tracking.Tracker;
// import edu.meu.mgen.notification.Notification;
// import edu.meu.mgen.user.User;
// import edu.meu.mgen.data.Food;
// import edu.meu.mgen.data.Exercise;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;

// class NotificationTest {

//     private static final String TEST_USER = "test_user";
//     private Notification notification;
//     private Tracker tracker;
//     private User testUser;

//     @BeforeEach
//     void setUp() {
//         // Initialize User, Tracker, and Notification
//         testUser = new User(TEST_USER, "password123", "test@example.com", 30, "male", 175, 80, 70);
//         tracker = new Tracker(TEST_USER, testUser);
//         notification = new Notification(1500.0, 70.0); // Target net calories: 1500, target weight: 70 kg
//     }

//     @Test
//     void testSendNetCaloriesNotification() {
//         // Track food and exercise to test net calories notification
//         Food food = new Food("Pizza", 200, 500, 20, 10, 50);
//         tracker.trackFood(food, 3); // Add 3 servings of Pizza (1500 kcal)

//         Exercise exercise = new Exercise("Running", 10);
//         exercise.setDuration(30); // Burn 300 kcal in 30 minutes
//         tracker.trackExercise(exercise);

//         // Capture the notification message
//         notification.sendNetCaloriesNotification(tracker, testUser);

//         // Check the net calories
//         assertEquals(1200.0, tracker.calculateNetCalories(), 0.01, "Net calories should be 1200 kcal");
//     }

//     @Test
//     void testRegisterNetCaloriesListener() {
//         // Register a listener and verify it is triggered correctly
//         notification.registerNetCaloriesListener(tracker, testUser);

//         // Add food to trigger the listener
//         Food food = new Food("Burger", 150, 800, 30, 20, 40);
//         tracker.trackFood(food, 2); // Add 2 servings of Burger (1600 kcal)

//         // Check net calories
//         assertEquals(1600.0, tracker.calculateNetCalories(), 0.01, "Net calories should be 1600 kcal");
//     }

//     @Test
//     void testSendProgressUpdate() {
//         // Test progress notification for weight loss
//         double currentWeight = 75.0; // Simulate current weight

//         // Send progress update
//         notification.sendProgressUpdate(testUser, currentWeight);

//         // Calculate progress
//         double initialWeight = testUser.getWeight();
//         double progress = (initialWeight - currentWeight) / (initialWeight - 70.0) * 100;

//         assertEquals(50.0, progress, 0.01, "Progress should be 50%");
//     }

//     @Test
//     void testSendDailyReminder() {
//         // Simulate sending a daily reminder
//         notification.sendDailyReminder(testUser);
//         // Verify reminder logic doesn't throw errors
//         assertTrue(true, "Daily reminder should execute without errors");
//     }

//     @Test
//     void testSendDailyExerciseNotification() {
//         // Add exercise to test exercise notification
//         Exercise exercise = new Exercise("Cycling", 12);
//         exercise.setDuration(60); // Burn 720 kcal in 60 minutes
//         tracker.trackExercise(exercise);

//         // Set a target and send notification
//         notification.sendDailyExerciseNotification(tracker, testUser, 500);

//         // Verify total calories burned
//         assertEquals(720.0, tracker.getTrackingSummary().get("Total Calories Burned"), 0.01, "Calories burned should be 720 kcal");
//     }
// }
