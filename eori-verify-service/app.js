var express = require('express');
var request = require('request');
var cheerio = require('cheerio');

var app = express();

var serviceName = 'eori-verify-service';
require('../nodejs-services-config/server-config.js').config(app, serviceName);

app.get('/info', function (req, res) {
    res.json({});
});

app.get('/verify', function (req, res) {
    request({
        method: 'GET',
        url: 'http://ec.europa.eu/taxation_customs/dds2/eos/eori_detail.jsp',
        //url: 'http://10.1.70.92:30303/taxation_customs/dds2/eos/eori_detail.jsp',
        qs: {Lang:"en", EoriNumb: req.query.eori},
        headers: {
            'cache-control': 'no-cache',
            'accept-language': 'en-US,en;q=0.8,tr;q=0.6',
            'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36',
            origin: 'http//ec.europa.eu',
            Referer:"http://ec.europa.eu/taxation_customs/dds2/eos/eori_validation.jsp",
            accept: '*/*'
        }
    }, function(error, response, body){
        if (error){
            res.json({status:"error", message:"Connection Error"});
            return;
        }
        $ = cheerio.load(body);
        var status = "";
        $("#infoTd > table > tr:nth-child(2) > td:nth-child(1)").each(function(i, elem){
            status = $(this).text().indexOf("not valid") >= 0 ? "error" : "success";
        });
        var eoriName="";
        var addressName="";
        var streetNumber ="";
        var postalCode="";
        var city ="";
        var countryCode ="";

        
        if('success' === status){
            eoriName = $("#infoTd > table > tr:nth-child(3) > td:nth-child(2)").text();  
            addressName = $("#infoTd > table > tr:nth-child(4) > td:nth-child(2)").text();
            postalCode = $("#infoTd > table > tr:nth-child(6) > td:nth-child(2)").text();
            city = $("#infoTd > table > tr:nth-child(7) > td:nth-child(2)").text();
            countryCode = $("#infoTd > table > tr:nth-child(8) > td:nth-child(2)").text();
            }

        res.json({status:status, eoriName:eoriName, addressName:addressName, streetNumber:streetNumber, postalCode:postalCode, city:city, countryCode:countryCode});
    });

});