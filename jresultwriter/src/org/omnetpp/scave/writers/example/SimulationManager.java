/*
 * Copyright (c) 2010, Andras Varga and Opensim Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Opensim Ltd. nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Andras Varga or Opensim Ltd. BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.omnetpp.scave.writers.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.omnetpp.scave.writers.IOutputScalarManager;
import org.omnetpp.scave.writers.IOutputVectorManager;
import org.omnetpp.scave.writers.ISimulationTimeProvider;
import org.omnetpp.scave.writers.impl.FileOutputScalarManager;
import org.omnetpp.scave.writers.impl.FileOutputVectorManager;

public class SimulationManager {
    double now = 0.0;
    PriorityQueue<Event> fes = new PriorityQueue<Event>();
    List<Component> components = new ArrayList<Component>();

    IOutputScalarManager scalarManager;
    IOutputVectorManager vectorManager;

    public SimulationManager(String runID, Map<String,String> runAttributes, String resultFilenameBase) {
        scalarManager = new FileOutputScalarManager(resultFilenameBase+".sca");
        vectorManager = new FileOutputVectorManager(resultFilenameBase+".vec");
        vectorManager.setSimtimeProvider(new ISimulationTimeProvider() {
            public long getEventNumber() { return 0; /*not counted*/ }
            public Number getSimulationTime() { return now; }
        });
        scalarManager.open(runID, runAttributes);
        vectorManager.open(runID, runAttributes);
    }

    void simulate(double timeLimit) {
        while (!fes.isEmpty() && fes.peek().time < timeLimit) {
            Event event = fes.remove();
            now = event.time;
            event.execute();
        }

        for (Component c : components)
            c.recordSummaryResults();

        scalarManager.close();
        vectorManager.close();
    }

}