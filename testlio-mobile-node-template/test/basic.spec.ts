import { generateStepsCombination, generateTest } from '../utils';
import { ResultType } from '../types/utils';

/**
 * This suite simulates basic test scenarios,
 * allowing to test features of Testlio automation platform
 *
 * To run this suite inclusively, add `--suite basic` to
 * TEST_ARGS value in .env file
 */

const MANUAL_TEST_GUID = 'some-manual-test-guid';

const COMMON_TEST_PARAMETERS = {
    screenshotsCount: 3,
    videosCount: 1,
    labelsCount: 3,
    argumentsCount: 3
};

const COMMON_STEP_PARAMETERS = {
    screenshotsCount: 3,
    argumentsCount: 3
};

describe('When all tests in suite passed', () => {
    const PASSED_TESTS_COUNT = 3;

    Array(PASSED_TESTS_COUNT)
        .fill(null)
        .map((_, index) =>
            generateTest({
                name: `test should pass #${index + 1}`,
                steps: generateStepsCombination(3, COMMON_STEP_PARAMETERS),
                ...COMMON_TEST_PARAMETERS
            })
        );
});

// eslint-disable-next-line mocha/max-top-level-suites
describe('When some tests in suite failed', () => {
    const PASSED_TESTS_COUNT = 2;
    const FAILED_TESTS_COUNT = 1;

    Array(PASSED_TESTS_COUNT)
        .fill(null)
        .map((_, index) =>
            generateTest({
                name: `test should pass #${index + 1}`,
                steps: generateStepsCombination(3, COMMON_STEP_PARAMETERS),
                ...COMMON_TEST_PARAMETERS
            })
        );

    Array(FAILED_TESTS_COUNT)
        .fill(null)
        .map((_, index) =>
            generateTest({
                name: `test should fail #${index + 1}`,
                steps: generateStepsCombination(3, COMMON_STEP_PARAMETERS, ResultType.FAILED),
                result: ResultType.FAILED,
                ...COMMON_TEST_PARAMETERS
            })
        );
});

describe('When some tests in suite errored', () => {
    const PASSED_TESTS_COUNT = 2;
    const ERRORED_TESTS_COUNT = 1;

    Array(PASSED_TESTS_COUNT)
        .fill(null)
        .map((_, index) =>
            generateTest({
                name: `test should pass #${index + 1}`,
                steps: generateStepsCombination(3, COMMON_STEP_PARAMETERS),
                ...COMMON_TEST_PARAMETERS
            })
        );

    Array(ERRORED_TESTS_COUNT)
        .fill(null)
        .map((_, index) =>
            generateTest({
                name: `test should be errored #${index + 1}`,
                steps: generateStepsCombination(3, COMMON_STEP_PARAMETERS, ResultType.ERRORED),
                result: ResultType.ERRORED,
                ...COMMON_TEST_PARAMETERS
            })
        );
});

describe('When Testlio manual test ID defined', () => {
    generateTest({
        name: `Test with Testlio manual test ID `,
        testlioManualTestID: MANUAL_TEST_GUID
    });
});

describe('When common problems arise', () => {
    generateTest({
        name: `LOG_APPIUM_SERVER_NEVER_STARTED common problem type should be detected`,
        message: 'appium server never started in 5 seconds. Exiting'
    });

    generateTest({
        name: `LOG_DEVICE_WAS_NOT_IN_THE_LIST common problem type should be detected`,
        message: 'An unknown server-side error occured while processing the command. Original error: Google Nexus was not in the list of connected devices'
    });
});
