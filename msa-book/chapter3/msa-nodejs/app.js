var http = require('http');

var PORT = 1234;

var app = http.createServer(function (req, res) {
  res.writeHead(200, {'Content-Type': 'text/html'});
  res.write('<h1>Hello111</h1>');
  res.end();
});
app.listen(PORT, function () {
  console.log('server is running at %d', PORT);
});
