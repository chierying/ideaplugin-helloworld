package com.github.chierying.apibuilder.ui

import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel


fun gridBagPanel(init: JPanel.() -> Unit): JPanel {
    val panel = JPanel()
    val layout = GridBagLayout()
    panel.layout = layout
    panel.init()
    return panel
}

fun <T : JComponent> JPanel.component(comp: T, init: T.() -> Unit = {}): T {
    add(comp)
    comp.init()
    return comp
}

fun JPanel.flowPanel(align: Int = FlowLayout.CENTER, hgap: Int = 5, vgap: Int = 5, init: JPanel.() -> Unit) {
    val row = JPanel(FlowLayout(align, hgap, vgap))
    add(row)
    row.init()
}

fun JComponent.gbc(x: Int, y: Int, w: Int = 1, h: Int = 1, action: GridBagConstraints.() -> Unit = {}) {
    val gridBagConstraints = GridBagConstraints().apply {
        gridx = x - 1
        gridy = y - 1
        gridwidth = w
        gridheight = h
        action()
    }
    (parent.layout as GridBagLayout).setConstraints(this, gridBagConstraints)
}