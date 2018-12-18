HanoiTower
=====================

HanoiTower is a basic UI used to demonstrate solver algorithm, maybe it will become a playable version 

It uses JRebirth Application Framework as core engine.


Build it
-----------

Requires [Git](http://git-scm.com/), [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](http://maven.apache.org/).

    git clone https://github.com/JRebirth/HanoiTower.git
    cd HanoiTower/org.jrebirth.demo.hanoi
    mvn clean install -P AutoJar,WinExe


UI test is disabled because it requires Monocle  from OpenJFX 