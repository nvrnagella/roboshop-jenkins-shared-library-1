def call() {
    pipeline {
        agent {
            node {
                label 'workstation'
            }
        }
    }
}