var UserSQL = {  
                logIn:'SELECT * FROM user_info WHERE email = ?',
                signIn:'INSERT INTO user_info(name,pswd,email,age,gender,location) VALUES(?,?,?,?,?,?)',
                addData:'INSERT INTO user_data(uid,did,time,AccelerationX,AccelerationY,AccelerationZ,FlightsAscended,FlightsDescended,Rate,SteppingGain,SteppingLoss,StepsAscended,StepsDescended,TotalGain,TotalLoss,Brightness,AirPressure,Temperature,Calories,CurrentMotion,Pace,Speed,TotalDistance,Resistance,AngularVelocityX,AngularVelocityY,AngularVelocityZ,HeartRate,TotalSteps,RRInterval,SkinTemperature,IndexLevel) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)',
                getUserByEmail:'SELECT * FROM user_info WHERE email = ? ',
                getDataByEmail:'SELECT * FROM user_data WHERE email = ? ',
                getDataByDevice:'SELECT * FROM user_data WHERE did = ? ',
                queryAll:'SELECT * FROM user_data',
                addFeedback:'INSERT INTO user_feedback(uid,did,time,HappinessLevel,WorkingContent,ThermalComfort,Healthy,Satisfactory,EmotionType,Conversation,SleepHour) VALUES(?,?,?,?,?,?,?,?,?,?,?)',
              };
module.exports = UserSQL;