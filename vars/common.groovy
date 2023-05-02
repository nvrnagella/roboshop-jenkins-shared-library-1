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
        sh 'npm test' || true
        println "hello from inittest"
    }
    if (app_lang == "maven"){
        sh 'mvn test'
    }
    if (app_lang == "python"){
        sh 'python3 -m unittest'
    }
}
def qualitycheck(){
    sh 'sonar-scanner -Dsonar.host.url=http://172.31.11.53:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=cart'
}
def email(email_note){
    //mail bcc: '', body: "failure job name- ${JOB_BASE_NAME} \n failed job url - ${JOB_URL} ", cc: '', from: 'nvrnagella90@gmail.com', replyTo: '', subject: 'job failured', to: 'nvrnagella@gmail.com'
    println "sending failed job status to authorized persons"
}