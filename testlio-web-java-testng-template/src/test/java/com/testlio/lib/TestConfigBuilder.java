package com.testlio.lib;

import com.testlio.models.ResultType;
import com.testlio.models.StepConfig;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestConfigBuilder {
    public static StepConfig[] generateStepsCombination(int count, int screenshotsCount, int argumentsCount, ResultType result) {
        List<StepConfig> stepConfigs = IntStream.rangeClosed(1, count - 1).mapToObj(val -> {
            StepConfig stepConfig = new StepConfig("Passed step");
            stepConfig.setScreenshotsCount(screenshotsCount);
            stepConfig.setArgumentsCount(argumentsCount);
            return stepConfig;
        }).collect(Collectors.toList());

        String lastStepTitle;
        switch (result) {
            case ERRORED:
                lastStepTitle = "Errored step";
                break;
            case FAILED:
                lastStepTitle = "Failed step";
                break;
            default:
                lastStepTitle = "Passed step";
                break;
        }
        StepConfig lastStepConfig = new StepConfig(lastStepTitle, result);
        lastStepConfig.setScreenshotsCount(screenshotsCount);
        lastStepConfig.setArgumentsCount(argumentsCount);
        stepConfigs.add(lastStepConfig);

        StepConfig[] array = new StepConfig[stepConfigs.size()];
        return stepConfigs.toArray(array);
    }

    public static StepConfig[] generateStepsCombination(int count, int screenshotsCount, int argumentsCount) {
        return generateStepsCombination(count, screenshotsCount, argumentsCount, ResultType.PASSED);
    }

    public static StepConfig[] generateStepsCombination(int count) {
        return generateStepsCombination(count, 0, 0);
    }

    public static StepConfig[] generateStepWithNestedSteps(int depth, int screenshotsCount, int videosCount, int argumentsCount) {
        StepConfig root = new StepConfig("Root step");
        root.setScreenshotsCount(screenshotsCount);
        root.setVideosCount(videosCount);
        root.setArgumentsCount(argumentsCount);

        StepConfig current = root;
        for (int i = 0; i < depth; i++) {
            StepConfig step = new StepConfig(String.format("Child step (depth %d)", i + 1));
            step.setScreenshotsCount(screenshotsCount);
            step.setVideosCount(videosCount);
            step.setArgumentsCount(argumentsCount);

            current.setSteps(new StepConfig[]{ step });
            current = step;
        }

        return new StepConfig[]{ root };
    }

    public static StepConfig[] generateStepWithNestedSteps(int depth) {
        return generateStepWithNestedSteps(depth, 0, 0, 0);
    }
}
