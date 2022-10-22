#!/usr/bin/env groovy

def call(Map config = [:]) {
  pipeline {
    agent any
    stages {
      stage("Этап Первый") {
        steps {
          loadLinuxScript(name: 'hello-world.sh')
					sh "./hello-world.sh ${config.name} po familii ${config.lastname}"
        }
      }
      stage("Этап Второй") {
        steps {
					sh "pwd"
					sh "mkdir -p ./Distrib/Config"
        }
      }
    }
  }
}