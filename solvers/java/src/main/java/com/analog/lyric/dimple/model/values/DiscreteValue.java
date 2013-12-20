package com.analog.lyric.dimple.model.values;

import com.analog.lyric.dimple.model.domains.DiscreteDomain;

public abstract class DiscreteValue extends Value
{
	@Override
	public abstract DiscreteValue clone();
	
	@Override
	public abstract DiscreteDomain getDomain();
	
	@Override
	public abstract int getIndex();
	
	@Override
	public abstract void setIndex(int index);
}
