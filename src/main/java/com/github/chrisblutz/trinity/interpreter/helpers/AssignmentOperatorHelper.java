package com.github.chrisblutz.trinity.interpreter.helpers;

import com.github.chrisblutz.trinity.lang.TYObject;
import com.github.chrisblutz.trinity.lang.variables.VariableLoc;


/**
 * @author Christopher Lutz
 */
public interface AssignmentOperatorHelper {
    
    void assign(VariableLoc location, TYObject value);
}
