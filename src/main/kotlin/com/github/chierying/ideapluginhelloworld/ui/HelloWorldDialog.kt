package com.github.chierying.ideapluginhelloworld.ui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.swing.JComponent
import javax.swing.JTextField

class HelloWorldDialog : DialogWrapper(true) {
    private val checked = BehaviorSubject.createDefault(false)

    init {
        init()
        title = "Hello World！"

    }

    override fun createCenterPanel(): JComponent? {
        return panel {
            noteRow("Login to get notified when the submitted\nexceptions are fixed.")
            val text = JTextField()
            row("Username:") { text() }
            row("Password:") { textField({ "输入密码" }, { println(it) }) }
            row {
                checkBox("check1")
                checkBox("check2")
                checkBox("check1")
            }
            row {
                checkBox("选我")
                row {
                    checkBox("check1")
                    checkBox("check2")
                }
                right {
                    link("Forgot password?") { /* custom action */ }
                }
            }
            noteRow("""Do not have an account? <a href="https://account.jetbrains.com/login">Sign Up</a>""")
            row {
                textField({ "" }, {})
            }.grow
        }
    }


}