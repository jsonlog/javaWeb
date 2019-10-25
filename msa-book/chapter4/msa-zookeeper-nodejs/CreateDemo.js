var zookeeper = require('node-zookeeper-client');

var CONNECTION_STRING = 'localhost:2181';
var OPTIONS = {
  sessionTimeout: 5000
};

var zk = zookeeper.createClient(CONNECTION_STRING, OPTIONS);
zk.on('connected', function () {
  // 创建节点
  zk.create('/foo', new Buffer('hello'), function (error, path) {
    console.log(path);
  });
  zk.close();
});
zk.connect();