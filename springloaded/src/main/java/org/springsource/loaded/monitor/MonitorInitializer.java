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
import org.springsource.loaded.TypeRegistry;

/**
 *
 * @author laganp
 */
public class MonitorInitializer {

	private MonitorOriginalVersionRegister reg = new MonitorOriginalVersionRegister();
	private MonitorApi api = new MonitorApi(reg);
	private MonitorConfigWatcher watcher = new MonitorConfigWatcher(api);
	private MonitorByteCodeModifier mod = new MonitorByteCodeModifier();

	public MonitorInitializer() {
		watcher.readConfig();
		watcher.setWatcher();
	}

	public ReloadableType onClassLoad(String className, TypeRegistry typeRegistry, byte[] bytes) {
		ReloadableType rtype = typeRegistry.addType(className, bytes);
		return rtype;
	}

}
