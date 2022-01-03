package com.testlio.lib.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class TestNGMethodInterceptor implements IMethodInterceptor {

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        log.info("Running TestNGMethodInterceptor");
        return methods;
    }

    private boolean methodGroupsContainingSetGroups(String[] methodGroups, String[] setIncludedGroupsArray) {
        if (ArrayUtils.isEmpty(methodGroups) || ArrayUtils.isEmpty(setIncludedGroupsArray)) {
            return true;
        }
        return Arrays.asList(methodGroups).containsAll(Arrays.asList(setIncludedGroupsArray));
    }

}
