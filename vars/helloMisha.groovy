#!/usr/bin/env groovy

def call(String name, String lastname){
	sh "echo Privet ${name} po familii ${lastname}"
}