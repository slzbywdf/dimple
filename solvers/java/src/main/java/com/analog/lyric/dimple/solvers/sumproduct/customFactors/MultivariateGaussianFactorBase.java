/*******************************************************************************
*   Copyright 2012 Analog Devices, Inc.
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

package com.analog.lyric.dimple.solvers.sumproduct.customFactors;

import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.variables.VariablePredicates;
import com.analog.lyric.dimple.solvers.core.SFactorBase;
import com.analog.lyric.dimple.solvers.core.SMultivariateNormalEdge;
import com.analog.lyric.dimple.solvers.core.SolverFactorCreationException;
import com.analog.lyric.dimple.solvers.core.parameterizedMessages.MultivariateNormalParameters;
import com.analog.lyric.dimple.solvers.interfaces.ISolverFactorGraph;
import com.google.common.collect.Iterables;

public abstract class MultivariateGaussianFactorBase extends SFactorBase
{
	@SuppressWarnings("null")
	public MultivariateGaussianFactorBase(Factor factor, ISolverFactorGraph parent)
	{
		super(factor, parent);
	}
	/*---------------
	 * SNode methods
	 */
	
	@Override
	protected MultivariateNormalParameters cloneMessage(int edge)
	{
		return getSiblingEdgeState(edge).factorToVarMsg.clone();
	}
	
	@Override
	protected boolean supportsMessageEvents()
	{
		return true;
	}
	
	@SuppressWarnings("null")
	@Override
	public SMultivariateNormalEdge getSiblingEdgeState(int siblingIndex)
	{
		return (SMultivariateNormalEdge)getSiblingEdgeState_(siblingIndex);
	}

	/*-----------------------------------------------
	 * MultivariateGaussianFactorBase helper methods
	 */
	
	/**
	 * Asserts that factor has only unbounded RealJoint variables.
	 * <p>
	 * For use in subclass constructors.
	 * <p>
	 * @throws SolverFactorCreationException if assertion fails.
	 * @since 0.08
	 */
	protected void assertUnboundedRealJoint(Factor factor)
	{
		if (!Iterables.all(factor.getSiblings(), VariablePredicates.isUnboundedRealJoint()))
		{
			throw new SolverFactorCreationException("Cannot use %s with %s: not all variables are unbounded RealJoint",
				getClass().getSimpleName(), factor);
		}
	}

}
