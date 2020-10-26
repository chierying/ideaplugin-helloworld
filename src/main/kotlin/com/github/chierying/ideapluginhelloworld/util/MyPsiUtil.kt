package com.github.chierying.ideapluginhelloworld.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache

/**
 * 查找单一类文件：一个文件一个类。
 * 只返回找到的第一个类
 */
fun findSingleClass(name: String, p: Project): PsiClass? = PsiShortNamesCache.getInstance(p)
        .getClassesByName(name, GlobalSearchScope.projectScope(p))
        .let { if (it.isEmpty()) null else it[0] }