package de.justitsolutions.justreddit.exception;

public class JustRedditException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1190251592458200925L;

	public JustRedditException(String exMessage) {
		super(exMessage);
	}
	
	//wenn im backend (insb. bei restapi) exception auftritt, soll keine technische information an den user gegeben werden
	//sondern fehlermeldung verstaendlicher ausgegeben werden 
}
