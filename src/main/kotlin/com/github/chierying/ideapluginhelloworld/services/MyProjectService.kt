package com.github.chierying.ideapluginhelloworld.services

import com.intellij.openapi.project.Project
import com.github.chierying.ideapluginhelloworld.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
