#!/usr/bin/env groovy

def call(Map config){
	sh "echo Privet ${config.name} po familii ${config.lastname}"
}