package com.github.chierying.apibuilder.builder

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.impl.PsiElementFinderImpl

const val Private = "private "

abstract class ClassFileBuilder(
        protected val className: String,
        protected val packageName: String,
        protected val propertiesText: String,
        protected val project: Project
) {
    protected abstract val fileTemplate: String

    fun createClass() {
        val property = propertiesText.lines().joinToString(separator = "\n\t") { convertProperty(it) }
        val fileText = fileTemplate.replace("#CLassName#", className)
                .replace("#Properties#", property)
                .replace("#packageName#", packageName)
        WriteCommandAction.runWriteCommandAction(project) {
            val psiFile = PsiFileFactory.getInstance(project)
                    .createFileFromText("${className}.java", JavaFileType.INSTANCE, fileText)
            val psiDirectory = PsiElementFinderImpl(project).findPackage(packageName)
                    ?.directories?.get(0)
            psiDirectory?.add(psiFile)
        }
    }

    protected abstract fun convertProperty(line: String): String
}

class JavaClassFileBuilder(className: String, packageName: String, propertiesText: String, project: Project)
    : ClassFileBuilder(className, packageName, propertiesText, project) {

    override val fileTemplate: String = """
        package #packageName#;
        
        public class #CLassName#{
            #Properties#
        }
    """.trimIndent()

    override fun convertProperty(line: String): String {
        var ret = if (line.startsWith(Private)) line else "private $line"
        ret = if (ret.endsWith(";")) ret else "$ret;"
        return ret
    }
}