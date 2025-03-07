package edu.meu.mgen.data;

public class Exercise {
    private String name;
    private double caloriesBurnedPerMinute;
    private String intensity;
    private double duration;

    public Exercise() {}
    // 构造函数，用于初始化运动名称和每分钟燃烧的卡路里
    public Exercise(String name, double caloriesBurnedPerMinute) {
        this.name = name;
        this.caloriesBurnedPerMinute = caloriesBurnedPerMinute;
    }

    // 获取运动名称
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // 设置运动强度
    public void setIntensity(String intensity) {
        this.intensity = intensity;
        adjustCaloriesBurnedPerMinuteBasedOnIntensity();
    }

    public String getIntensity() {
        return intensity;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    private void adjustCaloriesBurnedPerMinuteBasedOnIntensity() {
        if (intensity.equalsIgnoreCase("low")) {
            Math.round(caloriesBurnedPerMinute *= 0.8);
        } else if (intensity.equalsIgnoreCase("high")) {
            Math.round(caloriesBurnedPerMinute *= 1.2);
        }
    }

    // total calories burned
    public double calculateCaloriesBurned() {
        return Math.round(caloriesBurnedPerMinute * duration);
    }

    // calculate calories burned per minute
    public double getCaloriesBurnedPerMinute() {
        return Math.round(caloriesBurnedPerMinute);
    }
    public void setCaloriesPerMinute(double caloriesBurnedPerMinute) {
        this.caloriesBurnedPerMinute = caloriesBurnedPerMinute;
    }

    // 
    public void setCaloriesBurnedPerMinute(double caloriesBurnedPerMinute) {
        this.caloriesBurnedPerMinute = caloriesBurnedPerMinute;
    }
}

