package com.uama.transform

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class TransPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def transBoy=project.extensions.create("trans",MyTransForm)
        transBoy.insertHello(project)
        AppExtension appExtension=(AppExtension)project.getProperties().get('android')
        appExtension.registerTransform(transBoy)
    }
}