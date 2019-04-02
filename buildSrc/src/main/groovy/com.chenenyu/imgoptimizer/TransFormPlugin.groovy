package com.chenenyu.imgoptimizer

import org.gradle.api.Plugin
import org.gradle.api.Project

class TransFormPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        println("trans Form")
    }
}