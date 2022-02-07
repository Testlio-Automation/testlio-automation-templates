"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.config = void 0;
var config = {
    seleniumBrowser: process.env.SELENIUM_BROWSER,
    envUrl: process.env.ENV_URL,
    username: 'testlio',
    password: 'lovetesting'
};
exports.config = config;
