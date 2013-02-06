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

package com.analog.lyric.util.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.analog.lyric.dimple.model.Discrete;
import com.analog.lyric.dimple.model.Factor;
import com.analog.lyric.dimple.model.FactorGraph;
import com.analog.lyric.dimple.model.INameable;
import com.analog.lyric.dimple.model.INode;
import com.analog.lyric.dimple.model.Port;



public class FactorGraphDiffs
{
	private INameable _a = null;
	private INameable _b = null;
	private ArrayList<INameable>	_childrenInANotB = null;
	private ArrayList<INameable>	_childrenInBNotA = null;
	private ArrayList<FactorGraphDiffs> _childDiffs = null;
	
	public FactorGraphDiffs()
	{
		this(null, null);
	}
	public FactorGraphDiffs(INameable a, INameable b)
	{
		_a = a;
		_b = b;
	}
	
	public INameable getA()
	{
		return _a;
	}
	public INameable getB()
	{
		return _a;
	}
	public ArrayList<INameable> inANotB()
	{
		return _childrenInANotB;
	}
	public ArrayList<INameable> inBNotA()
	{
		return _childrenInBNotA;
	}
	public ArrayList<FactorGraphDiffs> childDiffs()
	{
		return _childDiffs;
	}

	static private String tabString(int numTabs)
	{
		String s = "";
		for(int i = 0; i < numTabs; ++i)
		{
			s += "\t";
		}
		return s;
	}
	
	static private String typeString(INameable n)
	{
		String s = "";
		if(n instanceof FactorGraph)
		{
			s = "Graph   ";
		}
		else if(n instanceof Discrete)
		{
			s = "Variable";
		}
		else if(n instanceof Factor)
		{
			s = "Factor  ";
		}
		else
		{
			s = n.getClass().toString();
		}
		return s;
	}
	
	public String toString(int numTabs)
	{
		String s = "";

		int pathDBG = 0;
		try
		{
			//String tabString = tabString(numTabs);
			if(numTabs == 0)
			{
				pathDBG |= 0x00000001;
				s += "diffs ";
			}
			pathDBG |= 0x00000002;
			String aName = "none";
			String bName = "none";
			int inANotB = 0;
			int inBNotA = 0;
			int childDiffs = 0;
			String typeString = "none";
			if(_a != null)
			{
				aName = _a.getQualifiedLabel();
				typeString = typeString(_a);
			}
			if(_b != null)
			{
				bName = _b.getQualifiedLabel();
				typeString = typeString(_b);
			}
			if(_childrenInANotB != null)
			{
				inANotB = _childrenInANotB.size();
			}
			if(_childrenInBNotA != null)
			{
				inANotB = _childrenInBNotA.size();
			}
			if(_childDiffs != null)
			{
				childDiffs = _childDiffs.size();
			}
			
			s += String.format("[%s]  a:[%s]  b:[%s]  inANotB:%d  inBNotA:%d  childDiffs:%d"
					,typeString
					,aName
					,bName
					,inANotB
					,inBNotA
					,childDiffs);
			pathDBG |= 0x00000004;
			String oneMoreTab = tabString(numTabs + 1);
			String twoMoreTabs = tabString(numTabs + 2);
			pathDBG |= 0x00000008;
			if(_childrenInANotB != null)
			{
				pathDBG |= 0x00000010;
				s += String.format("\n%sIn A not B:\n", oneMoreTab);
				for(INameable n : _childrenInANotB)
				{
					pathDBG |= 0x00000020;
					s += twoMoreTabs;
					s += "[";
					s += typeString(n);
					s += "] ";
					if(n != null)
					{
						s += n.getQualifiedLabel();
					}
					else
					{
						s += "none";
					}
					s += "\n";
				}
			}
			pathDBG |= 0x00000040;
			if(_childrenInBNotA != null)
			{
				pathDBG |= 0x00000080;
				s += String.format("\n%sIn B not A:\n", oneMoreTab);
				for(INameable n : _childrenInBNotA)
				{
					pathDBG |= 0x00000100;
					s += twoMoreTabs;
					s += "[";
					s += typeString(n);
					s += "] ";
					if(n != null)
					{
						s += n.getQualifiedLabel();
					}
					else
					{
						s += "none";
					}
					s += "\n";
				}
			}
			pathDBG |= 0x00000200;
			if(_childDiffs != null)
			{
				pathDBG |= 0x00000400;
				s += String.format("\n%sChild diffs:\n", oneMoreTab);
				for(FactorGraphDiffs d : _childDiffs)
				{
					pathDBG |= 0x00000800;
					s += twoMoreTabs;
					if(d != null)
					{
						//s += n.getQualifiedLabel();
						s += d.toString(numTabs + 1);
					}
					else
					{
						s += "none";
					}
					s += "\n";
				}
				pathDBG |= 0x00001000;
			}
			pathDBG |= 0x00002000;
		}
		catch(Exception e)
		{
			s += "\nException - " + e.toString() + " Path debug: " + pathDBG;
		}
		return s;
		
	}
	
