package com.rokuality.test.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.*;
import org.testng.internal.ResultMap;

public class TestListener implements IRetryAnalyzer, ITestListener, IInvokedMethodListener {
	
	private static final String TEST_ID = "TestID-";
	private static final String RUN_PROPS = "runProps";

    private static final int MAX_COUNT = 2;
    private static ThreadLocal<String> testID = ThreadLocal.withInitial(() -> null);
    private Map<String, AtomicInteger> retries = new HashMap<String, AtomicInteger>();
    private IResultMap failedCases = new ResultMap();

	@Override
	public void onTestStart(ITestResult result) {
		String id = null;
		String commonID = result.getMethod().getMethodName() + " (" + result.getMethod().getGroups()[0] + ")";
		id = TEST_ID + commonID;

		testID.set(id.toString().toLowerCase());
		System.out.println(testID.get());
		System.out.println("====STARTING TEST: " + id + "====");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		teardownTest(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		teardownTest(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		teardownTest(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
    public void onFinish(final ITestContext context) {
        System.out.println("========TEST FINISH========");
    }

    @Override
    public void onStart(final ITestContext test) {
        System.out.println("========TEST START========");
    }

	public boolean retry(ITestResult result) {
    	boolean retry = false;
		if (isRetry()) {
			if (getCount(result.getMethod(), result.getAttribute(RUN_PROPS)).intValue() > 0) {
			    System.out.println("RETRY TEST: " + result.getInstanceName() + "." + result.getName());
			    getCount(result.getMethod(), result.getAttribute(RUN_PROPS)).decrementAndGet();
			    retry = true;
		    } else {
			    System.out.println("RETRY COMPLETE: " + result.getInstanceName() + "." + result.getName());
		    }
		}
		return retry;
    }

    private AtomicInteger getCount(ITestNGMethod result, Object attribute) {
	    String id = getId(result, attribute);
	    if (retries.get(id) == null) {
	        retries.put(id, new AtomicInteger(MAX_COUNT));
	    }
	    return retries.get(id);
    }
	
	private String getId(ITestNGMethod result, Object attribute) {
		return result.getConstructorOrMethod().getMethod().toGenericString() 
	    		+ ":" + String.valueOf(attribute);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		IRetryAnalyzer retryAnalyzer = result.getMethod().getRetryAnalyzer();
		if (retryAnalyzer != null) {
			if(result.getStatus() == ITestResult.SKIP && result.getThrowable() != null) {
				result.setStatus(ITestResult.FAILURE);
				Reporter.setCurrentTestResult(result);
			}
		}
	}

	@Override
	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		
	}

	private void teardownTest(ITestResult result) {
        try {
            String status = result.isSuccess() ? "SUCCESS" : "FAILURE";
            System.out.println("======" + status + "======");
            System.out.println("Test: " + result.getInstanceName() + "." + result.getName());
         } catch (Exception e) {
            e.printStackTrace();
        }

        try {
    		if (!result.isSuccess()) {
    			// check if the test should be re-executed based on retry logic
            	if (result.getMethod().getRetryAnalyzer() != null && isRetry()) {
        		    TestListener testRetryAnalyzer = (TestListener) result.getMethod().getRetryAnalyzer();
        		    if (testRetryAnalyzer.getCount(result.getMethod(), result.getAttribute(RUN_PROPS)).intValue() > 0) {
        		        result.setStatus(ITestResult.FAILURE);
        		    } else {
        		    	failedCases.addResult(result, result.getMethod());
        		    }
        		}
    		}
    	} catch (Exception e) {
    		System.out.println("Failed to log test id to results.");
    		e.printStackTrace();
    	}
	}
	
	private boolean isRetry() {
		return true;
	}
}
