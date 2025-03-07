package edu.meu.mgen.registration;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import edu.meu.mgen.user.User;


public class UserRegistration {

    private static final String FILE_PATH = "users.csv";

    // 註冊新用戶
    public boolean registerUser(String username, String password, String email, int age, String gender, double height, double weight, double targetWeight, double targetCaloriesBurned) {
        if (isUsernameAvailable(username)) {
            String hashedPassword = hashPassword(password);
            if (hashedPassword == null) {
                return false; // 密碼加密失敗
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(username + "," + hashedPassword + "," + email + "," + age + "," + gender + "," + height + "," + weight + "," + targetWeight + "," + targetCaloriesBurned);
                writer.newLine();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false; // 用戶名已存在
    }

    // 檢查用戶名是否可用
    public boolean isUsernameAvailable(String username) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                if (fields[0].equals(username)) {
                    return false; // 用戶名已存在
                }
            }
        } catch (FileNotFoundException e) {
            // 文件不存在，表示還沒有任何用戶註冊
            return true;
        }
        return true;
    }

    // 驗證用戶登錄信息
    public boolean authenticateUser(String username, String password) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                if (fields[0].equals(username)) {
                    String hashedPassword = hashPassword(password);
                    return hashedPassword != null && hashedPassword.equals(fields[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false; // 登錄失敗
    }

    // 獲取用戶詳細信息
    public User getUserDetails(String username) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                if (fields[0].equals(username)) {
                    return new User(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]), fields[4], Double.parseDouble(fields[5]), Double.parseDouble(fields[6]), Double.parseDouble(fields[7]), Double.parseDouble(fields[8]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null; 
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
