import { generateTest, generateStepsCombination, generateStepWithNestedSteps, getRandomResultType } from '../utils';

import { IStepConfig } from '../types/utils';

/**
 * This suite simulates very specific test scenarios,
 * allowing to different corner cases on Testlio platform
 *
 * To run this suite inclusively, add `--suite stress` to
 * TEST_ARGS value in .env file
 */

describe('When suite has a lot of tests', () => {
    const TESTS_COUNT = 50;

    Array(TESTS_COUNT)
        .fill(null)
        .map((_, index) =>
            generateTest({
                name: `Test #${index + 1}`,
                result: getRandomResultType(),
                steps: generateStepsCombination(3)
            })
        );
});

describe('When test has a lot of steps', () => {
    const STEPS_COUNT = 50;

    generateTest({
        name: `Test with ${STEPS_COUNT} steps`,
        steps: generateStepsCombination(STEPS_COUNT)
    });
});

describe('When test has a lot of nested steps', () => {
    const STEP_NESTING_DEPTH = 7;

    generateTest({
        name: `Test with ${STEP_NESTING_DEPTH} nested steps`,
        steps: generateStepWithNestedSteps(STEP_NESTING_DEPTH)
    });
});

describe('When test has a lot of labels', () => {
    const TEST_LABELS_COUNT = 50;

    generateTest({
        name: `Test with ${TEST_LABELS_COUNT} labels`,
        labelsCount: TEST_LABELS_COUNT
    });
});

describe('When test step has a lot of arguments', () => {
    const TEST_STEP_ARGUMENTS_COUNT = 50;

    const step: IStepConfig = {
        title: `Test step with ${TEST_STEP_ARGUMENTS_COUNT} arguments`,
        argumentsCount: TEST_STEP_ARGUMENTS_COUNT
    };

    generateTest({
        name: `Test with step, having ${TEST_STEP_ARGUMENTS_COUNT} arguments`,
        steps: [step]
    });
});
