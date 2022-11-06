#!/usr/bin/env groovy

def call(String name, String lastname){
	pipeline {
		agent any

		stages{

			stage("Example") {
				steps {
					sh "echo Privet ${name} po familii ${lastname}"
				}
			}
		}
	}
}