var express = require('express');

var PORT = 1234;

var app = express();
app.use(express.static('.'));
app.listen(PORT, function () {
  console.log('server is running at %d', PORT);
});