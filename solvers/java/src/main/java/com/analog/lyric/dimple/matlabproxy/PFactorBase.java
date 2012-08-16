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

package com.analog.lyric.dimple.matlabproxy;

/*
 * Both Factors and FactorGraphs inherit from this.
 */
public abstract class PFactorBase implements IPNode 
{
	@Override
	public boolean isFactor() 
	{
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isVariable() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isGraph() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	abstract public boolean isDiscrete();


}
