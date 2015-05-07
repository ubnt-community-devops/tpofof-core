package com.tpofof.core.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IO.class);

	public String getContents(String filename) {
		String content = null;
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			InputStream resourceStream = classLoader.getResourceAsStream(filename);
			if (resourceStream != null) {
				content = IOUtils.toString(resourceStream);
			} else {
				LOGGER.error("File does not exist: " + filename);
			}
		} catch (IOException e) {
			LOGGER.error("Could not read file " + filename, e);
		}
		return content;
	}
}
