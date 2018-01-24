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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author laganp
 */
public class MonitorConfigWatcher {

	public static final String configName = "test.txt";
	public static final String configDir = "C:\\Users\\szemrajr\\Documents\\";

	public static List<String> watchedMethods = new ArrayList<>();

	public void readCofnig() {
		List<String> newMethodList = new ArrayList<>();
		String filePath = configDir + configName;
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			stream.forEach(newMethodList::add);
			addWatcherForNewlyAddedMethod(newMethodList);
			removeWatcherNewlyAddedMethod(newMethodList);
		} catch (IOException ex) {
			Logger.getLogger(MonitorConfigWatcher.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void addWatcherForNewlyAddedMethod(List<String> newMethodList) {

		for (String newMethod : newMethodList) {
			if (watchedMethods != null && !watchedMethods.contains(newMethod)) {
				System.err.println("added: " + newMethod);
				watchedMethods.add(newMethod);
			}
		}
	}

	public void removeWatcherNewlyAddedMethod(List<String> newMethodList) {
		List<String> methodsToRemove = new ArrayList<>();
		if (watchedMethods != null) {
			for (String oldMethod : watchedMethods) {
				if (!newMethodList.contains(oldMethod)) {
					methodsToRemove.add(oldMethod);
					System.err.println("removed: " + oldMethod);
				}
			}
		}
		watchedMethods.removeAll(methodsToRemove);
	}

	public void setWatcher() {
		final Path path = FileSystems.getDefault().getPath(configDir);
		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
			final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			while (true) {
				final WatchKey wk = watchService.take();
				for (WatchEvent<?> event : wk.pollEvents()) {
					final Path changed = (Path) event.context();
					if (changed.endsWith(configName)) {
						readCofnig();
					}
				}
				boolean valid = wk.reset();
				if (!valid) {
					System.out.println("Key has been unregisterede");
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(MonitorConfigWatcher.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(MonitorConfigWatcher.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
