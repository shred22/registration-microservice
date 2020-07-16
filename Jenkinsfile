pipeline {
    agent any
    options { buildDiscarder(logRotator(numToKeepStr: '5')) }
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
                               sh "cp lib/logging-log4j2-1.0.jar ."
                               sh "ls -l"
                         }
                    }
        }
        stage('Modify Config With Ansible') {
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
          stage('Start Application') {
                        steps {
                                 dir("${env.WORKSPACE}/deploy/scripts") {
                                    echo "Current Dir is:"
                                    sh "pwd"
                                    sh "./start-app.sh"
                                 }
                        }
                 }
        stage('Integration Tests') {
               steps {
                       sh "mvn test -Pintegration-tests,test-ci"
               }
        }
         stage('Stop Application') {
               steps {
                    echo "Current Dir is:"
                     sh "pwd"
                     dir("${env.WORKSPACE}/deploy/scripts") {
                          sh "./stop-app.sh"
                     }
                }
         }
        stage('Sonar Scan') {
                    steps {
                        sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=0675e85b9fa3ae24dd38fb9aa50715d7e7f23d5b'
                    }
        }
    }
}
