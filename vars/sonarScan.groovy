#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		
		options {
			timestamps()
		}
		
		stages {
			
			stage("Scan Sonar") {
				steps {
					echo "===============Scan Sonar======================="
					script {
					def data = libraryResource "sonar/repos.yml"
					writeFile (file: 'repos.yml', text: data)
					def datas = readYaml file: 'repos.yml'
					datas.each {

						println "${it.testpath}"
					}
					// writeFile file : 'Dockerfile', text: dockerfilecontents
					sh "pwd"
					sh "ls"
					sh "tree"
					}
				} 
			}
			stage("Example") {
        try {
            sh 'ls .'
        }
        catch (all) {
            echo 'Neverno zadana direktory'
        }
      }
		}
	}
}
