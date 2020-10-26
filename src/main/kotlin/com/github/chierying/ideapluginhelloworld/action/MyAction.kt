package com.github.chierying.ideapluginhelloworld.action

import com.github.chierying.ideapluginhelloworld.ui.DslPopup
import com.github.chierying.ideapluginhelloworld.ui.HelloWorldDialog
import com.github.chierying.ideapluginhelloworld.ui.MyNotifier
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages

class HelloWorldAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(PlatformDataKeys.PROJECT)
        val txt = Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon())
        Messages.showMessageDialog(project, "Hello, $txt!\n I am glad to see you.", "Information", Messages.getInformationIcon())
    }
}


class HelloWorldDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        HelloWorldDialog().showAndGet()
    }
}

class PopupAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
//        MyPopup().show()
        DslPopup().showPopup()
    }
}

class NotifyAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MyNotifier().notify("你好！")
    }
}