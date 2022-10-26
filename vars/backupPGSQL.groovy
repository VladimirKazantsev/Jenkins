#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		options {
			timestamps()
		}
		parameters {

			choice(name: 'IpServer',
				choices: [
					'Никуда',
					'192.168.50.230'
				],
				description: 'Адрес сервера куда пойдет по ssh'

			)
			
			string(
				name: 'dbServer',
				defaultValue: 'ubnt',
				description: 'Адрес сервера с pgsql'
			)
		}

		environment{
			BACKUPNAME=""
			BACKUPDATE=""
			NFS_PATH=""
			TARNAME=""
		}

		stages {
			stage("Set vars") {
				steps {
					script {
						BACKUPDATE=sh(returnStdout: true, script: 'date +"%Y-%m-%d-%H%M"').trim()
						BACKUPNAME="${params.IpServer}-${BACKUPDATE}.backup"
						NFS_PATH="/backup/${params.dbServer}"
						TARNAME="${BACKUPNAME}.tar.gz"
					}
				}
			}
			stage("Create dump DB") {
				steps {
					sh """
						ssh jenkins@${params.IpServer} 'bash -s << 'ENDSSH'
						echo =================Имя хоста===================
						docker exec pg5432 pg_dump \
						--username postgres \
						--verbose \
						--clean \
						--dbname elets \
						> ${NFS_PATH}/${BACKUPNAME}
						ls /backup
						
ENDSSH'
"""
				} 
			}
			stage("targz backup") {
				steps {
					sh """
						ssh jenkins@${params.IpServer} 'bash -s << 'ENDSSH'
						tar czvf ${NFS_PATH}/${TARNAME} -C ${NFS_PATH} ${BACKUPNAME}
						rm ${NFS_PATH}/${BACKUPNAME}
						
ENDSSH'
"""
				}
			}
		}
	}
}