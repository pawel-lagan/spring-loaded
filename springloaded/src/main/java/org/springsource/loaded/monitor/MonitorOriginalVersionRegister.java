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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author laganp
 */
public class MonitorOriginalVersionRegister {


    private Map<String, ReloadableType> rTypeClassMap = new HashMap<>();
    private Map<String, byte[]> originalClassMap = new HashMap<>();

    public byte[] getOriginalClassVersion(String dottedClassName) throws ClassNotFoundException {

        byte[] bytesFromMap = originalClassMap.get(dottedClassName);

        return bytesFromMap;

    }

    public void register(ReloadableType rtype, String dottedClassName, byte[] bytes) {

        //czy powinnismy w jednej mapie przechowywac klasy oryginalne a w drugiej najnowsza wersje zmodyfikowanej klasy ?

        if (rTypeClassMap.get(dottedClassName) == null && originalClassMap.get(dottedClassName) == null) {
            originalClassMap.put(dottedClassName, bytes);
            rTypeClassMap.put(dottedClassName, rtype);
        } else {
            rTypeClassMap.replace(dottedClassName, rtype);
        }
    }

    public ReloadableType getReloadableType(String dottedClassName) throws ClassNotFoundException {

        //czy jezeli nie znajdziemy klasy na mapie to powinnismy ja sciagac z TypeRegister ?

        ReloadableType reloadableType = null;
        reloadableType = rTypeClassMap.get(dottedClassName);
//        if(reloadableType == null) {
//            Class cls = Class.forName(dottedClassName);
//            ClassLoader loader = new ClassLoader(cls.getClassLoader());
//            TypeRegistry typeRegistryFor = TypeRegistry.getTypeRegistryFor(loader);
//            String slashedClassName = dottedClassName.replace(".", "/");
//            reloadableType = typeRegistryFor.getReloadableType(slashedClassName);
//        }

        return reloadableType;
    }

}
