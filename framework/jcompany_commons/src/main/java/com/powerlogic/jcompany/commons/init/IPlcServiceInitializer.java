/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.init;

import javax.servlet.ServletContext;

public interface IPlcServiceInitializer {

	public void init(ServletContext servletContext) throws Exception;
	
	public boolean isInitialized();
	
}
