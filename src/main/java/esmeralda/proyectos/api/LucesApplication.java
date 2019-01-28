/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.api;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author lingmoy
 */
public class LucesApplication extends Application {
    
    	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public LucesApplication() {
		classes.add(Luces.class);
	}
 
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return Collections.EMPTY_SET;
}
    
    
}
