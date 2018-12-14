package com.jiopeel.enter.log;

import org.apache.ibatis.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class logImp implements Log {
	private static final Logger log = LogManager.getLogger("sqlinfo");
	
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public void error(String s, Throwable e) {
		
	}

	@Override
	public void error(String s) {
		
	}

	@Override
	public void debug(String s) {
		
	}

	@Override
	public void trace(String s) {
		
	}

	@Override
	public void warn(String s) {
		
	}

}
