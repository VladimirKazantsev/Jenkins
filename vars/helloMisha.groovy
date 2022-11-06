#!/usr/bin/env groovy

def call(String lastname, String name){
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