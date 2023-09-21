sbt dist &&
cp ./target/universal/play-scala-seed-1.0-SNAPSHOT.zip ./build/server.zip &&
unzip ./build/server.zip -d ./build/
