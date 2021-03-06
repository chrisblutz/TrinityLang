package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.procedures.TYProcedure;
import com.github.chrisblutz.trinity.lang.variables.VariableLoc;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;

import java.util.*;


/**
 * @author Christopher Lutz
 */
public class TYRuntime implements Cloneable {
    
    private Map<String, VariableLoc> variables = new HashMap<>();
    private List<VariableLoc> clonedVars = new ArrayList<>();
    private TYObject thisKeywordObject = TYObject.NONE;
    private TYObject scope = TYObject.NONE;
    private TYClass scopeClass = null;
    private boolean staticScope = false;
    private TYModule module = null;
    private TYClass tyClass = null;
    private TYProcedure procedure = null;
    private boolean isBroken = false, isChainingSwitch = false;
    private TYObject switchObj = TYObject.NONE;
    private boolean isReturning = false;
    private TYObject returnObject = TYObject.NONE;
    private TYModule[] importedModules = new TYModule[0];
    
    public void setVariableLoc(String variable, VariableLoc value) {
        
        variables.put(variable, value);
    }
    
    public void setVariable(String variable, TYObject value) {
        
        VariableLoc loc = new VariableLoc();
        setVariableLoc(variable, loc);
        VariableManager.put(loc, value);
    }
    
    public VariableLoc getVariableLoc(String variable) {
        
        return variables.get(variable);
    }
    
    public TYObject getVariable(String variable) {
        
        return VariableManager.getVariable(getVariableLoc(variable));
    }
    
    public boolean hasVariable(String variable) {
        
        return variables.containsKey(variable);
    }
    
    public TYObject getScope() {
        
        return scope;
    }
    
    public boolean isStaticScope() {
        
        return staticScope;
    }
    
    public void setScope(TYObject scope, boolean staticScope) {
        
        this.scope = scope;
        this.staticScope = staticScope;
    }
    
    public TYClass getScopeClass() {
        
        return scopeClass;
    }
    
    public void setScopeClass(TYClass scopeClass) {
        
        this.scopeClass = scopeClass;
    }
    
    public TYModule getModule() {
        
        return module;
    }
    
    public void setModule(TYModule module) {
        
        this.module = module;
    }
    
    public TYClass getTyClass() {
        
        return tyClass;
    }
    
    public void setTyClass(TYClass tyClass) {
        
        this.tyClass = tyClass;
    }
    
    public TYProcedure getProcedure() {
        
        return procedure;
    }
    
    public void setProcedure(TYProcedure procedure) {
        
        this.procedure = procedure;
    }
    
    public boolean isBroken() {
        
        return isBroken;
    }
    
    public void setBroken(boolean broken) {
        
        isBroken = broken;
    }
    
    public boolean isChainingSwitch() {
        
        return isChainingSwitch;
    }
    
    public void setChainingSwitch(boolean chainingSwitch) {
        
        isChainingSwitch = chainingSwitch;
    }
    
    public TYObject getSwitchObj() {
        
        return switchObj;
    }
    
    public void setSwitchObj(TYObject switchObj) {
        
        this.switchObj = switchObj;
    }
    
    public boolean isReturning() {
        
        return isReturning;
    }
    
    public void setReturning(boolean returning) {
        
        isReturning = returning;
    }
    
    public TYObject getReturnObject() {
        
        return returnObject;
    }
    
    public void setReturnObject(TYObject returnObject) {
        
        this.returnObject = returnObject;
    }
    
    public void importModules(String[] modules) {
        
        importedModules = new TYModule[modules.length];
        
        for (int i = 0; i < modules.length; i++) {
            
            String s = modules[i];
            
            if (ModuleRegistry.moduleExists(s)) {
                
                importedModules[i] = ModuleRegistry.getModule(s);
            }
        }
    }
    
    public TYModule[] getImportedModules() {
        
        return importedModules;
    }
    
    public boolean hasImportedModuleWithClass(String className) {
        
        for (TYModule module : getImportedModules()) {
            
            if (module.hasClass(className)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public TYClass getImportedClassWithModule(String className) {
        
        for (TYModule module : getImportedModules()) {
            
            if (module.hasClass(className)) {
                
                return module.getClass(className);
            }
        }
        
        return null;
    }
    
    public void setThis(TYObject thisObj) {
        
        this.thisKeywordObject = thisObj;
    }
    
    public TYObject getThis() {
        
        return thisKeywordObject;
    }
    
    public void clearVariables() {
        
        variables.clear();
    }
    
    @Override
    public TYRuntime clone() {
        
        try {
            
            TYRuntime runtime = (TYRuntime) super.clone();
            Map<String, VariableLoc> varCopy = new HashMap<>();
            varCopy.putAll(variables);
            runtime.variables = varCopy;
            runtime.clonedVars = new ArrayList<>(varCopy.values());
            runtime.scope = scope;
            runtime.scopeClass = scopeClass;
            runtime.staticScope = staticScope;
            runtime.module = module;
            runtime.procedure = procedure;
            runtime.importedModules = Arrays.copyOf(importedModules, importedModules.length);
            
            return runtime;
            
        } catch (CloneNotSupportedException e) {
            
            e.printStackTrace();
            return new TYRuntime();
        }
    }
    
    public void dispose(TYRuntime runtime) {
        
        disposeVariables(runtime);
        
        runtime.setBroken(isBroken());
        
        runtime.setReturning(isReturning());
        runtime.setReturnObject(getReturnObject());
    }
    
    public void disposeVariables(TYRuntime runtime) {
        
        for (String var : runtime.variables.keySet()) {
            
            if (hasVariable(var)) {
                
                runtime.setVariableLoc(var, getVariableLoc(var));
            }
        }
        
        for (VariableLoc loc : variables.values()) {
            
            if (!clonedVars.contains(loc)) {
                
                VariableManager.clearVariable(loc);
            }
        }
    }
}
