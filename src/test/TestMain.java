package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestMain {

    public static void main (String[]args){
        Result res = JUnitCore.runClasses(VendingMachineTestSuite.class);
        for (Failure f : res.getFailures()){
            System.out.println(f.toString());
        }
        System.out.println("Done test execution");
        System.out.println("Total # of tests executed: " + res.getRunCount());
        System.out.println("Total # of passed tests: " + (res.getRunCount() - res.getFailures().size()));
        System.out.println("Total # of failed tests: " + res.getFailureCount());
    }
}
