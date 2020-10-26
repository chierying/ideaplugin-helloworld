package com.github.chierying.ideapluginhelloworld.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.layout.panel
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class MyPopup {
    val content = JPanel().apply {
        val label = JLabel("弹出框")
        add(label)

        val bt = JButton("ok")
        bt.addActionListener {
            println("clicked ok")
            dispose()
        }
        add(bt)
    }
    val popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(content, null)
            .createPopup()

    fun dispose() {
        popup.dispose()
    }

    fun show() {
        popup.showInFocusCenter()

    }
}

class DslPopup {
    fun createPanel(): DialogPanel {
        return panel {
            noteRow("Login to get notified when the submitted\nexceptions are fixed.")
            row("Username:") { JTextField() }
            row("Password:") { textField({ "输入密码" }, { println(it) }) }
            row {
                checkBox("选我")
                row {
                    label("点我")
                    checkBox("点我")
                }
                right {
                    link("Forgot password?") { /* custom action */ }
                }
            }
            noteRow("""Do not have an account? <a href="https://account.jetbrains.com/login">Sign Up</a>""")
        }
    }

    fun showPopup() {
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(createPanel(), null)
                .createPopup()
                .showInFocusCenter()
    }
}

