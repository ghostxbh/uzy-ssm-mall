#!/bin/bash
start_app()
{
  git pull
  echo "----------------"
  git log | head -n 15
  echo "----------------"

  mvn clean install

  nohup java -jar target/mall.jar > nohup.out 2>&1 &

  echo "app is started!"
  echo `(ps -ef | grep mall.jar | grep -v grep | awk '{ print $2 }')` > mall.pid
}

stop_app()
{
  PID=$(cat mall.pid)
  if [ -z "${PID}" ]
  then
    echo "Application is already stopped"
  else
    echo kill -9 ${PID}
    kill -9 ${PID}
  fi
}

case $1 in
  'start')
    start_app
  ;;
  'stop')
    stop_app
  ;;
  'restart')
    stop_app
    sleep 1
    start_app
  ;;
  'log')
    tail -f /web/server/proxy/nohup.out
  ;;
  *)
    echo "please use: start | stop | restart | log, for this app"
  ;;
esac