package main;

public interface EntryFieldValidator {

	/**
	 * Runs a validation check on the given entry field.
	 * @param field the entry field to validate
	 * @return true if the field is valid; false otherwise
	 */
	public boolean isValid (EntryField field);
	
}
