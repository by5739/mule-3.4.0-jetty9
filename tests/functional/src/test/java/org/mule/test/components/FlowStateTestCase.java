/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.test.components;

import static org.junit.Assert.*;

import org.mule.api.transport.MessageReceiver;
import org.mule.construct.AbstractFlowConstruct;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.AbstractConnector;

import org.junit.Test;

public class FlowStateTestCase extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "org/mule/test/components/flow-initial-state.xml";
    }

    @Test
    public void testDefaultInitialstate() throws Exception
    {
        doTestStarted("default");
    }

    @Test
    public void testStartedInitialstate() throws Exception
    {
        doTestStarted("started");
    }

    protected void doTestStarted(String flowName) throws Exception
    {
        AbstractFlowConstruct f = (AbstractFlowConstruct) muleContext.getRegistry().lookupFlowConstruct(
            flowName + "Flow");
        // Flow initially started
        assertTrue(f.isStarted());
        assertFalse(f.isStopped());

        // The listeners should be registered and started.
        doListenerTests(flowName, 1, true);
    }

    @Test
    public void testInitialStateStopped() throws Exception
    {
        AbstractFlowConstruct f = (AbstractFlowConstruct) muleContext.getRegistry().lookupFlowConstruct(
            "stoppedFlow");
        assertEquals("stopped", f.getInitialState());
        // Flow initially stopped
        assertFalse(f.isStarted());
        assertTrue(f.isStopped());

        // The connector should be started, but with no listeners registered.
        doListenerTests("stopped", 0, true);

        f.start();
        assertTrue(f.isStarted());
        assertFalse(f.isStopped());

        // The listeners should now be registered and started.
        doListenerTests("stopped", 1, true);
    }

    protected void doListenerTests(String receiverName, int expectedCount, boolean isConnected)
    {
        AbstractConnector connector = (AbstractConnector) muleContext.getRegistry().lookupConnector(
            "connector.test.mule.default");
        assertNotNull(connector);
        assertTrue(connector.isStarted());
        MessageReceiver[] receivers = connector.getReceivers("*" + receiverName + "*");
        assertEquals(expectedCount, receivers.length);
        for (int i = 0; i < expectedCount; i++)
        {
            assertEquals(isConnected, receivers[0].isConnected());
        }
    }

}
