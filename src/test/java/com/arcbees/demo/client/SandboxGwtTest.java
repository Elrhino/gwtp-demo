package com.arcbees.demo.client;

import com.google.gwt.junit.client.GWTTestCase;

public class SandboxGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.arcbees.demo.Demo";
    }

    public void testSandbox() {
        assertTrue(true);
    }
}
