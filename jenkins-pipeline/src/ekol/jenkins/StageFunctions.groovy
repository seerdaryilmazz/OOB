package ekol.jenkins

/**
 * Created by kilimci on 07/07/2017.
 */
class StageFunctions implements Serializable {
    def script
    def config
    StageFunctions(script, config){
        this.script = script
        this.config = config
    }

    def checkout(){
        def branch = this.script.env.BRANCH_NAME
        this.script.checkout script.scm
        def parentPomBranch = branch
        def roroBranch = branch.startsWith("roro")
        if(!roroBranch && (branch != "dev" || branch != "test")){
            parentPomBranch = "master"
        }
        if(roroBranch && (branch != "roro-dev" || branch != "roro-test")){
            parentPomBranch = "roro-master"
        }
        this.script.sh "echo ${parentPomBranch}"
        this.script.sh "cd .. && git clone -b ${parentPomBranch} git@bitbucket.org:ekol1order/parent-pom.git"

    }

    def buildBackend(){
        def mvnHome = this.script.tool 'mvn3'
        def branch = this.script.env.BRANCH_NAME
        def buildCmd = "${mvnHome}/bin/mvn -B clean package"
        if(branch == 'test' || branch == 'master'){
            buildCmd = "${mvnHome}/bin/mvn -B clean verify package"
        }

        this.script.sh buildCmd
        this.script.step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml', allowEmptyResults: true])

    }

    def buildFrontend(){
        def mvnHome = this.script.tool 'mvn3'
        def branch = this.script.env.BRANCH_NAME
        def env = branch
        if(branch == 'master'){
            env = "prod"
        }
        def buildCmd = "${mvnHome}/bin/mvn -B clean package -P frontend-${env}"

        this.script.sh buildCmd

    }

    def version() {
        def matcher = this.script.readFile('pom.xml') =~ '<version>(.+)</version>'
        matcher ? matcher[0][1] : null
    }
    def artifactId() {
        def matcher = this.script.readFile('pom.xml') =~ '<artifactId>(.+)</artifactId>'
        matcher ? matcher[0][1] : null
    }


    def archive(){
        def v = version()
        def artifact = artifactId()
        this.script.archiveArtifacts artifacts: "**/target/${artifact}-${v}.jar", fingerprint: true
        this.script.stash includes: "**/target/${artifact}-${v}.jar", name: "${artifact}-${v}-jar"

    }


    def analysis(){
        def mvnHome = this.script.tool 'mvn3'
        this.script.sh "${mvnHome}/bin/mvn -B sonar:sonar"

    }

    def buildImage(){
        def project = project()
        def namespace = config.namespace
        this.script.sh "docker build -t ${namespace}/${project}:${this.script.env.BRANCH_NAME} ."
    }

    def project(){
        def tokens = "${this.script.env.JOB_NAME}".tokenize('/')
        return tokens[1]
    }

    def pushImage(){
        def project = project()
        def registry = config.registry
        def namespace = config.namespace
        this.script.sh "docker tag ${namespace}/${project}:${this.script.env.BRANCH_NAME} ${registry}/${namespace}/${project}:${this.script.env.BRANCH_NAME}"
        this.script.sh "docker push ${registry}/${namespace}/${project}:${this.script.env.BRANCH_NAME}"
    }
}
