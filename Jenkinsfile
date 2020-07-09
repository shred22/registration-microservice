pipeline {
    agent any
    options { buildDiscarder(logRotator(numToKeepStr: '6')) }
    parameters {
            string(name: 'ansible_branch', defaultValue: 'master', description: 'Which Ansible branch to use for reading template vars ?')
    }
    /* tools {
            maven 'apache-maven-3.0.1'
    } */
    stages {

        stage('Build With Unit Tests') {
                    steps {
                        sh 'mvn clean install -Punit-tests'
                    }
                }

        stage('Prepare Environment') {
                    steps {
                         dir("${env.WORKSPACE}/deploy") {
                               echo "Displaying user info"
                               sh "rm -rfv *"
                               sh "cp -r ../target/boot-oai-log4j2-zip.zip ."
                               sh "unzip -o boot-oai-log4j2-zip.zip"
                               sh "ls -l"
                               //sh "./scripts/start-app.sh"

                         }
                    }
        }
        stage('Ansible Tweaks') {
                     steps {
                            echo 'Deploying....'
                            dir("${env.WORKSPACE}/ansible") {
                              sh "ls -l"
                              sh "rm -rfv *"
                              sh "ls -l"
                              sh "cp -r /home/dell/shreyas/Programming/Ansible/pipeline-data ."
                              sh """ansible-playbook -i ./pipeline-data/inventory.txt ./pipeline-data/templating-playbook.yaml"""
                            }
                     }

         }
         stage('Deploy App') {
                 steps {

                 echo "Deploying .!!!!!!!!!!"
                      /* dir("${env.WORKSPACE}/deploy") {
                           sh "./scripts/start-app.sh"
                        } */
                  }
         }

        stage('Integration Tests') {
               steps {
                   /*  dir("${env.WORKSPACE}/deploy") {
                   sh './scripts/stop-app.sh'
                       } */
                       sh "mvn clean install -Pintegration-tests"
               }
        }
        stage('Sonar Scan') {
                    steps {
                        sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=0675e85b9fa3ae24dd38fb9aa50715d7e7f23d5b'
                    }
        }
    }
}
