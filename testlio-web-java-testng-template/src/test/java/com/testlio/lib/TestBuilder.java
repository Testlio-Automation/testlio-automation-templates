package com.testlio.lib;

import com.testlio.models.StepConfig;
import com.testlio.models.TestConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBuilder {
    public void build(TestConfig testConfig) throws Exception {

        String name = testConfig.getName();

        Integer screenshotsCount = testConfig.getScreenshotsCount();
        if (screenshotsCount != null) {
            attachFakeScreenshots(name, screenshotsCount);
        }

        Integer videosCount = testConfig.getVideosCount();
        if (videosCount != null) {
            attachFakeVideos(name, videosCount);
        }

        Integer labelsCount = testConfig.getLabelsCount();
        if (labelsCount != null) {
            IntStream.rangeClosed(1, labelsCount).forEach(val ->
                    Allure.label(String.format("label_%d", val), String.format("label_value_%d", val))
            );
        }

        Integer argumentsCount = testConfig.getArgumentsCount();
        if (argumentsCount != null) {
            IntStream.rangeClosed(1, argumentsCount).forEach(val ->
                Allure.parameter(String.format("argument_%d", val), String.format("argument_value_%d", val))
            );
        }

        String testlioManualTestID = testConfig.getTestlioManualTestID();
        if (testlioManualTestID != null) {
            Allure.label("testlioManualTestID", testlioManualTestID);
        }

        StepConfig[] steps = testConfig.getSteps();
        if (steps != null) {
            Arrays.stream(steps).forEach(this::generateStep);
        }

        String message = testConfig.getMessage();
        switch (testConfig.getResult()) {
            case ERRORED:
                throw new Exception(
                        message != null ? message : "This test should throw error"
                );
            case FAILED:
                assertThat(false).isTrue().withFailMessage(
                        message != null ? message : "This test should fail"
                );
                break;
            default:
                if (message != null) {
                    System.out.println(message);
                }
        }
    }
    private void generateStep(StepConfig stepConfig) {
        String title = stepConfig.getTitle();
        Allure.step(title, () -> {
            Integer screenshotsCount = stepConfig.getScreenshotsCount();
            if (screenshotsCount != null) {
                attachFakeScreenshots(title, screenshotsCount);
            }

            Integer argumentsCount = stepConfig.getArgumentsCount();
            if (argumentsCount != null) {
                List<Parameter> parameters = IntStream.rangeClosed(1, argumentsCount).mapToObj(val -> {
                    Parameter parameter = new Parameter();
                    parameter.setName(String.format("argument_%d", val));
                    parameter.setValue(String.format("argument_value_%d", val));
                    return parameter;
                }).collect(Collectors.toList());
                AllureLifecycle lifecycle = Allure.getLifecycle();
                lifecycle.updateStep(stepResult -> stepResult.setParameters(parameters));
            }

            StepConfig[] steps = stepConfig.getSteps();
            if (steps != null) {
                Arrays.stream(steps).forEach(this::generateStep);
            }

            String message = stepConfig.getMessage();
            switch (stepConfig.getResult()) {
                case ERRORED:
                    throw new Exception(
                            message != null ? message : "This step should throw error"
                    );
                case FAILED:
                    assertThat(false).isTrue().withFailMessage(
                            message != null ? message : "This step should fail"
                    );
                    break;
                default:
                    if (message != null) {
                        System.out.println(message);
                    }
            }
        });
    }

    private void attachFakeScreenshot(String name) throws IOException {
        attachAssetToAllure(
                name,
                "image/png",
                getFileFromResourceAsStream("assets/image.png"),
                "png"
        );
    }

    private void attachFakeScreenshots(String name, int count) {
        IntStream.rangeClosed(1, count).forEach(val -> {
            try {
                attachFakeScreenshot(String.format("%s (%d)", name, val));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void attachFakeVideo(String name) throws IOException {
        attachAssetToAllure(
                name,
                "video/mp4",
                getFileFromResourceAsStream("assets/video.mp4"),
                "mp4"
        );
    }

    private void attachFakeVideos(String name, int count) {
        IntStream.rangeClosed(1, count).forEach(val -> {
            try {
                attachFakeVideo(String.format("%s (%d)", name, val));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void attachAssetToAllure(String name, String type, InputStream inputStream, String fileExtension) throws IOException {
        byte[] byteArr = IOUtils.toByteArray(inputStream);
        Allure.addAttachment(
                name,
                type,
                new ByteArrayInputStream(byteArr),
                fileExtension
        );
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
