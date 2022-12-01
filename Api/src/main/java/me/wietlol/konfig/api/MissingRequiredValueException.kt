package me.wietlol.konfig.api

class MissingRequiredValueException : Exception
{
	val valuePath: Path
	
	constructor(valuePath: Path)
		: super(valuePath.toFlatString())
	{
		this.valuePath = valuePath
	}
	
	constructor(message: String, valuePath: Path)
		: super(message + valuePath.toFlatString())
	{
		this.valuePath = valuePath
	}
	
	constructor(message: String, cause: Throwable, valuePath: Path)
		: super(message + " " + valuePath.toFlatString(), cause)
	{
		this.valuePath = valuePath
	}
	
	constructor(cause: Throwable, valuePath: Path)
		: super(valuePath.toFlatString(), cause)
	{
		this.valuePath = valuePath
	}
	
	constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean, valuePath: Path)
		: super(message + valuePath.toFlatString(), cause, enableSuppression, writableStackTrace)
	{
		this.valuePath = valuePath
	}
}
