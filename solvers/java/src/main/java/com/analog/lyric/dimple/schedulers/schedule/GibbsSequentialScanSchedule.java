/*******************************************************************************
*   Copyright 2012 Analog Devices Inc.
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

package com.analog.lyric.dimple.schedulers.schedule;

import java.util.ArrayList;
import java.util.Iterator;

import com.analog.lyric.dimple.model.FactorGraph;
import com.analog.lyric.dimple.model.INode;
import com.analog.lyric.dimple.model.Port;
import com.analog.lyric.dimple.model.VariableBase;
import com.analog.lyric.dimple.model.VariableList;
import com.analog.lyric.dimple.schedulers.scheduleEntry.EdgeScheduleEntry;
import com.analog.lyric.dimple.schedulers.scheduleEntry.IScheduleEntry;
import com.analog.lyric.dimple.schedulers.scheduleEntry.NodeScheduleEntry;

/**
 * @author jeffb
 * 
 *         This is a dynamic schedule, which updates only one variable at a
 *         time, chosen sequentially. Prior to doing so, it updates the
 *         corresponding edges of the connected factors. This allows one
 *         iteration to correspond to exactly one variable update.
 * 
 *         WARNING: This schedule DOES NOT respect any existing sub-graph
 *         scheduler associations. That is, if any sub-graph already has an
 *         associated scheduler, that scheduler is ignored in creating this
 *         schedule. I believe this is a necessary limitation for Gibbs sampling
 *         to operate properly.
 * 
 */
public class GibbsSequentialScanSchedule extends ScheduleBase
{
	protected VariableList _variables;
	protected int _numVariables;
	protected int _currentVariableIndex;
	
	public GibbsSequentialScanSchedule(FactorGraph factorGraph)
	{
		_factorGraph = factorGraph;
		initialize();
	}
	
	@Override
	public void attach(FactorGraph factorGraph) 
	{
		super.attach(factorGraph);
		initialize();
	}

	protected void initialize()
	{
		_variables = _factorGraph.getVariables();
		_numVariables = _variables.size();
		_currentVariableIndex = 0;
	}

	@Override
	public Iterator<IScheduleEntry> iterator()
	{
		ArrayList<IScheduleEntry> updateList = new ArrayList<IScheduleEntry>();
		
		VariableBase v = ((ArrayList<VariableBase>)_variables.values()).get(_currentVariableIndex);
		if (++_currentVariableIndex >= _numVariables) _currentVariableIndex = 0;
		for (Port p : v.getPorts())
		{
			INode f = p.getConnectedNode();
			Port fPort = p.getSibling();
			updateList.add(new EdgeScheduleEntry(f,fPort));
		}
		updateList.add(new NodeScheduleEntry(v));
		
		return updateList.iterator();
	}

}
