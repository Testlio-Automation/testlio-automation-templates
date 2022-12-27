import { assert } from 'chai';
import allureReporter from '@wdio/allure-reporter';
import fs from 'fs';
import path from 'path';
import { ResultType, IStepConfig, ITestConfig } from '../types/utils';

export const attachFakeScreenshot = async (name?: string) => {
    allureReporter.addAttachment(
        name || 'Screenshot',
        await fs.promises.readFile(path.resolve(__dirname, '..', 'assets', 'image.png')),
        'image/png'
    );
};

export const attachFakeVideo = async (name?: string) => {
    allureReporter.addAttachment(
        name || 'Execution video',
        await fs.promises.readFile(path.resolve(__dirname, '..', 'assets', 'video.mp4')),
        'video/mp4'
    );
};

export const addTestlioManualTestId = (testlioManualTestID: string) => {
    allureReporter.addLabel('testlioManualTestID', testlioManualTestID);
};

export const generateStep = async (config: IStepConfig) => {
    const { title, result, steps, screenshotsCount, videosCount, argumentsCount, message } = config;
    allureReporter.startStep(title);
    if (screenshotsCount) {
        await Promise.all(
            Array(screenshotsCount)
                .fill(null)
                .map((_, index) => attachFakeScreenshot(`${title} (${index + 1})`))
        );
    }
    if (videosCount) {
        await Promise.all(
            Array(videosCount)
                .fill(null)
                .map((_, index) => attachFakeVideo(`${title} (${index + 1})`))
        );
    }
    if (argumentsCount) {
        Array(argumentsCount)
            .fill(null)
            .map((_, index) => allureReporter.addArgument(`argument_${index + 1}`, `argument_value_${index + 1}`));
    }
    if (steps) {
        // eslint-disable-next-line no-restricted-syntax
        for (const stepConfig of steps) {
            await generateStep(stepConfig);
        }
    }
    switch (result) {
        case ResultType.ERRORED:
            throw new Error(message || 'This step should throw error');
        case ResultType.FAILED:
            assert.isTrue(false, message || 'This step should fail');
            break;
        default:
            if (message) {
                console.log(message);
            }
            break;
    }
    allureReporter.endStep();
};

export const generateStepsCombination = (
    count: number,
    config?: Omit<IStepConfig, 'title' | 'result'>,
    result?: ResultType
): IStepConfig[] => {
    const configs: IStepConfig[] = [];

    const STEP_TITLE_MAPPING = {
        [ResultType.PASSED]: 'Passed step',
        [ResultType.ERRORED]: 'Errored step',
        [ResultType.FAILED]: 'Failed step'
    };

    configs.push(
        ...Array(count - 1)
            .fill(null)
            .map((_, index) => ({
                title: `${STEP_TITLE_MAPPING[ResultType.PASSED]} #${index + 1}`,
                ...config
            }))
    );

    configs.push({
        title: `${STEP_TITLE_MAPPING[result || ResultType.PASSED]} #${count}`,
        result,
        ...config
    });

    return configs;
};

export const generateStepWithNestedSteps = (
    depth: number,
    config?: Omit<IStepConfig, 'title' | 'result' | 'steps'>
): IStepConfig[] => {
    const root: IStepConfig = {
        title: 'Root step',
        ...config
    };

    let current = root;

    Array(depth)
        .fill(null)
        .forEach((_, index) => {
            const step = {
                title: `Child step (depth ${index + 1})`,
                ...config
            };
            current.steps = [step];
            current = step;
        });

    return [root];
};

export const generateTest = (config: ITestConfig) => {
    const {
        name,
        result,
        steps,
        screenshotsCount,
        videosCount,
        testlioManualTestID,
        message,
        labelsCount,
        argumentsCount
    } = config;

    return it(name, async () => {
        if (screenshotsCount) {
            await Promise.all(
                Array(screenshotsCount)
                    .fill(null)
                    .map((_, index) => attachFakeScreenshot(`${name} (${index + 1})`))
            );
        }
        if (videosCount) {
            await Promise.all(
                Array(videosCount)
                    .fill(null)
                    .map((_, index) => attachFakeVideo(`${name} (${index + 1})`))
            );
        }
        if (labelsCount) {
            Array(labelsCount)
                .fill(null)
                .map((_, index) => allureReporter.addLabel(`label_${index + 1}`, `label_value_${index + 1}`));
        }
        if (argumentsCount) {
            Array(argumentsCount)
                .fill(null)
                .map((_, index) => allureReporter.addArgument(`argument_${index + 1}`, `argument_value_${index + 1}`));
        }
        if (testlioManualTestID) {
            addTestlioManualTestId(testlioManualTestID);
        }
        if (steps) {
            // eslint-disable-next-line no-restricted-syntax
            for (const stepConfig of steps) {
                await generateStep(stepConfig);
            }
        }
        switch (result) {
            case ResultType.ERRORED:
                throw new Error(message || 'This test should throw error');
            case ResultType.FAILED:
                assert.isTrue(false, message || 'This test should fail');
                break;
            default:
                if (message) {
                    console.log(message);
                }
                break;
        }
    });
};

export const getRandomResultType = (): ResultType => {
    const enumValues = Object.keys(ResultType)
        .map((n) => Number.parseInt(n, 10))
        .filter((n) => !Number.isNaN(n)) as unknown as ResultType[];
    const randomIndex = Math.floor(Math.random() * enumValues.length);
    return enumValues[randomIndex];
};
