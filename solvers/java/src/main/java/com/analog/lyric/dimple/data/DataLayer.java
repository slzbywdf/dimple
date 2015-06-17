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

package com.analog.lyric.dimple.data;

import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.variables.Variable;

/**
 * DataLayer holds variable data for {@link FactorGraph}s in a graph tree.
 * <p>
 * @param <D> is subclass of the {@link IDatum} interface.
 * @since 0.08
 * @author Christopher Barber
 * @see ValueDataLayer
 * @see GenericDataLayer
 */
public class DataLayer<D extends IDatum> extends DataLayerBase<Variable, D>
{
	/*--------------
	 * Construction
	 */
	
	public DataLayer(FactorGraph graph, FactorGraphData.Constructor<Variable, D> constructor)
	{
		super(graph, constructor);
	}
	
	public DataLayer(FactorGraph graph, DataDensity density, Class<D> baseType)
	{
		super(graph, density, Variable.class, baseType);
	}
	
	protected DataLayer(DataLayer<D> other)
	{
		super(other);
	}
	
	@Override
	public DataLayer<D> clone()
	{
		return new DataLayer<>(this);
	}
}
