#!/usr/bin/env groovy

def call(Map config) {
  pipeline {
    agent any
    stages {
      stage("Этап Первый") {
        steps {
          sh "echo Privet ${config.name} po familii ${config.lastname}"
        }
      }
    }
  }
}