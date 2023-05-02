def call(){
    try{
        pipeline{
            agent{
                label 'ansible'
            }
            stages{
                stage('compile/built'){
                    steps{
                        script{
                            common.compile()
                        }

                    }
                }
                stage('unit test'){
                    steps{
                        script{
                            common.unittest()
                        }
                    }
                }
                stage('code analysis'){
                    environment{
                        USER='$(aws ssm get-parameters --names sonar.user --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                        PASS='$(aws ssm get-parameters --names sonar.pass --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                    }
                    steps{
                        script{
                            sh "sonar-scanner -Dsonar.host.url=http://172.31.4.215:9000 -Dsonar.login=${USER} -Dsonar.password=${PASS} -Dsonar.projectKey=${component}"
                        }
                    }
                }
                stage('code to centralized place'){
                    steps{
                        echo 'code to centralized place'
                    }
                }
            }
            post{
                always{
                    echo "this is post section"
                }
            }
        }
    }catch(Exception e){
        common.email("failed")
    }
}