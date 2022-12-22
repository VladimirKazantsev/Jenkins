#!/usr/bin/env groovy

def call(Map configm) {
  pipeline {
    agent any
    stages {
      stage("Этап Первый") {
        steps {
          sh "echo Privet ${configm.name} po familii ${configm.lastname}"
        }
      }
      stage("Этап Второй") {
        steps {
					sh "pwd"
					sh "mkdir -p ./Distrib/Config"
          sh "echo Privet ${configm.name} po familii ${configm.lastname}"
        }
      }
    }
  }
}