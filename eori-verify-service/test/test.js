var supertest = require("supertest");
var should = require("should");

var server = supertest.agent("http://localhost:3001");
describe("eori number verification", function(){
    this.timeout(10000);
    it("should not verify", function(done){
        server
            .get("/verify?eori=100")
            .expect("Content-type",/json/)
            .expect(200)
            .end(function(err,res){
                res.status.should.equal(200);
                JSON.parse(res.text).status.should.equal("error");
                done();
            });
    });

});