	@Override
	public String toString()
	{
		return toString(0);
	}
	
	public void print()
	{
		print("");
	}
	public void print(String tag)
	{
		System.out.println(tag + " - " + toString());
	}
	public boolean noDiffs()
	{
		return  _childrenInANotB == null &&
				_childrenInBNotA == null &&
				_childDiffs == null;
	}
	private void addANotB(INameable node)
	{
		if(_childrenInANotB == null)
		{
			_childrenInANotB = new ArrayList<INameable>();
		}
		_childrenInANotB.add(node);
		//System.out.println("--addANotB [" + node.getLabel() + "] [\n" + toString() + "\n]\n");
	}
	private void addBNotA(INameable node)
	{
		if(_childrenInBNotA == null)
		{
			_childrenInBNotA = new ArrayList<INameable>();
		}
		_childrenInBNotA.add(node);
		//System.out.println("--addBNotA [" + node.getLabel() + "] [\n" + toString() + "\n]\n");
	}
	private void addDifference(FactorGraphDiffs difference)
	{
		if(_childDiffs == null)
		{
			_childDiffs = new ArrayList<FactorGraphDiffs>();
		}
		_childDiffs.add(difference);
	}
	private void addDifference(INameable a, INameable b, ArrayList<FactorGraphDiffs> childFactorGraphDiffs)
	{
		FactorGraphDiffs oneDiff = new FactorGraphDiffs();
		oneDiff._a = a;
		oneDiff._b = b;
		oneDiff._childDiffs = childFactorGraphDiffs;
		addDifference(oneDiff);
		//System.out.println("--addBNotA [" + a.getLabel() + "] [" + b.getLabel() + "]\n" + toString() + "\n]\n");
	}

	static private boolean CompareTwoNameables(INameable a, INameable b, boolean byName)
	{
		String aName = a.getName();
		String bName = b.getName();
		UUID aUUID = a.getUUID();
		UUID bUUID = b.getUUID();
		
		boolean bEquals =
			//identity same?
			(byName ?
					aName.equals(bName) :
					aUUID.equals(bUUID)
			) &&
			//if identity same, class same?
			a.getClass().equals(b.getClass());
		
		return bEquals;
	}
	static private boolean CompareTwoPorts(Port a, Port b, boolean byName)
	{
		INode aParent = a.node;
		INode aConnected = a.getConnectedNode();
		INode bParent = b.node;
		INode bConnected = b.getConnectedNode();
		
		boolean equal = CompareTwoNameables(aParent, bParent, byName) &&
		CompareTwoNameables(aConnected, bConnected, byName);
		
		return equal;
	}
	
	static private FactorGraphDiffs comparePorts(
			INameable a,
			INameable b,
			boolean quickExit,
			boolean byName) 
	{
		INode ndA = (INode) a;
		INode ndB = (INode) b;
		ArrayList<Port> aPorts = ndA.getPorts();
		ArrayList<Port> bPorts = ndB.getPorts();

		FactorGraphDiffs diffs = new FactorGraphDiffs(a, b);
		
		//variables don't have an ordered set of ports like
		//factors, as they can 'accumulate' connections
		//whereas factors have all their connections added
		//exactly in addFactor
		//So for variables we must 'go looking'
		HashSet<Integer> bPortIndices = new HashSet<Integer>();
		for(int i = 0; i < bPorts.size(); ++i)
		{
			bPortIndices.add(i);
		}
		
		//a against b
		boolean exit = false;
		for(int i = 0; i < aPorts.size() && !exit; ++i)
		{
			boolean same = true;
			if(i < bPorts.size())
			{
				//For Factors, just go pairwise
				if(ndA instanceof Factor ||
				   ndB instanceof Factor)
				{
					same = CompareTwoPorts(aPorts.get(i), bPorts.get(i), byName);
					if(!same)
					{
						diffs.addANotB(aPorts.get(i).getConnectedNode());
						diffs.addBNotA(bPorts.get(i).getConnectedNode());
					}
				}
				else
				{
					//For variables, go 'looking'
					int match = -1;
					for(Integer bIdx : bPortIndices)
					{
						if(CompareTwoPorts(aPorts.get(i), bPorts.get(bIdx), byName))
						{
							match = bIdx;
							break;
						}
					}
					if(match == -1)
					{
						diffs.addANotB(aPorts.get(i).getConnectedNode());
						//Not strictly complete; if there's a mismatch, a
						//and b have same number of ports, there must also
						//be a port in B but not in A
						//But we don't bother to go look for that.
					}
					else
					{
						bPortIndices.remove(match);
					}
				}
			}
			else
			{
				same = false;
				diffs.addANotB(aPorts.get(i).getConnectedNode());
			}
			exit = quickExit && !same;
		}
		if(!exit && bPorts.size() > aPorts.size())
		{
			for(int i = aPorts.size(); i < bPorts.size() && !exit; ++i)
			{
				diffs.addBNotA(bPorts.get(i).getConnectedNode());
				exit = quickExit;
			}
		}

		return diffs;
	}

