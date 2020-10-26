package com.github.chierying.ideapluginhelloworld.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil


class PsiNavigationDemoAction : AnAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val editor = anActionEvent.getData(CommonDataKeys.EDITOR)
        val psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE)
        if (editor == null || psiFile == null) return
        val offset = editor.caretModel.offset
        val infoBuilder = StringBuilder()
        val element = psiFile.findElementAt(offset)
        infoBuilder.append("光标所在元素: $element \n")
        if (element != null) {
            val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
            infoBuilder.append("所属方法: ${containingMethod?.name ?: "none"}\n")
            if (containingMethod != null) {
                val containingClass = containingMethod.containingClass
                infoBuilder.append("所属的类: ${if (containingClass != null) containingClass.name else "none"}\n")
                infoBuilder.append("所属方法的局部变量:\n")
                containingMethod.accept(object : JavaRecursiveElementVisitor() {
                    override fun visitLocalVariable(variable: PsiLocalVariable) {
                        infoBuilder.append(variable.name).append("\n")
                    }
                })
            }
        }
        Messages.showMessageDialog(anActionEvent.project, infoBuilder.toString(), "PSI Info", null)
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabled = editor != null && psiFile != null
    }
}

class PsiAddMethodAction : AnAction() {
    val text = """
            public String hello(String name) {
                System.out.println("hello" + name);
                return "hello" + name;
            }
    """.trimIndent()

    override fun actionPerformed(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        var cls: PsiClass? = null
        psiFile.accept(object : JavaRecursiveElementVisitor() {
            override fun visitClass(aClass: PsiClass?) {
                cls = aClass
            }
        })
        if (cls == null) return

        val factory = JavaPsiFacade.getInstance(e.project).elementFactory
        val method = factory.createMethodFromText(text, null)
        WriteCommandAction.runWriteCommandAction(e.project) {
            cls?.add(method)
        }
    }
}