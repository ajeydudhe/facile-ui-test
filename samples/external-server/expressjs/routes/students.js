var express = require('express');
var router = express.Router();

function HTML_ACCEPTED (req, res, next) { return req.accepts("html") ? next() : next("route") }
function JSON_ACCEPTED (req, res, next) { return req.accepts("json") ? next() : next("route") }

router.get('/:studentId', HTML_ACCEPTED, function(req, res, next) {

  res.render('student', { title: 'Student Details' });
});

router.get('/:studentId', JSON_ACCEPTED, function(req, res, next) {

  res.json({"studentId": "bar"});
});

module.exports = router;
