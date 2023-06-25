def call() {
    pipeline {
        options {
            ansiColor('xterm')
        }
        agent {
            node {
                label 'workstation'
            }
        }
        parameters {
            string(name: 'INFRA_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
        }
        stages {
            stage('Terraform Init') {
                steps {
                    withCredentials([aws(credentialsId: 'awskey', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]){
                        sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
                    }
                }
            }
            stage('Terraform apply') {
                steps {
                    withCredentials([aws(credentialsId: 'awskey', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]){
                        sh "terraform apply -auto-approve -var-file=env-${INFRA_ENV}/main.tfvars"
                    }
                }
            }
        }
        post {
            always {
                cleanWs()
            }
        }
    }
}