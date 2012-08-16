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

package com.analog.lyric.dimple.model.repeated;

import com.analog.lyric.dimple.FactorFunctions.NopFactorFunction;
import com.analog.lyric.dimple.model.Factor;
import com.analog.lyric.dimple.model.Port;
import com.analog.lyric.dimple.model.VariableBase;

public class BlastFromThePastFactor extends Factor 
{
	
	private Object _msg;
	
	public BlastFromThePastFactor(int id, VariableBase var, Object msg) 
	{
		super(id,new NopFactorFunction("BlastFromThePast"),new VariableBase[]{var});		
		setOutputMsg(msg);

	}

	public void setOutputMsg(Object msg) 
	{
		_msg = msg;
		_ports.get(0).setOutputMsg(_msg);
	}
	
	public void initializePortMsg(Port port)
	{
		
	}
	
	
	
	@Override
	public void update()  
	{
		setOutputMsg(_msg);		
	}

	@Override
	public void updateEdge(int outPortNum)  
	{
		setOutputMsg(_msg);
		
	}
	
	
}
