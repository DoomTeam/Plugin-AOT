package com.uama.transform.utils

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformOutputProvider
import groovyjarjarasm.asm.ClassWriter
import org.objectweb.asm.ClassReader
import com.android.utils.FileUtils

class UpdateClass {

    public static void updateClass(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {

        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(),
                Format.DIRECTORY)

        String srcDirPath = directoryInput.getFile().getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()

        File dir =  directoryInput.getFile()
        if (dir.isDirectory()) {
            for (final File file : FileUtils.getAllFiles(dir)) {
                String filePath = file.absolutePath
                String destFilePath = filePath.replace(srcDirPath, destDirPath)
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    File destFile = new File(destFilePath)
                    if (destFile.exists()) {
                        InputStream inputStream = new FileInputStream(destFile)
//                        ClassPrinter cp = new ClassPrinter()
                        ClassWriter classWriter =new ClassWriter()
                        ClassReader cr = new ClassReader(inputStream)
                        cr.accept(classWriter, 0)
                        classWriter.toByteArray()

                    }
                }
            }

        }

    }
}