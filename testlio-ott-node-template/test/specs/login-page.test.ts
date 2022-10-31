import { describe } from 'mocha';
import { expect } from 'chai';
import allureReporter from '@wdio/allure-reporter'

const SUCCESSFUL_LOGIN_ALERT_TITLE = 'Successful login';

describe('When on login screen', function() {
    it('Checks that can login with correct credentials', async function() {
        allureReporter.startStep('Get desc list');
        await driver.pressKeyCode(85);
        await driver.pressKeyCode(20);
        await driver.pressKeyCode(23);

        await driver.pause(5000);
        const descList = await driver.$('id=com.example.android.tvleanback:id/description');
        expect(1).to.be.greaterThan(0);

        allureReporter.endStep();

        allureReporter.startStep('Get headers list');
        await driver.pressKeyCode(22);
        await driver.pressKeyCode(22);
        await driver.pressKeyCode(22);
        await driver.pressKeyCode(23);

        await driver.pause(5000);
        const headerList = await driver.$('id=com.example.android.tvleanback:id/browse_headers_dock');
        console.log(headerList);
        expect(1).to.be.greaterThan(0);

        allureReporter.endStep();

        allureReporter.startStep('Get control cards');
        await driver.pressKeyCode(20);
        await driver.pressKeyCode(20);
        await driver.pressKeyCode(23);

        await driver.pause(5000);

        await driver.pressKeyCode(22);
        await driver.pressKeyCode(22);
        await driver.pressKeyCode(23);

        await driver.pause(5000);

        await driver.pressKeyCode(23);

        await driver.pause(5000);

        const cardList = await driver.$('id=com.example.android.tvleanback:id/controls_card');
        expect(1).to.be.greaterThan(0);

        await driver.pause(5000);
        allureReporter.endStep();

        allureReporter.startStep('Pause & Play & Finish');
        await driver.pressKeyCode(85); // Pause
        await driver.pause(5000);

        await driver.pressKeyCode(85); // Play
        await driver.pause(5000);

        await driver.pressKeyCode(89);
        await driver.pressKeyCode(21);

        await driver.pause(5000);

        await driver.pressKeyCode(90);
        await driver.pressKeyCode(22);

        await driver.pause(5000);

        await driver.pressKeyCode(4);
        await driver.pressKeyCode(4);

        await driver.pause(5000);
        allureReporter.endStep();
    });
}).timeout(240000);
