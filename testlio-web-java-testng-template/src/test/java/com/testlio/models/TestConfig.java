package com.testlio.models;

public class TestConfig {
    private final String name;
    private ResultType result = ResultType.PASSED;
    private StepConfig[] steps;
    private Integer screenshotsCount;
    private Integer videosCount;
    private String testlioManualTestID;
    private Integer labelsCount;
    private Integer argumentsCount;
    private String message;

    public TestConfig(String name) {
        this.name = name;
    }

    public TestConfig(String name, ResultType result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
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

    public String getTestlioManualTestID() {
        return testlioManualTestID;
    }

    public void setTestlioManualTestID(String testlioManualTestID) {
        this.testlioManualTestID = testlioManualTestID;
    }

    public Integer getLabelsCount() {
        return labelsCount;
    }

    public void setLabelsCount(Integer labelsCount) {
        this.labelsCount = labelsCount;
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
