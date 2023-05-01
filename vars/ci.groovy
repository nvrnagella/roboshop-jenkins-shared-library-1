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
                            sh 'env'
                            common.unittest()
                        }
                    }
                }
                stage('code analysis'){
                    steps{
                        echo 'scanning code in sonar qube'
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
                    echo "sending an email"
                }
            }
        }
    }catch(Exception e){
        common.email("failed")
    }
}