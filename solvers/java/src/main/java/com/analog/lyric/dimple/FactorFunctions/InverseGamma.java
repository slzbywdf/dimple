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

package com.analog.lyric.dimple.FactorFunctions;

import com.analog.lyric.dimple.FactorFunctions.core.FactorFunction;
import com.analog.lyric.dimple.FactorFunctions.core.FactorFunctionUtilities;
import com.analog.lyric.dimple.model.DimpleException;


/**
 * Inverse Gamma distribution. The variables in the argument list are ordered as follows:
 * 
 * Alpha: Alpha parameter of the iInverse Gamma distribution (non-negative)
 * Beta: Beta parameter of the Inverse Gamma distribution (non-negative)
 * x: Inverse Gamma distributed variable
 * 
 * Alpha and Beta parameters may optionally be specified as constants in the constructor.
 * In this case, they are not included in the list of arguments.
 * 
 */
public class InverseGamma extends FactorFunction
{
	double _alpha;
	double _beta;
	boolean _alphaSpecified = false;
	boolean _betaSpecified = false;
	
	public InverseGamma() {super("InverseGamma");}
	public InverseGamma(double alpha, double beta)
	{
		this();
		_alpha = alpha;
		_alphaSpecified = true;
		_beta = beta;
		_betaSpecified = true;
	}
	
    @Override
	public double evalEnergy(Object... arguments)
    {
    	int index = 0;
    	if (!_alphaSpecified)
    		_alpha = FactorFunctionUtilities.toDouble(arguments[index++]);	// First input is alpha parameter (must be non-negative)
    	if (!_betaSpecified)
    		_beta = FactorFunctionUtilities.toDouble(arguments[index++]);	// Second input is beta parameter (must be non-negative)
    	double x = FactorFunctionUtilities.toDouble(arguments[index++]);	// Third input is the Inverse Gamma distributed variable
    	if (_alpha < 0) throw new DimpleException("Negative alpha parameter. Domain must be restricted to non-negative values.");
    	if (_beta < 0) throw new DimpleException("Negative beta parameter. Domain must be restricted to non-negative values.");
    	
    	return _beta/x - _alpha * Math.log(_beta) + (_alpha + 1) * Math.log(x) + org.apache.commons.math.special.Gamma.logGamma(_alpha);
	}
    
    
}
