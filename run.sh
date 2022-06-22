#!/bin/bash
cd msgboard
mvn package -Dmaven.test.skip=true
jar_name='msgboard-0.0.1-SNAPSHOT.jar'
if [ $(pgrep -f ${jar_name} | wc -l) -gt 0 ]; then
  pkill -9 -f ${jar_name}
fi
cd target
java -Xmx256m -jar ${jar_name} > /dev/null 2>&1 &

echo '正在启动, 请等待 Chrome 浏览器自动打开. 如果如果没有安装 Chrome, 请稍后手动访问 http://127.0.0.1:8629 查看'
sleep 10s
open -a "/Applications/Google Chrome.app" http://127.0.0.1:8629