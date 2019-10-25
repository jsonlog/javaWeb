var http = require('http');
var fs = require('fs');

var port = 1234;

http.createServer(function (req, res) {
  var url = req.url;
  if (url == '/') {
    url = '/index.html';
  }
  var path = __dirname + url;
  fs.exists(path, function (exists) {
    if (exists) {
      fs.readFile(path, function (err, data) {
        res.write(data.toString());
        res.end();
      });
    } else {
      res.writeHead(404);
      fs.readFile(__dirname + '/404.html', function (err, data) {
        res.write(data.toString());
        res.end();
      });
    }
  });
}).listen(port, function () {
  console.log('server is running at %d', port);
});
