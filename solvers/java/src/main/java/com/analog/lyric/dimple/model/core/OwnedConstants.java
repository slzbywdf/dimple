/*******************************************************************************
*   Copyright 2015 Analog Devices, Inc.
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

package com.analog.lyric.dimple.model.core;

import org.eclipse.jdt.annotation.Nullable;

import com.analog.lyric.dimple.model.variables.Constant;

/**
 * 
 * @since 0.08
 * @author Christopher Barber
 */
public class OwnedConstants extends OwnedArray<Constant>
{
	/*--------------
	 * Construction
	 */
	
	OwnedConstants()
	{
		super();
	}

	/*
	 * Methods
	 */
	
	@Override
	int idTypeMask()
	{
		return Ids.CONSTANT_TYPE << Ids.LOCAL_ID_TYPE_OFFSET;
	}
	
	@Override
	Constant[] resize(@Nullable Constant[] array, int length)
	{
		final Constant[] newArray = new Constant[length];
		if (array != null)
		{
			System.arraycopy(array, 0, newArray, 0, Math.min(length, array.length));
		}
		return newArray;
	}

}
