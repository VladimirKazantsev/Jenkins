#!/usr/bin/env groovy

/**
 * Пайплайн для бэкапа БД pgsql
 * @author Дмитрий Баженов
 * */

def call(Map config) {

    pipeline {

        agent {

            label 'master'

        }

        options {

            buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))

            timestamps()

        }

        parameters {

            string( name: 'DbServer',
                defaultValue: config.DbServer,
                description: 'Адрес сервер c pgsql'
            )

            string( name: 'DbName',
                defaultValue: config.DbName,
                description: 'Имя БД, которую бэкапим'
            )

            string( name: 'DbUser',
                defaultValue: config.DbUser,
                description: 'Имя пользователя БД, от имени которого делаем бэкап'
            )

            string(
                name: 'conatainerName',
                defaultValue: config.conatainerName,
                description: 'Имя докер-контейнера с pgsql'
            )

        }


        environment {

                    BACKUPDATE=""
                    BACKUPNAME=""
                    TARNAME=""
                    PATH_IN_CONTAINER=""
                    PATH_IN_HOST=""
                    NFS_PATH=""

    }

        stages {
            
            stage("set vars") {

                steps {

                    script{

                        BACKUPDATE=sh(returnStdout: true, script: 'date +"%Y-%m-%d-%H%M"').trim()
                        BACKUPNAME="${params.DbName}-${params.DbServer}-${BACKUPDATE}.backup"
                        TARNAME="${BACKUPNAME}.tar.gz"
                        PATH_IN_CONTAINER="/var/lib/postgresql/data/backup/"
                        if (params.conatainerName == "pg5432" || params.conatainerName == "posgresqlplatform")
                        {
                            PATH_IN_HOST="/opt/docker-conts/pg5432/data/backup/"
                        }
                        else
                        {
                            PATH_IN_HOST="/opt/docker-conts/${params.conatainerName}/data/backup/"
                        }
                        NFS_PATH="/backup/${params.DbServer}/${params.conatainerName}/"

                    }

                }

            }
            stage("Ensure source folder exist") {

                steps {

                    sh """
  
                        ssh jenkins@${params.DbServer} 'bash -s << 'ENDSSH'
  
                        mkdir -p ${PATH_IN_HOST}
  
ENDSSH'
  
        """
                }

            }

            stage("Make a backup") {

                steps {

                    sh """
  
                        ssh jenkins@${params.DbServer} 'bash -s << 'ENDSSH'
  
                            docker exec ${params.conatainerName} pg_dump \
														#Удалил в оригинале --file надо вернуть назад
														--file ${PATH_IN_CONTAINER}${BACKUPNAME} \
                            --host "localhost" \
                            --port "5432" \
                            --username ${params.DbUser} \
                            --no-password \
                            --verbose \
                            --format=t \
                            --blobs \
                            --create \
                            --clean \
                            ${params.DbName}
                            #> ${NFS_PATH}${BACKUPNAME}
ENDSSH'
  
        """    

                }

            }

            stage("targz backup") {

                steps {
                                    
                    sh """
  
                        ssh jenkins@${params.DbServer} 'bash -s << 'ENDSSH'
                            #tar -czvf ${NFS_PATH}${TARNAME} -C ${NFS_PATH} ${BACKUPNAME}
														#rm ${NFS_PATH}${BACKUPNAME}
                            tar -czvf ${PATH_IN_HOST}${TARNAME} ${PATH_IN_HOST}${BACKUPNAME}
                            rm ${PATH_IN_HOST}${BACKUPNAME}
  
ENDSSH'
  
        """

                }

            }

            stage("Ensure dest folder exist") {

                steps {

                    sh """
  
                        ssh jenkins@${params.DbServer} 'bash -s << 'ENDSSH'
  
                        mkdir -p ${NFS_PATH}
  
ENDSSH'
  
        """
                }

            }
            
            stage("move backup") {
                steps {

                    sh """
  
                        ssh jenkins@${params.DbServer} 'bash -s << 'ENDSSH'
                            # Закоментить mv
                            mv ${PATH_IN_HOST}${TARNAME} ${NFS_PATH}${TARNAME}
  
ENDSSH'
  
        """
                }

            }

            stage("clean old ones") {

                steps {

                    sh """
  
                        ssh jenkins@${params.DbServer} 'bash -s << 'ENDSSH'
  
                            find ${NFS_PATH} -type f -mtime +7 -exec rm -f {} \\;
  
ENDSSH'
  
        """                   
                }
            }
        }

    post {

        failure {

            sendNotifications ( buildStatus : currentBuild.result,
              channelNotify : "uts-jenkins-chatid",
              emailAddr : "d.bazhenov@ase-ec.ru" )
               
        }

    }
    }
}
