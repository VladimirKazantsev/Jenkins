#!/usr/bin/env groovy
def call() {
	pipeline {
    agent any
		options {
			timestamps()
		}
    stages {
      stage("Этап Первый") {
        steps {
          sh "echo Hello world"
					sh "pwd"
        }
			}
			stage("Этап_Два_Скрипты") {
				steps {
					echo "================Start of block script===================="
					script {
					// Определяем переменную vova
						def vova
						vova = "elki"
					// Распечатываем переменную vova
						sh "echo This is: ${vova}"
					// Создание папки
						sh "mkdir -p ${vova}/Distrib/Identity"
						sh "ls"
						sh "tree"
					}
				}
			}	
    }
	}
}	