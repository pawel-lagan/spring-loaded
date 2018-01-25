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


import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author laganp
 */
public class MonitorByteCodeModifier implements ClassFileTransformer {

    RedefiningClassMetaData redefiningClassMetaData;

    public MonitorByteCodeModifier(RedefiningClassMetaData redefiningClassMetaData) {
        super();
        this.redefiningClassMetaData = redefiningClassMetaData;
    }

    public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        return transformClass(redefiningClass, bytes);
    }

    private byte[] transformClass(Class classToTransform, byte[] b) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
            CtBehavior[] methods = cl.getDeclaredBehaviors();
            String methodName = redefiningClassMetaData.getMethodName();
            for (CtBehavior method : methods) {
                if (!method.isEmpty() && method.getName().equals(methodName)) {
                    changeMethod(method);
                }
            }
            b = cl.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return b;
    }

    private void changeMethod(CtBehavior method) throws NotFoundException, CannotCompileException {
        method.insertBefore("System.out.println(\"started method at \" + new java.util.Date());");
        method.insertAfter("System.out.println(\"ended method at \" + new java.util.Date());");
    }
}
