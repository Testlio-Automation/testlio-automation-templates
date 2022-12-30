export enum ResultType {
    PASSED,
    FAILED,
    ERRORED
}

export interface IStepConfig {
    title: string;
    result?: ResultType;
    steps?: IStepConfig[];
    screenshotsCount?: number;
    videosCount?: number;
    argumentsCount?: number;
    message?: string;
}

export interface ITestConfig {
    name: string;
    body?: () => {};
    result?: ResultType;
    steps?: IStepConfig[];
    screenshotsCount?: number;
    videosCount?: number;
    testlioManualTestID?: string;
    labelsCount?: number;
    argumentsCount?: number;
    message?: string;
}
