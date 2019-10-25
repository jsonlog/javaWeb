var zookeeper = require('node-zookeeper-client');

var CONNECTION_STRING = 'localhost:2181';
var OPTIONS = {
  sessionTimeout: 5000
};

var zk = zookeeper.createClient(CONNECTION_STRING, OPTIONS);
zk.on('connected', function () {
  // 获取节点数据
  zk.getData('/foo', function (error, data, stat) {
    console.log(data.toString());
  });
  zk.close();
});
zk.connect();