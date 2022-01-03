package com.testlio.lib.pagefactory;

import org.openqa.selenium.WebDriverException;

public class TestlioElementException extends WebDriverException {

    public TestlioElementException(Exception e) {
        super(e);
    }

    public TestlioElementException(String message) {
        super(message);
    }

}