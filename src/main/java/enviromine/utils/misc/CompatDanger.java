package enviromine.utils.misc;

public @interface CompatDanger {
	
	//Annotates classes that have imports of third-party classes.
	
	//This very rarely causes problems, with crashes like `NoClassDefFoundError` if the corresponding mod for which there is compatibility in the main class is not loaded
	
	//!Classes annotated with this must be modified: all compatibility logic should be placed in a separate class
}
