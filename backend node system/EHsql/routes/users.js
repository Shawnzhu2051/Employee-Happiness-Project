var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var dbConfig = require('../db/DBConfig');
var userSQL = require('../db/usersql');
var crypto = require('crypto');
var bodyParser = require('body-parser');
var multipart = require('connect-multiparty');
var multipartMiddleware = multipart();

var pool = mysql.createPool( dbConfig.mysql );

var responseJSON = function (res, ret) {
    if(typeof ret === 'undefined') { 
      res.json({     
        code:'-200',     
        msg: 'operation failed'   
      }); 
    }
    else if(ret.errno == 1065){
      res.json({
        code:'1065',
        msg:'unknown'
      });
    }
    else if(ret.errno == 1062){
      res.json({
        code: '1062',
        msg: 'email address already exist'
      });
    }
    else { 
      res.json(ret); 
  }};

router.get('/sighIn',function(req,res,next){
  res.render('signIn');
});

router.post('/signIn',function(req,res){
  var name = req.body.name;
  var plaintext = req.body.pswd;
  var md5 = crypto.createHash('md5');
  var pswd = md5.update(plaintext).digest('hex');
  var email = req.body.email;
  var gender = req.body.gender;
  var age = req.body.age;
  var location = req.body.location;
  console.log(req.body);
  pool.getConnection(function(err,connection){
    connection.query(userSQL.signIn, [name,pswd,email,age,gender,location],function(err, result){
      if(err){
        responseJSON(res,err);
      }
      else{       
        res.send(req.body);
      }
      connection.release();
    })
  })
})
/////////////////////////////
router.get('/logIn',function(req,res,next){
  res.render('logIn');
});

router.post('/logIn',multipartMiddleware,function(req,res){
  console.log(req.body);
  var email = req.body.email;
  var plaintext = req.body.pswd;
  var md5 = crypto.createHash('md5');
  md5.update(plaintext)
  var pswd = md5.digest('hex');
  pool.getConnection(function(err,connection){
    connection.query(userSQL.logIn, [email],function(err, result){
      if(err){
        responseJSON(res,err);
      }
      else if(JSON.stringify(result) == "[]"){
        res.json({
        code: '1060',
        msg: 'User doesnt exist'
        });
      }
      else if(result[0].pswd != pswd){
        res.json({
        code: '1061',
        msg: 'Wrong password'
        });
      }
      else{
        res.send(req.body);
      }
    connection.release();
    })
  })
})
//////////////////////////////

router.get('/addData',function(req,res,next){
  res.render('addData');
})

router.post('/addData',function(req,res){
  var uid = req.body.uid;
  var did = req.body.did;
  var time = req.body.time;
  var AccelerationX = req.body.AccelerationX;
  var AccelerationY = req.body.AccelerationY;
  var AccelerationZ = req.body.AccelerationZ;
  var FlightsAscended = req.body.FlightsAscended;
  var FlightsDescended = req.body.FlightsDescended;
  var Rate = req.body.Rate;
  var SteppingGain = req.body.SteppingGain;
  var SteppingLoss = req.body.SteppingLoss;
  var StepsAscended = req.body.StepsAscended;
  var StepsDescended = req.body.StepsDescended;
  var TotalGain = req.body.TotalGain;
  var TotalLoss = req.body.TotalLoss;
  var Brightness = req.body.Brightness;
  var AirPressure = req.body.AirPressure;
  var Temperature = req.body.Temperature;
  var Calories = req.body.Calories;
  var CurrentMotion = req.body.CurrentMotion;
  var Pace = req.body.Pace;
  var Speed = req.body.Speed;
  var TotalDistance = req.body.TotalDistance;
  var Resistance = req.body.Resistance;
  var AngularVelocityX = req.body.AngularVelocityX;
  var AngularVelocityY = req.body.AngularVelocityY;
  var AngularVelocityZ = req.body.AngularVelocityZ;
  var HeartRate = req.body.HeartRate;
  var TotalSteps = req.body.TotalSteps;
  var RRInterval = req.body.RRInterval;
  var SkinTemperature = req.body.SkinTemperature;
  var IndexLevel = req.body.IndexLevel;

  pool.getConnection(function(err,connection){
    connection.query(userSQL.addData, [uid,did,time,AccelerationX,AccelerationY,AccelerationZ,FlightsAscended,FlightsDescended,Rate,SteppingGain,SteppingLoss,StepsAscended,StepsDescended,TotalGain,TotalLoss,Brightness,AirPressure,Temperature,Calories,CurrentMotion,Pace,Speed,TotalDistance,Resistance,AngularVelocityX,AngularVelocityY,AngularVelocityZ,HeartRate,TotalSteps,RRInterval,SkinTemperature,IndexLevel],function(err, result){
      if(err){
        responseJSON(res,err);// still need to update
        console.log(err);
      }
      else{       
        res.send(req.body);
        //console.log(req.body);
      }
      connection.release();
    })
  })
})

//////////////////////////////

router.get('/addFeedback',function(req,res,next){
  res.render('addFeedback');
})

router.post('/addFeedback',function(req,res){
  var uid = req.body.uid;
  var did = req.body.did;
  var time = req.body.time;
  var HappinessLevel = req.body.HappinessLevel;
  var WorkingContent = req.body.WorkingContent;
  var ThermalComfort = req.body.ThermalComfort;
  var Healthy = req.body.Healthy;
  var Satisfactory = req.body.Satisfactory;
  var EmotionType = req.body.EmotionType;
  var Conversation = req.body.Conversation;
  var SleepHour = req.body.SleepHour;

  pool.getConnection(function(err,connection){
    connection.query(userSQL.addFeedback, [uid,did,time,HappinessLevel,WorkingContent,ThermalComfort,Healthy,Satisfactory,EmotionType,Conversation,SleepHour],function(err, result){
      if(err){
        responseJSON(res,err);// still need to update
        console.log(err);
      }
      else{       
        res.send(req.body);
        console.log(req.body);
      }
      connection.release();
    })
  })
})

router.get('/getUserByEmail',function(req,res,next){
  pool.getConnection(function(err,connection){
    var param = req.query || req.params; 
    connection.query(userSQL.getUserByEmail, [param.email],function(err, result){
      responseJSON(res, result[0]);
      connection.release();
    })
  })
})

router.get('/getDataByEmail',function(req,res,next){
  pool.getConnection(function(err,connection){
    var param = req.query || req.params; 
    connection.query(userSQL.getDataByEmail, [param.email],function(err, result){
      responseJSON(res, result[0]);   
      connection.release();
    })
  })
})

router.get('/getDataByDevice',function(req,res,next){
  pool.getConnection(function(err,connection){
    var param = req.query || req.params; 
    connection.query(userSQL.getDataByDevice, [param.did], function(err,result){
      responseJSON(res, result[0]);   
      connection.release();
    })
  })
})

module.exports = router;