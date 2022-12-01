package me.wietlol.konfig.api

class InstantiationFailedException : Exception
{
	val errors: List<Throwable>
	
	constructor(errors: List<Throwable>)
		: super()
	{
		this.errors = errors
	}
	
	constructor(message: String, errors: List<Throwable>)
		: super(message)
	{
		this.errors = errors
	}
	
	constructor(message: String, cause: Throwable?, errors: List<Throwable>)
		: super(message, cause)
	{
		this.errors = errors
	}
	
	constructor(cause: Throwable, errors: List<Throwable>)
		: super(cause)
	{
		this.errors = errors
	}
	
	constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean, errors: List<Throwable>)
		: super(message, cause, enableSuppression, writableStackTrace)
	{
		this.errors = errors
	}
}
