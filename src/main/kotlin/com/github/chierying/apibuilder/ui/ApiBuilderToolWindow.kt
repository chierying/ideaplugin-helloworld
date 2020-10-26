package com.github.chierying.apibuilder.ui

import com.github.chierying.apibuilder.builder.MyPsiManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import java.awt.CardLayout
import javax.swing.JPanel

class ApiBuilderToolWindow : ToolWindowFactory {
    private lateinit var project: Project
    private val contentPanel: JPanel
    private lateinit var cardPanel: JPanel

    private lateinit var controllerCheckbox: JBCheckBox
    private lateinit var serviceCheckbox: JBCheckBox
    private lateinit var daoCheckbox: JBCheckBox
    private lateinit var testCheckbox: JBCheckBox
    private lateinit var pathVariableCheckbox: JBCheckBox

    private lateinit var classNameText: JBTextField
    private lateinit var methodNameText: JBTextField

    private lateinit var controllerText: JBTextField
    private lateinit var serviceText: JBTextField
    private lateinit var serviceImplText: JBTextField
    private lateinit var daoText: JBTextField
    private lateinit var testText: JBTextField

    private lateinit var dtoText: EditorTextField
    private lateinit var voText: EditorTextField

    private lateinit var myPsiManager: MyPsiManager

    init {
        val configPanel = panel {
            row {
                controllerCheckbox = checkBox("Controller", true).component
                serviceCheckbox = checkBox("Service", true).component
                daoCheckbox = checkBox("Dao", true).component
                testCheckbox = checkBox("Test", true).component
                pathVariableCheckbox = checkBox("Path Variable", false).component
            }
            row("输入类名按回车搜索:") {
                classNameText = textField({ "" }, {}).component.apply {
                    addActionListener {
                        myPsiManager.className = it.actionCommand
                        myPsiManager.findClassQualifiedName { names ->
                            names.forEachIndexed { index, name ->
                                when (index) {
                                    0 -> controllerText.text = name
                                    1 -> serviceText.text = name
                                    2 -> serviceImplText.text = name
                                    3 -> daoText.text = name
                                    4 -> testText.text = name
                                }
                            }
                        }
                    }
                }
            }
            row("输入方法名:") {
                methodNameText = textField({ "" }, {}).component
            }
            row("Controller:") {
                controllerText = textField({ "" }, {}).component
            }
            row("Service:") {
                serviceText = textField({ "" }, {}).component
            }
            row("ServiceImpl:") {
                serviceImplText = textField({ "" }, {}).component
            }
            row("Dao:") {
                daoText = textField({ "" }, {}).component
            }
            row("Test:") {
                testText = textField({ "" }, {}).component
            }
        }

        val dtoPanel = panel {
            row {
                dtoText = EditorTextField().apply {
                    setOneLineMode(false)
                    invoke(CCFlags.pushY, CCFlags.growY, CCFlags.growX)
                }
            }
        }

        val voPanel = panel {
            row {
                voText = EditorTextField().apply {
                    setOneLineMode(false)
                    invoke(CCFlags.pushY, CCFlags.growY, CCFlags.growX)
                }
            }
        }

        contentPanel = panel {
            fun selectCard(card: String) {
                (cardPanel.layout as CardLayout).show(cardPanel, card)
            }
            row {
                buttonGroup {
                    radioButton("config").component.apply {
                        isSelected = true
                        addActionListener { selectCard("config") }
                    }
                    radioButton("dto").component.apply {
                        isSelected = false
                        addActionListener { selectCard("dto") }
                    }
                    radioButton("vo").component.apply {
                        isSelected = false
                        addActionListener { selectCard("vo") }
                    }
                }
            }
            row {
                cardPanel = JPanel(CardLayout()).apply {
                    add(configPanel, "config")
                    add(dtoPanel, "dto")
                    add(voPanel, "vo")
                    invoke(CCFlags.growX, CCFlags.pushY, CCFlags.growY)
                }
            }
            row {
                button("Create") {
                    myPsiManager.methodName = methodNameText.text
                    myPsiManager.className = classNameText.text
                    myPsiManager.createDTO(dtoText.text)
                    myPsiManager.createVO(voText.text)
                    myPsiManager.createMethod()
                }
                button("Cancel") {}
            }
        }
    }


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.project = project
        this.myPsiManager = MyPsiManager(project)
        val factory = ContentFactory.SERVICE.getInstance()
        val content = factory.createContent(contentPanel, "api builder", false)
        toolWindow.contentManager.addContent(content)
    }
}