package com.github.chierying.apibuilder.builder

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass

val String.withFirstCharLowerCased
    get() = this.mapIndexed { i: Int, c: Char -> if (i == 0) c.toLowerCase() else c }.joinToString(separator = "")

val String.withFirstCharUpperCased
    get() = this.mapIndexed { i: Int, c: Char -> if (i == 0) c.toUpperCase() else c }.joinToString(separator = "")


class MethodBuilder(
        private val methodName: String,
        private val className: String,
        private val dtoType: String,
        private val voType: String,
        private val project: Project,
) {
    private val controllerMethodTemplate = """
            public $voType ${methodName}(${dtoType} dto) {
                return ${className.withFirstCharLowerCased}Service.${methodName}(dto);
            }
    """.trimIndent()

    private fun createMethod(psiClass: PsiClass, methodTemplate: String) {
        val method = JavaPsiFacade.getInstance(project).elementFactory.createMethodFromText(methodTemplate, null)
        WriteCommandAction.runWriteCommandAction(project) {
            psiClass.add(method)
        }
    }

    fun createControllerMethod(psiClass: PsiClass) {
        createMethod(psiClass, controllerMethodTemplate)
    }
}