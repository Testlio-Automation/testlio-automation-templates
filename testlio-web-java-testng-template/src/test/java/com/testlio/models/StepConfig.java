package com.testlio.models;

public class StepConfig {
    private final String title;
    private ResultType result = ResultType.PASSED;
    private StepConfig[] steps;
    private Integer screenshotsCount;
    private Integer videosCount;
    private Integer argumentsCount;
    private String message;

    public StepConfig(String title) {
        this.title = title;
    }

    public StepConfig(String title, ResultType result) {
        this.title = title;
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public ResultType getResult() {
        return result;
    }

    public StepConfig[] getSteps() {
        return steps;
    }

    public void setSteps(StepConfig[] steps) {
        this.steps = steps;
    }

    public Integer getScreenshotsCount() {
        return screenshotsCount;
    }

    public void setScreenshotsCount(Integer screenshotsCount) {
        this.screenshotsCount = screenshotsCount;
    }

    public Integer getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(Integer videosCount) {
        this.videosCount = videosCount;
    }

    public Integer getArgumentsCount() {
        return argumentsCount;
    }

    public void setArgumentsCount(Integer argumentsCount) {
        this.argumentsCount = argumentsCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
