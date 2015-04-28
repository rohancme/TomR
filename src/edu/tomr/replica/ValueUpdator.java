package edu.tomr.replica;

public class ValueUpdator {

	public enum Operation{
		ADD, DELETE, UNKNOWN
	}

	private final Operation operation;
	private final String key;

	public ValueUpdator(){
		this.operation = Operation.UNKNOWN;
		this.key = null;
	}

	public ValueUpdator(String key){
		this.operation = Operation.UNKNOWN;
		this.key = key;
	}

	public ValueUpdator(Operation operation, String key) {

		this.operation = operation;
		this.key = key;
	}

	public Operation getOperation() {
		return operation;
	}
	public String getKey() {
		return key;
	}

	@Override
	public boolean equals(Object obj) {

		ValueUpdator updatr = (ValueUpdator)obj;
		boolean retValue = (updatr.getKey().equals(this.key))?true:false;

		return retValue;
	}

}
