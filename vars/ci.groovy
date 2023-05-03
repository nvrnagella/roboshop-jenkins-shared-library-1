def call(){
    try{
        pipeline{
            agent{
                label 'ansible'
            }
            environment{
                AWS_DEFAULT_REGION="us-east-1"
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
                    steps{
                        script{
                            sh "sonar-scanner -Dsonar.host.url=http://172.31.10.23:9000 -Dsonar.login=${USER} -Dsonar.password=${PASS} -Dsonar.projectKey=${component}"
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