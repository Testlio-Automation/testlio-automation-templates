package com.testlio.lib.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import static java.lang.String.format;
import static org.testng.ITestResult.FAILURE;
import static org.testng.ITestResult.SUCCESS;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

	int counter = 1;
	int retryLimit = 2;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IRetryAnalyzer#retry(org.testng.ITestResult)
	 * 
	 * This method decides how many times a test needs to be rerun. TestNg will call
	 * this method every time a test fails. So we can put some code in here to
	 * decide when to rerun the test.
	 * 
	 * Note: This method will return true if a tests needs to be retried and false
	 * it not.
	 *
	 */

	@Override
	public boolean retry(ITestResult result) {
		log.info(format("TEST CASE STATUS IS: %s", TestResultListener.getTestResult(result)));

		if (!result.isSuccess()) {
			if (counter < retryLimit) {
				log.info(format("Test failed : %s. Retrying execution: %d", result.getName(), counter));
				counter++;
				result.setStatus(FAILURE); // Mark test as failed
				return true;
			} else {
				result.setStatus(FAILURE); // If maxCount reached,test marked as failed
			}
		} else {
			result.setStatus(SUCCESS);
		}
		return false;
	}

}