package com.testlio.lib.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.XmlSuite;

import static java.lang.String.format;
import static com.testlio.lib.properties.Execution.properties;

@Slf4j
public class TestNGTestSuiteListener implements ISuiteListener {

	@Override
	public void onStart(ISuite suite) {
		XmlSuite.ParallelMode parallelMode = XmlSuite.ParallelMode
				.getValidParallel(properties().getParallelMode());
		int threadsCount = properties().getThreadCounts();

		log.info(format("Setting parallel params: Parallel Type is: %s and Thread Count is: %d", parallelMode.name(), threadsCount));
		suite.getXmlSuite().setParallel(parallelMode);
		suite.getXmlSuite().setThreadCount(threadsCount);
	}

	@Override
	public void onFinish(ISuite suite) {
        log.info(format("Suite run finished %s", suite.getName()));
	}

}