	static private void compareNameableMaps(FactorGraphDiffs diffs,
										 NameableMap aMap,
										 NameableMap bMap,
										 boolean quickExit,
										 boolean byName) 
	{
		boolean someInANotB = false;
		
		//a against b
		for(INameable nA : aMap)
		{
			INameable nB = null;
			if(byName)
			{
				nB = bMap.get(nA.getName());
			}
			else
			{
				nB = bMap.get(nA.getUUID());
			}

			
			if(nB == null || //in a, missing from b
			    //or in both places with same identity, but different class
			   !nB.getClass().equals(nA.getClass()))
			{
				someInANotB = true;
				diffs.addANotB(nA);
				if(quickExit)
				{
					break;
				}//else keep accumulating
			}
			//else in both; are they the same?
			else
			{
				FactorGraphDiffs oneDiff = null;
				if(nA instanceof FactorGraph)
				{
					oneDiff = getFactorGraphDiffsNoRootSearch(
							   (FactorGraph) nA,
							   (FactorGraph) nB,
							   quickExit,
							   byName);
				}
				else
				{
					oneDiff = comparePorts(
							nA,
							nB,
							quickExit,
							byName);
				}
				if(oneDiff != null &&
				   !oneDiff.noDiffs())
				{
					diffs.addDifference(oneDiff);
					if(quickExit)
					{
						break;
					}//else keep accumulating
				}//else same
			}//end check one nA vs one nB
		}//end loop comparing a list against b list
		
		//look for in b but not a
		if((someInANotB || aMap.size() != bMap.size()) &&
		   !quickExit)
		{
			for(INameable nB : bMap)
			{
				INameable nA = null;
				if(byName)
				{
					nA = aMap.get(nB.getName());
				}
				else
				{
					nA = aMap.get(nB.getUUID());
				}

				if(nA == null)
				{
					diffs.addBNotA(nB);
					if(quickExit)
					{
						break;
					}//else keep accumulating
				}//else both there - we must hace checked for node equality above
			}//end loop comparing b list against a list
		}//else everything happy or quick bomb out
	}

	static public FactorGraphDiffs	getFactorGraphDiffs(
			   FactorGraph a,
			   FactorGraph b,
			   boolean quickExit,
			   boolean byName)  
	{
		FactorGraph aRoot = a;
		FactorGraph bRoot = b;
		while(aRoot.hasParentGraph())
		{
			aRoot = aRoot.getParentGraph();
		}
		while(bRoot.hasParentGraph())
		{
			bRoot = bRoot.getParentGraph();
		}
		return getFactorGraphDiffsNoRootSearch(
				   aRoot,
				   bRoot,
				   quickExit,
				   byName);
	}
	static public FactorGraphDiffs	getFactorGraphDiffsNoRootSearch(
								   FactorGraph a,
								   FactorGraph b,
								   boolean quickExit,
								   boolean byName)  
	{
		FactorGraphDiffs diffs = new FactorGraphDiffs(a, b);
		
		String aName = a.getName();
		String bName = b.getName();
		UUID aUUID = a.getUUID();
		UUID bUUID = b.getUUID();
		
		//System.out.println("++getFactorGraphDiffs [" + a.getLabel() + "] [" + b.getLabel() + "]\n");
		
		boolean bEquals =
			byName ?
					aName.equals(bName) :
					aUUID.equals(bUUID);
		if(!bEquals)
		{
			diffs.addDifference(a, b, null);
		}
		
		if(!a.hasParentGraph())
		{
			@SuppressWarnings("all")
			NameableMap aMap
			= new NameableMap((Collection)
					a.getBoundaryVariables()
						.values());

			@SuppressWarnings("all")
			NameableMap bMap
				= new NameableMap((Collection)
						b.getBoundaryVariables()
							.values());
			
			compareNameableMaps(diffs,
							 aMap,
							 bMap,
							 quickExit,
							 byName);
		}
		
		if(diffs.noDiffs() || !quickExit)
		{
			@SuppressWarnings("all")
			NameableMap aMap
			= new NameableMap((Collection)
					a.getNodesTop()
						.values());

			@SuppressWarnings("all")
			NameableMap bMap
				= new NameableMap((Collection)
						b.getNodesTop()
							.values());

			compareNameableMaps(diffs,
					 aMap,
					 bMap,
					 quickExit,
					 byName);
		}
		if(diffs.noDiffs() || !quickExit)
		{
			@SuppressWarnings("all")
			NameableMap aMap
			= new NameableMap((Collection)
					a.getNestedGraphs());

			@SuppressWarnings("all")
			NameableMap bMap
				= new NameableMap((Collection)
						b.getNestedGraphs());

			compareNameableMaps(diffs,
					 aMap,
					 bMap,
					 quickExit,
					 byName);
		}
		//System.out.println("--getFactorGraphDiffs [\n" + diffs.toString() + "\n]\n");
		return diffs;
	}
}
