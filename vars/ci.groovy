def call(){
    pipeline{
        agent{
            label 'ansible'
        }
        stages{
            stage('clean jenkins workspace'){
                steps{
                    cleanWs()
                }
            }
            stage('compile/built'){
                steps{
                    echo 'compiling'
                    echo 'from shared library'
                    test.new1()
                }
            }
            stage('unit test'){
                steps{
                    echo 'running test cases'
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
}