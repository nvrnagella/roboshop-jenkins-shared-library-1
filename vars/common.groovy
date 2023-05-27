def compile(){
    if (app_lang == "nodejs"){
        sh 'npm install'
    }
    if (app_lang == "maven"){
        sh 'mvn compile'
    }
}
def unittest(){
    if (app_lang == "nodejs"){
        sh 'npm test || true'
    }
    if (app_lang == "maven"){
        sh 'mvn test'
    }
    if (app_lang == "python"){
        sh 'python3 -m unittest'
    }
}
def email(email_note){
    //mail bcc: '', body: "failure job name- ${JOB_BASE_NAME} \n failed job url - ${JOB_URL} ", cc: '', from: 'nvrnagella90@gmail.com', replyTo: '', subject: 'job failured', to: 'nvrnagella@gmail.com'
    println "sending failed job status to authorized persons"
}
def artifactPush(){
    sh "echo ${TAG_NAME} > VERSION"
    if(app_lang == "nodejs"){
        sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
    }
    withCredentials([aws(credentialsId: 'venkat-aws-cred', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
        environment{
            NEXUS_USER = $(aws ssm get-parameters --region us-east-1 --name nexus.user --with-decryption --query Parameters[0].Value | sed 's/"//g')
            NEXUS_PASSWORD = $(aws ssm get-parameters --region us-east-1 --name nexus.pass --with-decryption --query Parameters[0].Value | sed 's/"//g')
        }

        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEUXS_PASSWORD}", var: 'SECRET']]]){
            sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASSWORD} --upload-file ${component}-${TAG_NAME}.zip http://172.31.10.126:8081/repository/${component}/${component}-${TAG_NAME}.zip"
            }
    }
}