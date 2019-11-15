var express = require('express');
var router = express.Router();

router.get('/:studentId', function(req, res, next) {

  var student = req.path.substring(1);
  
  console.log('StudentId: %s', student);
  
  res.render('student', { studentId: student });
});

router.get('/api/:studentId', function(req, res, next) {

  var studentId = req.params.studentId;
  console.log('Request for student "%s"', studentId);
  
  res.json({"studentId": studentId,
            "firstName": studentId + "-firstName",
            "lastName": studentId + "-lastName",
            "age": 12});
});

module.exports = router;
