package localBlastWrapper;

import java.io.File;

public abstract class BlastWrapper {
	protected String baseProgramsPath;

	//////////////////////
	// Getters and Setters
	
	public String getBaseProgramsPath() {
		return baseProgramsPath;
	}
	public void setBaseProgramsPath(String baseProgramsPath) {
		if (baseProgramsPath.endsWith(File.separator)) {
			this.baseProgramsPath = baseProgramsPath;
		} else { 
			this.baseProgramsPath = baseProgramsPath + File.separator;
		}
		
	}

	
}
