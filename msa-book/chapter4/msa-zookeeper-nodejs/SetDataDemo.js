var zookeeper = require('node-zookeeper-client');

var CONNECTION_STRING = 'localhost:2181';
var OPTIONS = {
  sessionTimeout: 5000
};

var zk = zookeeper.createClient(CONNECTION_STRING, OPTIONS);
zk.on('connected', function () {
  // 更新节点数据
  zk.setData('/foo', new Buffer('hi'), function (error, stat) {
    console.log(stat);
  });
  zk.close();
});
zk.connect();