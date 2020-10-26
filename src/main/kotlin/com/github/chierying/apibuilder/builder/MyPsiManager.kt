package com.github.chierying.apibuilder.builder

import com.github.chierying.ideapluginhelloworld.util.findSingleClass
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.file.PsiDirectoryImpl
import com.intellij.psi.impl.source.PsiJavaFileImpl

/**
 * 管理Psi状态
 */
data class MyPsiManager(val project: Project) {
    lateinit var methodName: String
    lateinit var className: String

    var controllerClass: PsiClass? = null
    var serviceClass: PsiClass? = null
    var serviceImplClass: PsiClass? = null
    var daoClass: PsiClass? = null
    var testClass: PsiClass? = null

    private val classExt = listOf("Controller", "Service", "ServiceImpl", "Dao", "ControllerTest")

    fun findClassQualifiedName(action: (List<String>) -> Unit) {
        val qualifiedNames = mutableListOf<String>()
        classExt.forEachIndexed { index, ext ->
            findSingleClass("$className$ext", project).also {
                when (index) {
                    0 -> controllerClass = it
                    1 -> serviceClass = it
                    2 -> serviceImplClass = it
                    3 -> daoClass = it
                    4 -> testClass = it
                }
                qualifiedNames.add(it?.qualifiedName ?: "")
            }
        }
        action(qualifiedNames)
    }

    private val controllerDirectory
        get() = controllerClass?.parent?.parent as PsiDirectoryImpl

    private val controllerPackageName
        get() = (controllerClass?.parent as PsiJavaFileImpl).packageName

    private val dtoPackageName: String
        get() {
            if (controllerDirectory.findSubdirectory("dto") == null) {
                WriteCommandAction.runWriteCommandAction(project) { controllerDirectory.createSubdirectory("dto") }
            }
            return "$controllerPackageName.dto"
        }

    private val voPackageName: String
        get() {
            if (controllerDirectory.findSubdirectory("vo") == null) {
                WriteCommandAction.runWriteCommandAction(project) { controllerDirectory.createSubdirectory("vo") }
            }
            return "$controllerPackageName.vo"
        }

    private val dtoType
        get() = "${methodName.withFirstCharUpperCased}DTO"

    private val voType
        get() = "${methodName.withFirstCharUpperCased}VO"

    fun createDTO(propertiesText: String) {
        JavaClassFileBuilder(dtoType, dtoPackageName, propertiesText, project)
                .createClass()
    }

    fun createVO(propertiesText: String) {
        JavaClassFileBuilder(voType, voPackageName, propertiesText, project)
                .createClass()
    }

    private lateinit var methodBuilder: MethodBuilder

    fun createMethod() {
        methodBuilder = MethodBuilder(methodName, className, dtoType, voType, project)
        methodBuilder.createControllerMethod(controllerClass!!)
    }
}