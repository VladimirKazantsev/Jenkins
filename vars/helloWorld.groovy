#!/usr/bin/env groovy
def call() {
	pipeline {
    agent any
    stages {
      stage("Этап Первый") {
        steps {
          sh "echo Hello world"
					sh "pwd"
        }
			}
			stage("Этап_Два_Скрипты") {
				steps {
					script {
						def vova
						vova = ("drugtymoylyubeznyi")
						sh "echo ${vova}"
					}
				}
			}	
    }
	}
}	