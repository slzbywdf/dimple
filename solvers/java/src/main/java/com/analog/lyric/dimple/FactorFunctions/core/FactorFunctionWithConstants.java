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

package com.analog.lyric.dimple.FactorFunctions.core;

import com.analog.lyric.dimple.model.DimpleException;


public class FactorFunctionWithConstants extends FactorFunction 
{
	private FactorFunctionBase _factorFunction;
	private Object [] _constants;
	private int [] _constantIndices;
	
	public FactorFunctionWithConstants(FactorFunctionBase factorFunction,
			Object [] constants, int [] constantIndices) 
	{
		super(factorFunction.getName());
		_factorFunction = factorFunction;
		_constants = constants;
		_constantIndices = constantIndices;
		
		if (_constantIndices.length > 1)
			for (int i = 1; i < _constantIndices.length; i++)
			{
				if (_constantIndices[i] <= _constantIndices[i-1])
					throw new DimpleException("constants must be provided in ascending index order");
			}
		
		if (_constantIndices.length != _constants.length)
			throw new DimpleException("need to specify the constants and their locations");
	}
	

	public Object [] getConstants()
	{
		return _constants;
	}
	
	public int [] getConstantIndices()
	{
		return _constantIndices;
	}
	
	@Override
	public double eval(Object... input) 
	{
		Object [] realInputs = new Object[input.length + _constantIndices.length];
		
		int curConstantIndexIndex = 0;
		int curInputIndex = 0;
		
		for (int i = 0; i < realInputs.length; i++)
		{
			if (curConstantIndexIndex < _constantIndices.length && _constantIndices[curConstantIndexIndex] == i)
			{
				//insert constant
				realInputs[i] = _constants[curConstantIndexIndex];
				curConstantIndexIndex++;
			}
			else
			{
				if (curInputIndex >= input.length)
					throw new DimpleException("incorrect number of arguments");
				
				realInputs[i] = input[curInputIndex];
				curInputIndex++;
			}
		}
		
		
		// TODO Auto-generated method stub
		return _factorFunction.eval(realInputs);
	}

}
