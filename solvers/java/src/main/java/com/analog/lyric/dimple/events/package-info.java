/*******************************************************************************
*   Copyright 2014 Analog Devices, Inc.
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
********************************************************************************/

/**
 * Classes for controlling the generation and handling of events specific to Dimple.
 * 
 * <h2>Overview</h2>
 * 
 * Dimple events are intended for use in monitoring changes to a Dimple model, data values
 * or solver state. The events can either by passed to an external monitoring system, logged
 * somewhere, or even used synchronously respond to changes. The event system is is designed to
 * have minimal overhead when there are no listeners active for a given event type.
 * 
 * <h2>Events</h2>
 * 
 * Events are represented by instances of concrete subclasses of the class
 * {@link com.analog.lyric.dimple.events.DimpleEvent}. There are three classes of event types,
 * represented by distinct abstract subclasses:
 * <ul>
 * <li>{@link com.analog.lyric.dimple.events.ModelEvent}: changes to model such as add or removal of factors or
 * variables.
 * <li>{@link com.analog.lyric.dimple.events.DataEvent}: changes to observed data values or variable input
 * distributions.
 * <li>{@link com.analog.lyric.dimple.events.SolverEvent}: solver-specific events such as message updates for
 * belief-propagation solvers or new sample values for sampling solvers.
 * </ul>
 * 
 * A full hierarchy of events can be seen easily using the Type Hierarchy view in Eclipse (press
 * Ctrl+Shift+H, type "DimpleEvent" in the dialog, and hit "Ok").
 * 
 * <h2>Event listeners</h2>
 * 
 * An objects of type {@link com.analog.lyric.dimple.events.DimpleEventListener} may be attached to the
 * root {@link com.analog.lyric.dimple.model.core.FactorGraph} of the model by the
 * {@link com.analog.lyric.dimple.model.core.FactorGraph#setEventListener} method and will be used to dispatch all events
 * generated by the model and its associated data and solver layers. If there is no listener on the root
 * graph, then no events should be generated. By default there is no listener for newly created graphs.
 * When creating a listener, you can either create a new instance or use the global default instance
 * that is lazily created by {@link com.analog.lyric.dimple.events.DimpleEventListener#getDefault()}.
 * <p>
 * One a listener has been associated with the graph, then one or more handlers may be registered to
 * handle events on the graph. The registration must specify the base class of the type of event to
 * be handled and the root object on which events will be raised. It will be easiest to register for
 * events at the root graph, but it may sometimes be desirable to set up handlers for specific variables,
 * factors or subgraphs. For instance, to register handlers for various belief propagation messages on
 * a graph, you could write:
 * 
 * <pre>
 *     DimpleEventListener listener = DimpleEventListener.getDefault();
 *     fg.setEventListener(listener);
 *     listener.register(factorMessageHandler, FactorToVariableMessageEvent.class, fg);
 *     listener.register(variableMessageHandler, VariableToFactorMessageEvent.class, fg);
 * </pre>
 * 
 * Handlers can be removed by one of the various {@code unregister} methods on the listener.
 * <p>
 * Changes to event registration or the value of the root listener are not guaranteed to take effect until
 * the next time the affected objects have been initialized or
 * {@link com.analog.lyric.dimple.events.IDimpleEventSource#notifyListenerChanged()}
 * has been invoked on the object that generates the event. This is important for model events in particular
 * since model changes typically occur prior to initialization.
 * 
 * <h2>Event handlers</h2>
 * 
 * Dimple event handlers are objects that implement the {@link com.analog.lyric.dimple.events.IDimpleEventHandler}
 * interface. In practice most handlers should simply extend {@link com.analog.lyric.dimple.events.DimpleEventHandler}.
 * For example, here is a simple handler class that simply prints events out to the console with a verbosity of one:
 * 
 * <pre>
 * public class EventPrinter extends DimpleEventHandler&LT;DimpleEvent&GT;
 * {
 *     public void handleEvent(DimpleEvent event)
 *     {
 *         event.println(System.out, 1);
 *     }
 * }
 * </pre>
 * 
 * Handler classes that are specific to a particular event subclass can be parameterized appropriately
 * to avoid the need for downcasts. For example, here is a simple handler that keeps a running total
 * of the total graph score during Gibbs sampling based on sample score differences:
 * 
 * <pre>
 * public class RunningScoreHandler extends DimpleEventHandler&LT;GibbsScoredVariableUpdateEvent&GT;
 * {
 *     public double score;
 * 
 *     RunningScoreHandler(double startingScore)
 *     {
 *         score = startingScore;
 *     }
 * 
 *     public void handleEvent(GibbsScoredVariableUpdateEvent event)
 *     {
 *         score += event.getScoreDifference();
 *     }
 * }
 * </pre>
 * 
 * <h2>Concurrency issues</h2>
 * 
 * Handlers are invoked synchronously as soon as the events have been constructed. Accessing the attributes
 * of the event objects is guaranteed to be thread safe, but calling methods on the event source or model
 * objects referred to in the event may not be safe.
 * 
 * <h2>Serialization</h2>
 * 
 * Dimple events implement the {@link java.io.Serializable} interface, which allows them to be serialized
 * to binary form for persistent storage or transmission to a remote monitor. However, note that events
 * purposely will not attempt to serialize references to model or solver elements, since that would require
 * serializing the entire graph. This means that deserialized event objects will contain null references for
 * any such object. Instead of serializing the actual object, the name and id of the object will be serialized.
 * 
 * @since 0.06
 * @author Christopher Barber
 */
package com.analog.lyric.dimple.events;
