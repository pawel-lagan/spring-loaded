/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springsource.loaded.monitor;

import org.springsource.loaded.ReloadableType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author laganp
 */
public class MonitorApi {

	private static Logger log = Logger.getLogger(MonitorApi.class.getName());

	private List<String> monitoredMethods;
	private MonitorOriginalVersionRegister monitorOriginalVersionRegister;

	public MonitorApi(MonitorOriginalVersionRegister monitorOriginalVersionRegister) {
		monitoredMethods = new ArrayList<>();
		this.monitorOriginalVersionRegister = monitorOriginalVersionRegister;
	}

    public void switchOn(String className, String methodName) throws ClassNotFoundException {
        boolean isMethodMonitored = isMethodMonitored(className, methodName);
		if(!isMethodMonitored){
			log.info(className + "." + methodName + " switching ON monitoring...");
			//byte[] modifiedBytes = asmMethod.getModified
			byte[] mockModifiedBytes = monitorOriginalVersionRegister.getOriginalClassVersion(className);
			InputStream modifiedClassStream = new ByteArrayInputStream(mockModifiedBytes);
			ReloadableType rType = monitorOriginalVersionRegister.getReloadableType(className);
			Long lmt = getLastModificationTime();
			rType.typeRegistry.loadNewVersion(rType, lmt, modifiedClassStream);
			log.info(className + "." + methodName + "monitoring switched ON!");
			monitoredMethods.add(className + "." + methodName);
		}else{
			log.info(className + "." + methodName + "is monitored already!");
		}
	}

    public void switchOff(String className, String methodName) throws ClassNotFoundException {
        boolean isMethodMonitored = isMethodMonitored(className, methodName);
		if(isMethodMonitored){
			log.info(className + "." + methodName + "is monitored now. Switching OFF...");
            byte[] originalVersionClass = monitorOriginalVersionRegister.getOriginalClassVersion(className);
            InputStream originalVersionClassStream = new ByteArrayInputStream(originalVersionClass);
			ReloadableType rType = monitorOriginalVersionRegister.getReloadableType(className);
			Long lmt = getLastModificationTime();
			rType.typeRegistry.loadNewVersion(rType, lmt, originalVersionClassStream);
			monitoredMethods.remove(className + "." + methodName);
		}else{
			log.info(className + "." + methodName + "isn't monitored already!");
		}
	}

	private boolean isMethodMonitored(String className, String methodName){
		return monitoredMethods.contains(className + "." + methodName);
	}

	private Long getLastModificationTime(){
		return new Timestamp(System.currentTimeMillis()).getTime();
	}
}

