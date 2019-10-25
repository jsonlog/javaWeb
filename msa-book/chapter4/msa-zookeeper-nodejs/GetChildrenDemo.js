var zookeeper = require('node-zookeeper-client');

var CONNECTION_STRING = 'localhost:2181';
var OPTIONS = {
  sessionTimeout: 5000
};

var zk = zookeeper.createClient(CONNECTION_STRING, OPTIONS);
zk.on('connected', function () {
  // 列出子节点
  zk.getChildren('/', function (error, children, stat) {
    console.log(children);
  });
  zk.close();
});
zk.connect();