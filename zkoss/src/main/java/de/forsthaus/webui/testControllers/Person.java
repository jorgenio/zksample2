package de.forsthaus.webui.testControllers;

/**
 * Simple person class
 */
public class Person implements Comparable<Person> {

	String _firstName;
	String _lastName;

	/**
	 * Constructor method
	 * 
	 * @param firstName
	 *            The first name of the person
	 * @param lastName
	 *            The last name of the person
	 */
	public Person(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Person otherPerson) {
		if (otherPerson != null) {
			return toString().compareToIgnoreCase(otherPerson.toString());
		}
		return -1;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return _firstName;
	}

	/**
	 * Returns the full name
	 * 
	 * @return The full name of the person
	 */
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return _lastName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	@Override
	public String toString() {
		return getFullName();
	}
}
