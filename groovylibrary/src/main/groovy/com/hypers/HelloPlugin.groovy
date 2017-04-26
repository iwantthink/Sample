package com.hypers

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
//        project.task('helloTask') {
//            group 'hypers'
//
//            doLast {
//                println 'hello task from helloPlugin'
//            }
//        }
//        project.extensions.add("personExt", PersonExt)
//
//        project.task('printPerson') {
//            group 'hypers'
//
//            doLast {
//                PersonExt ext = project.personExt;
//
//                println ext
//            }
//        }

        NamedDomainObjectContainer<Person> persons = project.container(Person)

        project.extensions.add("team", persons)

        def task = project.task('showTeam') {
            group 'hypers'
            doLast {
                def team1 = project.team

                println team1
            }
        }


    }
}
